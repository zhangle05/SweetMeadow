/**
 * 
 */
package com.sweetmeadow.api.bridge.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.octopusdio.api.common.annotation.Secured;
import com.octopusdio.api.common.controller.AbstractBaseController;
import com.octopusdio.api.common.domain.ListResult;
import com.octopusdio.api.common.domain.RESTResult;
import com.sweetmeadow.api.bridge.domain.pojo.TRmPayflow;
import com.sweetmeadow.api.bridge.service.PayflowService;

/**
 * @author zhangle
 *
 */
@Secured
@RestController
@RequestMapping("/bridge/payflow")
public class PayflowController extends AbstractBaseController {

    @Autowired
    private PayflowService payflowSvc;

    @RequestMapping("/list")
    public ResponseEntity<RESTResult> list(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to) {
        try {
            Date fromDate = null;
            Date toDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (!StringUtils.isEmpty(from)) {
                fromDate = sdf.parse(from);
            }
            if (!StringUtils.isEmpty(to)) {
                toDate = sdf.parse(to);
            }
            LOG.info("list payflow, from date:" + fromDate + ", to date:" + toDate);
            List<TRmPayflow> payflows = payflowSvc.listPayflows(fromDate, toDate);
            ListResult<TRmPayflow> list = new ListResult<TRmPayflow>(
                    payflows.size(), payflows);
            RESTResult result = new RESTResult(list);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalArgumentException ex) {
            LOG.error("list pay flow error: " + ex.getMessage());
            RESTResult result = new RESTResult(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            RESTResult result = new RESTResult(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

}
