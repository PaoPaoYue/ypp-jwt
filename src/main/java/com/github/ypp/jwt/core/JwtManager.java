package com.github.ypp.jwt.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.ypp.jwt.config.JwtProperties;
import com.github.ypp.jwt.JwtSubject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public class JwtManager {

    private final JwtProperties prop;

    private final JwtCache cache;

    public JwtManager(JwtProperties prop) {
        this.prop = prop;
        cache = new JwtCache(prop.getCache());
    }

    public String issue(JwtSubject subject) {
        Date expiration = new Date(System.currentTimeMillis() + prop.getMaxAlive() * 60 * 1000);
        Algorithm algorithm = Algorithm.HMAC256(prop.getSecret());
        String token = JWT.create()
                .withSubject(subject.getClass().getName())
                .withClaim(prop.getJsonKey(), subject.getClaims())
                .withExpiresAt(expiration)
                .sign(algorithm);
        if (cache.isEnable())
            cache.put(token, subject, expiration);
        return token;
    }

    public String refresh(String token) {
        if (verify(token) && new Date().getTime() > JWT.decode(token).getExpiresAt().getTime()) {
            JwtSubject subject = extract(token);
            if (subject != null) {
                if (cache.isEnable())
                    cache.remove(token);
                return issue(subject);
            }
        }
        return "";
    }

    public JwtSubject extract(String token) {
        if (token.isEmpty())
            return null;
        if (cache.isEnable()) {
            JwtCache.Record record = cache.get(token);
            if (record != null)
                return record.subject;
        }
        return extractFromToken(token);
    }

    public boolean verify(String token) {
        if (token.isEmpty())
            return false;
        if (cache.isEnable()) {
            JwtCache.Record record = cache.get(token);
            if (record != null) {
                if (new Date().getTime() <= record.expiration.getTime() + prop.getRefreshWindow() * 60 * 1000) {
                    return true;
                } else {
                    cache.remove(token);
                    return false;
                }
            }

        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(prop.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptExpiresAt(this.prop.getMaxIdle() * 60)
                    .build();
            verifier.verify(token);
            if (cache.isEnable())
                cache.put(token, extractFromToken(token), JWT.decode(token).getExpiresAt());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void setToken(HttpServletResponse response, String token) {
        response.setHeader("Access-Token", token);
    }

    public String getToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth!=null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return "";
    }

    private JwtSubject extractFromToken(String token) {
        JwtSubject subject;
        Map<String, Object> data;
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            Map<String, Claim> claims = decodedJWT.getClaims();
            String subjectName = claims.get("sub").asString();
            data = claims.get(prop.getJsonKey()).asMap();
            Class<?> clazz = Class.forName(subjectName);
            subject = (JwtSubject) clazz.newInstance();
        } catch (Exception e) {
            return null;
        }

        if (data != null)
            subject.setClaims(data);
        return subject;
    }

}
