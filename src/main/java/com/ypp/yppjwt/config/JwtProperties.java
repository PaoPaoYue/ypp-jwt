package com.ypp.yppjwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "jwt-ypp")
public class JwtProperties {

    /**
     * 加密accessToken所使用的密钥
     */
    private String secret = "1234";

    /**
     * accessToken中存放subject数据的map名称
     */
    private String jsonKey = "data";

    /**
     * accessToken的理论过期时间，单位分钟，token如果超过该时间则接口响应的header中附带新的token信息
     */
    private int maxAlive = 30;

    /**
     * accessToken的最大生存周期，单位分钟，在此时间内的token无需重新登录即可刷新
     */
    private int maxIdle = 60;

    /**
     * 是否启用token的自动刷新机制
     */
    private boolean autoRefresh = true;

    @NestedConfigurationProperty
    private JwtCacheProperties cache = new JwtCacheProperties();

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public void setJsonKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    public int getMaxAlive() {
        return maxAlive;
    }

    public void setMaxAlive(int maxAlive) {
        this.maxAlive = maxAlive;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public JwtCacheProperties getCache() {
        return cache;
    }

    public void setCache(JwtCacheProperties cache) {
        this.cache = cache;
    }

    public int getRefreshWindow() { return maxIdle - maxAlive;}

    @Override
    public String toString() {
        return "JwtProperties{" +
                "jsonKeyName='" + jsonKey + '\'' +
                ", maxAliveMinute=" + maxAlive +
                ", maxIdleMinute=" + maxIdle +
                ", enableAutoRefresh=" + autoRefresh +
                ", cache=" + cache +
                '}';
    }
}
