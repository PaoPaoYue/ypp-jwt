package com.github.ypp.jwt.core;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoRefreshInterceptor implements HandlerInterceptor {

    private final JwtManager manager;

    public AutoRefreshInterceptor(JwtManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = manager.getToken(request);
        token = manager.refresh(token);
        if (!token.isEmpty())
            manager.setToken(response, token);
        return true;
    }
}
