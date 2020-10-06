package com.ypp.yppjwt;

import com.ypp.yppjwt.core.JwtManager;
import com.ypp.yppjwt.core.WebMvcUtil;


/**
 * A helper class for you to issue and decrypt JWT token
 */
public class JwtUtil {

    private static JwtManager manager;

    static void setManager(JwtManager jwtManager) {
        manager = jwtManager;
    }

    /**
     * Issue a new token and put token into "Assess-token" HTTP header
     * @param subject The payload object
     * @return The JWT token
     */
    public static String issue(JwtSubject subject) {
        String token = manager.issue(subject);
        manager.setToken(WebMvcUtil.getResponse(), token);
        return token;
    }

    /**
     * Decrypt the payload from the Authorization Bearer token
     * @return The payload object
     */
    public static JwtSubject extract() {
        String token = manager.getToken(WebMvcUtil.getRequest());
        return manager.extract(token);
    }

}
