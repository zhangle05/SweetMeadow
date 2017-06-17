/**
 * 
 */
package com.octopusdio.api.common.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zhangle
 *
 */
public abstract class AbstractBaseController {
    protected final Log LOG;

    public AbstractBaseController() {
        LOG = LogFactory.getLog(this.getClass());
    }

    protected String getFormDataInBody(String body, String name) {
        if (body == null || !body.contains("WebKitFormBoundary")
                || !body.contains("form-data")) {
            return getFormDataAsUrlParam(body, name);
        }
        String[] lineArr = body.split("\r\n");
        int valuePos = lineArr.length;
        for (int i = 0; i < lineArr.length; i++) {
            if (lineArr[i].contains("name=") && lineArr[i].contains(name)) {
                valuePos = i + 1;
                break;
            }
        }
        for (int i = valuePos; i < lineArr.length; i++) {
            if (StringUtils.isEmpty(lineArr[i])) {
                continue;
            }
            if (lineArr[i].contains("WebKitFormBoundary")) {
                return "";
            }
            return lineArr[i];
        }
        return "";
    }

    private String getFormDataAsUrlParam(String body, String name) {
        String[] paramArr = body.split("&");
        for (int i = 0; i < paramArr.length; i++) {
            String param = paramArr[i];
            if (param.contains(name)) {
                int pos = param.indexOf("=");
                return param.substring(pos + 1);
            }
        }
        return "";
    }

}
