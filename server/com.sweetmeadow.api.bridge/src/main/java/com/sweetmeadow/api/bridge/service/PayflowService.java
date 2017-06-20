/**
 * 
 */
package com.sweetmeadow.api.bridge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.octopusdio.api.common.service.AbstractBaseService;
import com.sweetmeadow.api.bridge.domain.pojo.TRmPayflow;
import com.sweetmeadow.api.bridge.domain.pojo.TRmPayflowExample;
import com.sweetmeadow.api.sqlserver.dao.gen.TRmPayflowMapper;

/**
 * @author zhangle
 *
 */
@Service
public class PayflowService extends AbstractBaseService {

    @Autowired
    private TRmPayflowMapper payflowMapper;

    public long getPayflowCount() {
        TRmPayflowExample example = new TRmPayflowExample();
        return payflowMapper.countByExample(example);
    }

    public List<TRmPayflow> listPayflows(int page, int pageSize) {
        if (page < 1) {
            page = 1;
        }
        if (pageSize <= 0 || pageSize > 500) {
            pageSize = 20;
        }
        TRmPayflowExample example = new TRmPayflowExample();
        example.setOrderByClause("oper_date desc");
        example.setLimit(pageSize);
        example.setOffset((page - 1) * pageSize);
        return payflowMapper.selectByExample(example);
    }

}
