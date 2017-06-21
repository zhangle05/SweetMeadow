/**
 * 
 */
package com.sweetmeadow.api.bridge.service;

import java.util.Date;
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

    private static final int TIME_SPAN_UPPER = 3600 * 1000;

    @Autowired
    private TRmPayflowMapper payflowMapper;

    public List<TRmPayflow> listPayflows(Date from, Date to) {
        long now = System.currentTimeMillis();
        if (from == null) {
            from = new Date(now - TIME_SPAN_UPPER);
        }
        if (to == null || to.getTime() > now) {
            to = new Date(now);
        }
        if (to.getTime() - from.getTime() > TIME_SPAN_UPPER) {
            throw new IllegalArgumentException("Time span exceeds '" + TIME_SPAN_UPPER + "' miliseconds.");
        }
        TRmPayflowExample example = new TRmPayflowExample();
        TRmPayflowExample.Criteria c = example.createCriteria();
        c.andOperDateBetween(from, to);
        return payflowMapper.selectByExample(example);
    }

}
