package com.github.ypp.jwt.core;

import com.github.ypp.jwt.config.JwtCacheProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.github.ypp.jwt.JwtSubject;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class JwtCache {

    public static class Record {
        JwtSubject subject;
        Date expiration;

        public Record(JwtSubject subject, Date expiration) {
            this.subject = subject;
            this.expiration = expiration;
        }

    }

    private final boolean enable;

    private Cache<String, Record> tokenCache = null;

    public JwtCache(JwtCacheProperties prop) {
        enable = prop.isEnable();
        if (enable)
            tokenCache = CacheBuilder.newBuilder().maximumSize(prop.getSize())
                    .expireAfterWrite(prop.getExpiration(), TimeUnit.MINUTES).build();
    }

    public boolean isEnable() {
        return enable;
    }

    public Record get(String token) {
        return tokenCache.getIfPresent(token);
    }

    public void remove(String token) {
        tokenCache.invalidate(token);
    }

    public void put(String token, JwtSubject subject, Date expiration) {
        tokenCache.put(token, new Record(subject, expiration));
    }
}

