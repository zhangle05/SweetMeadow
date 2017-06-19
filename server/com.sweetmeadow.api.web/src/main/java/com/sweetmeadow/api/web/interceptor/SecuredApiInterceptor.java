/**
 * 
 */
package com.sweetmeadow.api.web.interceptor;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.octopusdio.api.common.annotation.Secured;
import com.octopusdio.api.common.domain.RESTResult;
import com.sweetmeadow.api.bridge.service.ClientAppService;
import com.sweetmeadow.api.bridge.utils.SharedConstants;
import com.sweetmeadow.api.web.controller.filter.BodyCacheRequestWrapper;

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
        Secured secure = hmethod.getBean().getClass()
                .getAnnotation(Secured.class);
        if (secure == null || !secure.required()) {
            secure = method.getAnnotation(Secured.class);
        }
        if (secure != null && secure.required()) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            boolean isPost = false;
            if (rm != null && rm.method().length > 0) {
                isPost = (rm.method()[0] == RequestMethod.POST);
            }
            LOG.info("nonce required. isPost: " + isPost);
            RESTResult result = null;
            if (isPost) {
                result = checkPostSign(request, secure.level());
            } else {
                result = checkGetSign(request, secure.level());
            }
            if (result == null) {
                result = new RESTResult(HttpStatus.BAD_REQUEST,
                        "Check sign failed!");
            }
            if (result.getData() == HttpStatus.OK) {
                return true;
            }
            if (!(result.getData() instanceof HttpStatus)) {
                result.setData(HttpStatus.BAD_REQUEST);
            }
            int code = ((HttpStatus) result.getData()).value();
            response.setStatus(code);
            response.setContentType("text/html; charset=utf-8");
            JSONObject obj = new JSONObject();
            obj.put("code", code);
            obj.put("msg", result.getMsg());
            response.getWriter().write(obj.toString());
            response.getWriter().flush();
            return false;
        }
        return true;
    }

    protected RESTResult checkPostSign(HttpServletRequest request, int level) {
        if (!(request instanceof BodyCacheRequestWrapper)) {
            return new RESTResult(HttpStatus.BAD_REQUEST,
                    "request type error!");
        }
        String requestBody = ((BodyCacheRequestWrapper) request).getBody();
        if (StringUtils.isEmpty(requestBody)) {
            return new RESTResult(HttpStatus.BAD_REQUEST,
                    "request body empty!");
        }
        try {
            JSONObject json = JSONObject.fromObject(requestBody);
            return checkSign(json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new RESTResult(HttpStatus.BAD_REQUEST,
                    "request body parse error!");
        }
    }

    protected RESTResult checkGetSign(HttpServletRequest request, int level) {
        JSONObject keyValues = new JSONObject();
        @SuppressWarnings("rawtypes")
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = em.nextElement().toString();
            String value = request.getParameter(name);
            keyValues.put(name, value);
        }
        return checkSign(keyValues);
    }

    /**
     * check if the sign matches the requested parameters
     *
     */
    protected RESTResult checkSign(JSONObject keyValues) {
        String appName = keyValues
                .optString(SharedConstants.CLIENT_NAME_PARAM_NAME);
        if (StringUtils.isEmpty(appName)) {
            return new RESTResult(HttpStatus.BAD_REQUEST,
                    "parameter '" + SharedConstants.CLIENT_NAME_PARAM_NAME
                            + "' is empty!");
        }
        String sign = keyValues
                .optString(SharedConstants.CLIENT_SIGN_PARAM_NAME);
        if (StringUtils.isEmpty(sign)) {
            return new RESTResult(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED,
                    "sign is empty!");
        }
        try {
            String correctSign = getCorrectSign(keyValues, appName);
            LOG.info("check sign for '" + appName + "', correct sign is:"
                    + correctSign);
            return compareSign(sign, correctSign);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new RESTResult(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new RESTResult(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new RESTResult(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage());
        }
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
            return new RESTResult(HttpStatus.UNAUTHORIZED, "incorrect sign!");
        }
        return new RESTResult(HttpStatus.OK);
    }

    protected String getCorrectSign(JSONObject allKeyValues,
            String appName) throws UnsupportedEncodingException,
            IllegalArgumentException, NoSuchAlgorithmException {
        allKeyValues.remove("sign");
        if (!allKeyValues.containsKey(SharedConstants.TIMESTAMP_PARAM_NAME)) {
            LOG.warn("timestamp not in the request");
            return SharedConstants.ERROR_SIGN_TIMESTAMP_EMPTY;
        }
        String tsStr = allKeyValues.optString(SharedConstants.TIMESTAMP_PARAM_NAME);
        try {
            long ts = Long.parseLong(tsStr);
            if (SharedConstants.TIMESTAMP_TORLERRANCE < Math
                    .abs(System.currentTimeMillis() - ts)) {
                return SharedConstants.ERROR_SIGN_TIMESTAMP_EXPIRED;
            }
        } catch (Exception ex) {
            return SharedConstants.ERROR_SIGN_TIMESTAMP_EMPTY;
        }
        String appSecret = clientAppService.getClientAppSecret(appName);
        if (appSecret != null) {
            return generateSign(allKeyValues, appName, appSecret);
        }
        return SharedConstants.ERROR_SIGN_CLIENT_NOT_FOUND;
    }

    @SuppressWarnings("unchecked")
    public String generateSign(JSONObject keyValues,
            String appName, String secretKey)
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String paramStr = "";
        List<String> sortedKeys = new ArrayList<String>(keyValues.size());
        sortedKeys.addAll(keyValues.keySet());
        Collections.sort(sortedKeys);
        for (int i = 0; i < sortedKeys.size(); i++) {
            String key = sortedKeys.get(i);
            String value = keyValues.optString(key);
            if (null == key || "".equals(key) || null == value
                    || "".equals(value)) {
                continue;
            }
            paramStr += key + "=" + value;
        }
        String secretText = secretKey + "from=" + appName + paramStr;
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

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
