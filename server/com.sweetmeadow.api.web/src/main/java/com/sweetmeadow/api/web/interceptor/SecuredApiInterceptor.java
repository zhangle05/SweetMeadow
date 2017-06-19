/**
 * 
 */
package com.sweetmeadow.api.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.octopusdio.api.common.annotation.Login;
import com.octopusdio.api.common.domain.RESTResult;
import com.sweetmeadow.api.bridge.service.ClientAppService;
import com.sweetmeadow.api.bridge.utils.SharedConstants;

import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
public class SecuredApiInterceptor extends HandlerInterceptorAdapter {

    private static Log LOG = LogFactory.getLog(SecuredApiInterceptor.class);

    @Autowired
    private ClientAppService clientAppService;

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        LOG.info("AuthenticationInterceptor: Pre-handle, request URI is:"
                + request.getRequestURI());
        if (handler instanceof HandlerMethod) {
            HandlerMethod hmethod = (HandlerMethod) handler;
            if (!checkNonce(hmethod, request, response)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkNonce(HandlerMethod hmethod,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Method method = hmethod.getMethod();
        // 用户登录
        Login login = hmethod.getBean().getClass().getAnnotation(Login.class);
        if (login == null || !login.required()) {
            login = method.getAnnotation(Login.class);
        }
        if (login != null && login.required()) {
            LOG.info("login required.");
            String userId = String.valueOf(request.getSession().getAttribute("USERID"));

            boolean isJson = method.getAnnotation(ResponseBody.class) != null;
            if (StringUtils.isEmpty(userId)) {
                LOG.info("AuthenticationInterceptor: recirecting "
                        + request.getRequestURI() + " to login");
                error400(request, response, isJson);
                return false;
            } else {
                LOG.info("Pass the interceptor, userId is:" + userId);
                return true;
            }
        }
        LOG.info("Pass the interceptor, no recirection.");
        return true;
    }

    private void error400(HttpServletRequest request,
            HttpServletResponse response, boolean isJson) throws IOException {
        LOG.debug("error 400, isJson:" + isJson);
        // user in session is empty
        StringBuilder builder = new StringBuilder(
                request.getRequestURL().toString());
        String qs = request.getQueryString();
        if (!StringUtils.isEmpty(qs)) {
            builder.append("?");
            builder.append(qs);
        }
        String prefix = "";
        Object prefixObj = request.getAttribute(SharedConstants.URL_PREFIX_KEY);
        if (prefixObj != null) {
            prefix = prefixObj.toString();
        }
        if (StringUtils.isEmpty(prefix)) {
            prefix = "lecture";
        }
        LOG.info("prefix is:" + prefix);
        if (isJson) {
            String redirctUrl = "/" + prefix + "/account/login";
            JSONObject obj = new JSONObject();
            obj.put("code", 400);
            obj.put("msg", "Invalidate Request, please login.");
            obj.put("url", redirctUrl);
            String returnurl = request.getHeader("referer"); // Yes, with the
                                                             // legendary
                                                             // misspelling.
            if (!returnurl.contains(prefix)) {
                returnurl = returnurl.replaceFirst("lecture", prefix);
            }
            LOG.info("new returnurl is:" + returnurl);
            obj.put("returnurl", returnurl);
            PrintWriter write = response.getWriter();
            write.write(obj.toString());
            write.flush();
        } else {
            String returnurl = URLEncoder.encode(builder.toString(), "UTF-8");
            if (!returnurl.contains(prefix)) {
                returnurl = returnurl.replaceFirst("lecture", prefix);
            }
            LOG.info("new returnurl is:" + returnurl);
            response.sendRedirect(
                    String.format("/" + prefix + "/account/login?returnurl=%s", returnurl));
        }
    }


    /**
     * check if the sign matches the requested parameters
     *
     * @return AjaxResult.SUCCESS if the sign matches, otherwise return error
     *         codes
     */
    protected RESTResult checkSign(String appId, String sign,
            HttpServletRequest request, boolean shouldCheckTimestamp) {
        if (StringUtils.isEmpty(sign)) {
            return new RESTResult(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "sign is empty!");
        }
        try {
            String correctSign = getCorrectSign(request, appId,
                    shouldCheckTimestamp);
            LOG.info("check sign for '" + appId + "', correct sign is:"
                    + correctSign);
            return compareSign(sign, correctSign);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new RESTResult(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new RESTResult(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new RESTResult(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected String getCorrectSign(HttpServletRequest request, String appKey,
            boolean shouldCheckTimestamp) throws UnsupportedEncodingException,
            NoSuchAlgorithmException, IllegalArgumentException {
        HashMap<String, String> keyValues = new HashMap<String, String>();
        @SuppressWarnings("rawtypes")
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement().toString();
            String value = request.getParameter(name);
            keyValues.put(name, value);
        }
        return getCorrectSign(keyValues, appKey, shouldCheckTimestamp);
    }

    private RESTResult compareSign(String inputSign,
            String correctSignOrErrorMsg) {
        LOG.debug("correct sign is:" + correctSignOrErrorMsg);
        if (SharedConstants.ERROR_SIGN_CLIENT_NOT_FOUND
                .equals(correctSignOrErrorMsg)
                || SharedConstants.ERROR_SIGN_TIMESTAMP_EMPTY
                        .equals(correctSignOrErrorMsg)
                || SharedConstants.ERROR_SIGN_TIMESTAMP_EXPIRED
                        .equals(correctSignOrErrorMsg)) {
            return new RESTResult(HttpStatus.UNAUTHORIZED,
                    correctSignOrErrorMsg);
        }
        if (!inputSign.equals(correctSignOrErrorMsg)) {
            return new RESTResult(HttpStatus.UNAUTHORIZED,
                    "incorrect sign!");
        }
        return new RESTResult(HttpStatus.OK);
    }

    protected String getCorrectSign(HashMap<String, String> allKeyValues,
            String appId, boolean shouldCheckTimestamp)
            throws UnsupportedEncodingException, IllegalArgumentException,
            NoSuchAlgorithmException {
        LOG.debug(
                "sign excluded, shouldCheckTimestamp:" + shouldCheckTimestamp);
        allKeyValues.remove("sign");
        if (shouldCheckTimestamp) {
            if (!allKeyValues
                    .containsKey(SharedConstants.TIMESTAMP_PARAM_NAME)) {
                LOG.warn("timestamp not in the request");
                return SharedConstants.ERROR_SIGN_TIMESTAMP_EMPTY;
            }
            String tsStr = allKeyValues
                    .get(SharedConstants.TIMESTAMP_PARAM_NAME);
            try {
                long ts = Long.parseLong(tsStr);
                if (SharedConstants.TIMESTAMP_TORLERRANCE < Math
                        .abs(System.currentTimeMillis() - ts)) {
                    return SharedConstants.ERROR_SIGN_TIMESTAMP_EXPIRED;
                }
            } catch (Exception ex) {
                return SharedConstants.ERROR_SIGN_TIMESTAMP_EMPTY;
            }
        }
        String appSecret = clientAppService.getClientAppSecret(appId);
        if (appSecret != null) {
            return generateSign(allKeyValues, appId, appSecret);
        }
        return SharedConstants.ERROR_SIGN_CLIENT_NOT_FOUND;
    }

    public String generateSign(HashMap<String, String> keyValues, String appKey,
            String secretKey)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String paramStr = "";
        List<String> sortedKeys = new ArrayList<String>(keyValues.size());
        sortedKeys.addAll(keyValues.keySet());
        Collections.sort(sortedKeys);
        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            String value = keyValues.get(key);
            if (null == key || "".equals(key) || null == value
                    || "".equals(value)) {
                continue;
            }
            paramStr += key + "=" + value;
        }
        String secretText = secretKey + "from=" + appKey + paramStr;
        LOG.debug("secret text is:" + secretText);
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(secretText.getBytes("UTF-8"));
        String sign = byteToHex(crypt.digest());
        return sign;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
