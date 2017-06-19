/**
 * 
 */
package com.octopusdio.api.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangle
 *
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

    /**
     * 是否要求参数加密，false表示不需要
     * 
     * @return
     */
    boolean required() default true;

    /**
     * 安全等级
     * 
     * @return
     */
    int level() default 1;

}
