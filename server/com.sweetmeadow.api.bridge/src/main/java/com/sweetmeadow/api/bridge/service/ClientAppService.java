/**
 * 
 */
package com.sweetmeadow.api.bridge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.octopusdio.api.common.service.AbstractBaseService;
import com.sweetmeadow.api.bridge.dao.gen.ClientAppMapper;
import com.sweetmeadow.api.bridge.domain.pojo.ClientApp;
import com.sweetmeadow.api.bridge.domain.pojo.ClientAppExample;

/**
 * @author zhangle
 *
 */
@Service
public class ClientAppService extends AbstractBaseService {

    @Autowired
    private ClientAppMapper clientAppMapper;

    public String getClientAppSecret(String clientName) {
        ClientAppExample example = new ClientAppExample();
        ClientAppExample.Criteria c = example.createCriteria();
        c.andNameEqualTo(clientName);
        List<ClientApp> list = clientAppMapper.selectByExample(example);
        if (list.size() > 0) {
            return list.get(0).getSecret();
        }
        return "";
    }
}
