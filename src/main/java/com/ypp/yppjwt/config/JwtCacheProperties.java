package com.ypp.yppjwt.config;

public class JwtCacheProperties {
    /**
     * 是否启用token的缓存验证机制
     */
    private boolean enable = true;
    /**
     * token缓存的最大容量
     */
    private int size = 1000;
    /**
     * token缓存记录过期时间，以分钟为单位
     */
    private int expiration = 30;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "JwtCacheProperties{" +
                "enable=" + enable +
                ", size=" + size +
                ", Expiration=" + expiration +
                '}';
    }
}
