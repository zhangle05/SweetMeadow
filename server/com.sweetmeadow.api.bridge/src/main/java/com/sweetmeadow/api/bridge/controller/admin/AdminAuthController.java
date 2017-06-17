/**
 * 
 */
package com.sweetmeadow.api.bridge.controller.admin;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.octopusdio.api.common.domain.RESTResult;
import com.octopusdio.api.common.service.AbstractBaseService;
import com.octopusdio.api.common.service.QrCodeService;
import com.sweetmeadow.api.bridge.domain.pojo.AdminAuth;
import com.sweetmeadow.api.bridge.domain.pojo.AdminUser;
import com.sweetmeadow.api.bridge.service.AdminAuthService;
import com.sweetmeadow.api.bridge.utils.SharedConstants;

import net.sf.json.JSONObject;

/**
 * @author zhangle
 *
 */
@RestController
@RequestMapping("/bridge/admin/auth")
public class AdminAuthController extends AbstractBaseService {

    private static final String ADMIN_AUTH_PATH = "/admin/auth";

    @Autowired
    private QrCodeService qrService;

    @Autowired
    private AdminAuthService authService;

    @RequestMapping("/check")
    public ResponseEntity<RESTResult> adminAuthCheck(HttpServletRequest request,
            HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        LOG.debug("check admin auth for session:" + sessionId);
        try {
            AdminUser admin = authService.getAdminBySessionId(sessionId);
            // 脱敏处理，不能将用户open_id暴露到前端
            JSONObject json = new JSONObject();
            json.put("nick", admin.getNick());
            json.put("id", admin.getId());
            RESTResult result = new RESTResult(json);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (AuthenticationException ex) {
            LOG.error("check admin auth failed:" + ex.getMessage());
            RESTResult result = new RESTResult("验证失败：" + ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            RESTResult result = new RESTResult(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

    @RequestMapping("/qr-code")
    public void getAuthQrCode(HttpServletRequest request,
            HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        LOG.debug("getting auth qr code for session:" + sessionId);
        try {
            AdminAuth auth = authService.getOrCreateBySessionId(sessionId);
            String url = getAdminAuthUrl(request, auth.getId());
            qrService.generateQrCode(url, response.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping("/logout")
    public ResponseEntity<RESTResult> adminLogout(HttpServletRequest request,
            HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        LOG.debug("logout admin user for session:" + sessionId);
        try {
            authService.logoutAdmin(sessionId);
            RESTResult result = new RESTResult("ok");
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            RESTResult result = new RESTResult(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

    private String getAdminAuthUrl(HttpServletRequest request, int authId) {
        String prefix = "bridge";
        Object prefixObj = request.getAttribute(SharedConstants.URL_PREFIX_KEY);
        if (prefixObj != null) {
            prefix = prefixObj.toString();
        }
        if (StringUtils.isEmpty(prefix)) {
            prefix = "bridge";
        }
        String scheme = request.getScheme();
        String portStr = "";
        String referrer = request.getHeader("referer");
        if (referrer != null && referrer.contains("https")) {
            // revise scheme by referrer because the scheme may
            // be changed by Nginx forwarding
            scheme = "https";
        }
        if (request.getServerPort() != 80) {
            portStr = ":" + request.getServerPort();
        }
        LOG.info("getting admin auth url, scheme is:" + scheme);
        String url = scheme + "://" + request.getServerName() + portStr + "/"
                + prefix + ADMIN_AUTH_PATH + "?auth_id=" + authId;
        return url;
    }

}
