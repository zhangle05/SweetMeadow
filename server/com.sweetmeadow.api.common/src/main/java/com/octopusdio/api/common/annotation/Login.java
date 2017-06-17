package com.octopusdio.api.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

    /**
     * 是否要求登录，false表示用户无需登录
     * 
     * @return
     */
    boolean required() default true;

    /**
     * 页面等级，只有当角色等级大于等于页面等级时才能访问页面
     * 
     * @return
     */
    int level() default 1;

}
