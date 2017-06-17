/**
 * 
 */
package com.sweetmeadow.api.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.octopusdio.api.common.annotation.Login;
import com.sweetmeadow.api.bridge.utils.SharedConstants;

import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private static Log LOG = LogFactory.getLog(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        LOG.info("AuthenticationInterceptor: Pre-handle, request URI is:"
                + request.getRequestURI());
        if (handler instanceof HandlerMethod) {
            HandlerMethod hmethod = (HandlerMethod) handler;
            if (!checkLogin(hmethod, request, response)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLogin(HandlerMethod hmethod,
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
}
