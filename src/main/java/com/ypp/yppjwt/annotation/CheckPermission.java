package com.ypp.yppjwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface CheckPermission {

    /**
     * 权限码数组 ，String类型
     * @return .
     */
    String [] value() default {};

    /**
     * false=必须全部具有，true=只要具有一个就可以通过
     * @return .
     */
    boolean any() default false;

}
