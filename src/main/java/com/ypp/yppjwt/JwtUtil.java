package com.ypp.yppjwt;

import com.ypp.yppjwt.core.JwtManager;
import com.ypp.yppjwt.core.WebMvcUtil;


public class JwtUtil {

    private static JwtManager manager;

    static void setManager(JwtManager jwtManager) {
        manager = jwtManager;
    }

    public static String issue(JwtSubject subject) {
        String token = manager.issue(subject);
        manager.setToken(WebMvcUtil.getResponse(), token);
        return token;
    }

    public static JwtSubject extract() {
        String token = manager.getToken(WebMvcUtil.getRequest());
        return manager.extract(token);
    }

}
