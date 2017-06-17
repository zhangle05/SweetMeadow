/**
 * 
 */
package com.sweetmeadow.api.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.octopusdio.api.common.controller.AbstractBaseController;
import com.octopusdio.api.common.domain.RESTResult;

/**
 * @author zhangle
 *
 */
@ControllerAdvice
@RestController
@RequestMapping("/")
public class DefaultExceptionHandler extends AbstractBaseController {

    @RequestMapping("/resource_not_found")
    public ResponseEntity<RESTResult> handle404Error() {
        LOG.debug("handle 404 error");
        RESTResult result = new RESTResult("找不到URL");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<RESTResult> handleAnyException(Throwable ex) {
        LOG.debug("handle throwable:" + ex);
        ex.printStackTrace();
        RESTResult result = new RESTResult("内部错误:" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(result);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RESTResult> handleArgumentException(
            MethodArgumentTypeMismatchException ex) {
        LOG.debug("handle argument exception:" + ex);
        ex.printStackTrace();
        RESTResult result = new RESTResult("参数类型错误:" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<RESTResult> handleArgumentException(
            MissingServletRequestParameterException ex) {
        LOG.debug("handle argument exception:" + ex);
        ex.printStackTrace();
        RESTResult result = new RESTResult("缺少参数:" + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
