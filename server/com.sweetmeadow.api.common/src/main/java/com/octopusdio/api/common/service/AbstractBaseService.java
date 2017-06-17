/**
 * 
 */
package com.octopusdio.api.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author zhangle
 *
 */
public abstract class AbstractBaseService {
    protected final Log LOG;

    public AbstractBaseService() {
        LOG = LogFactory.getLog(this.getClass());
    }

}
