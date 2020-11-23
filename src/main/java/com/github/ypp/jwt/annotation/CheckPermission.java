package com.github.ypp.jwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface CheckPermission {

    /**
     * @return 权限列表
     */
    String [] value() default {};

    /**
     * @return 是否满足一个权限即可通过
     */
    boolean any() default false;

}
