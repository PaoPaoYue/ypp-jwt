package com.ypp.yppjwt;

import java.util.List;
import java.util.Map;

public interface JwtSubject {
    List<String> getPermissions();
    void setClaims(Map<String, Object> claims);
    Map<String, Object> getClaims();
}
