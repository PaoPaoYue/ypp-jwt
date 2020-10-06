package com.ypp.yppjwt.core;

import com.ypp.yppjwt.JwtSubject;
import com.ypp.yppjwt.annotation.CheckLogin;
import com.ypp.yppjwt.annotation.CheckPermission;
import com.ypp.yppjwt.exception.CheckLoginException;
import com.ypp.yppjwt.exception.CheckPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class CheckInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(CheckInterceptor.class);

    private final JwtManager manager;

    public CheckInterceptor(JwtManager manager) {
        this.manager = manager;
    }

    /**
     * 每次请求之前触发
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 获取处理method
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod method = (HandlerMethod) handler;

        // 验证登录
        if(method.hasMethodAnnotation(CheckLogin.class) || method.getBeanType().isAnnotationPresent(CheckLogin.class)) {

            //logger.info("new check login");

            String token = manager.getToken(request);

            if (!manager.verify(token))
                throw new CheckLoginException();

            // 获取权限注解
            CheckPermission cp = method.getMethodAnnotation(CheckPermission.class);
            if(cp == null) {
                cp = method.getBeanType().getAnnotation(CheckPermission.class);
            }
            if(cp != null) {

                List<String> permissions = Arrays.asList(cp.value());
                //logger.info("new check permission: " + permissions.toString());

                JwtSubject subject = manager.extract(token);
                if (subject == null)
                    throw new CheckPermissionException();

                // 开始验证权限
                if(cp.any()) {
                    if (!checkPermissionAny(subject, permissions))
                        throw new CheckPermissionException();
                } else {
                    if (!checkPermissionAll(subject, permissions))
                        throw new CheckPermissionException();
                }
            }
        }

        return true;
    }

    private boolean checkPermissionAny(JwtSubject subject, List<String> permissions) {
        return subject.getPermissions().stream().anyMatch(permissions::contains);
    }

    private boolean checkPermissionAll(JwtSubject subject, List<String> permissions) {
        return permissions.containsAll(subject.getPermissions());
    }
}
