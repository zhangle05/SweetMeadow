/**
 * 
 */
package com.sweetmeadow.api.bridge.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.octopusdio.api.common.service.AbstractBaseService;
import com.sweetmeadow.api.bridge.dao.gen.AdminAuthMapper;
import com.sweetmeadow.api.bridge.dao.gen.AdminUserMapper;
import com.sweetmeadow.api.bridge.domain.pojo.AdminAuth;
import com.sweetmeadow.api.bridge.domain.pojo.AdminAuthExample;
import com.sweetmeadow.api.bridge.domain.pojo.AdminUser;
import com.sweetmeadow.api.bridge.domain.pojo.AdminUserExample;

/**
 * @author zhangle
 *
 */
@Service
public class AdminAuthService extends AbstractBaseService {

    private static final long AUTH_EXPIRE_MILS = 15 * 60 * 1000L;

    @Autowired
    private AdminUserMapper adminMapper;

    @Autowired
    private AdminAuthMapper authMapper;

    public AdminAuth getOrCreateBySessionId(String sessionId) {
        AdminAuthExample example = new AdminAuthExample();
        AdminAuthExample.Criteria c = example.createCriteria();
        c.andSessionIdEqualTo(sessionId);
        List<AdminAuth> list = authMapper.selectByExample(example);
        if (list.size() > 0) {
            AdminAuth auth = list.get(0);
            // update create time
            auth.setCreateTime(new java.util.Date());
            authMapper.updateByPrimaryKey(auth);
            return auth;
        }
        AdminAuth auth = new AdminAuth();
        auth.setSessionId(sessionId);
        auth.setCreateTime(new java.util.Date());
        authMapper.insert(auth);
        return auth;
    }

    public AdminAuth getById(Integer authId) {
        return authMapper.selectByPrimaryKey(authId);
    }

    public boolean doAuth(int authId, String openId) {
        AdminAuth auth = authMapper.selectByPrimaryKey(authId);
        if (StringUtils.isEmpty(openId)) {
            throw new IllegalStateException("用户ID为空!");
        }
        if (auth == null) {
            throw new IllegalStateException("无效的验证码!");
        }
        if (System.currentTimeMillis()
                - auth.getCreateTime().getTime() > AUTH_EXPIRE_MILS) {
            throw new IllegalStateException("验证码已过期!");
        }
        AdminUserExample example = new AdminUserExample();
        AdminUserExample.Criteria c = example.createCriteria();
        c.andOpenIdEqualTo(openId);
        List<AdminUser> list = adminMapper.selectByExample(example);
        if (list.size() <= 0) {
            throw new IllegalStateException("无效的用户ID");
        }

        authMapper.deleteByPrimaryKey(authId);
        return true;
    }

    public AdminUser getAdminBySessionId(String sessionId) throws AuthenticationException {
//        String userId = cacheService.getValue(sessionId);
        String userId = "";
        if (StringUtils.isEmpty(userId)) {
            throw new AuthenticationException("未登录");
        }
        AdminUserExample example = new AdminUserExample();
        AdminUserExample.Criteria c = example.createCriteria();
        c.andOpenIdEqualTo(userId);
        List<AdminUser> list = adminMapper.selectByExample(example);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            throw new AuthenticationException("找不到登录用户");
        }
    }

    public void logoutAdmin(String sessionId) {
//        cacheService.deleteValue(sessionId);
    }
}
