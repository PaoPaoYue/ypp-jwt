package com.github.ypp.jwt;

import java.util.List;
import java.util.Map;

/**
 * Your payload object in JWT should implement this interface
 * and is a standard Java bean with an empty constructor
 */
public interface JwtSubject {
    /**
     * @return The list of permissions of this user class
     */
    List<String> getPermissions();

    /**
     * @return All attributes of this object you want to put into the payload
     */
    Map<String, Object> getClaims();

    /**
     * @param claims All attributes of this object you just put into payload
     */
    void setClaims(Map<String, Object> claims);

}
