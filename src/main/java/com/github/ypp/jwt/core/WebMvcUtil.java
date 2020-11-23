package com.github.ypp.jwt.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class WebMvcUtil {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();// 大善人SpringMVC提供的封装
        if(servletRequestAttributes == null) {
            throw new RuntimeException("当前环境非JavaWeb");
        }
        return servletRequestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();// 大善人SpringMVC提供的封装
        if(servletRequestAttributes == null) {
            throw new RuntimeException("当前环境非JavaWeb");
        }
        return servletRequestAttributes.getResponse();
    }




}
