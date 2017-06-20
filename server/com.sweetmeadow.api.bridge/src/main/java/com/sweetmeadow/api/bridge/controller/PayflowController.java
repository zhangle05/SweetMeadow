/**
 * 
 */
package com.sweetmeadow.api.bridge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.octopusdio.api.common.annotation.Secured;
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
public class PayflowController {

    @Autowired
    private PayflowService payflowSvc;

    @RequestMapping("/list")
    public ResponseEntity<RESTResult> list(
            @RequestParam(value = "p", required = false) Integer page,
            @RequestParam(value = "ps", required = false) Integer pageSize) {
        try {
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 10;
            }
            long count = payflowSvc.getPayflowCount();
            List<TRmPayflow> payflows = payflowSvc.listPayflows(page, pageSize);
            ListResult<TRmPayflow> list = new ListResult<TRmPayflow>(
                    count, payflows);
            RESTResult result = new RESTResult(list);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            RESTResult result = new RESTResult(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(result);
        }
    }

}
