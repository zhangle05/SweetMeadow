/**
 * 
 */
package com.octopusdio.api.common.domain;

/**
 * @author zhangle
 *
 */
public class RESTResult {

    private Object data;
    private String msg;

    public RESTResult(Object data) {
        this.data = data;
    }

    public RESTResult(Object data, String msg) {
        this.data = data;
        this.msg = msg;
    }

    public RESTResult(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
