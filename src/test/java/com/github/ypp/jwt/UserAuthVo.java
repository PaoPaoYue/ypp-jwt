package com.github.ypp.jwt;

import java.util.*;

public class UserAuthVo implements JwtSubject {
    private int uid;
    private String username;

    public UserAuthVo() {

    }
    public UserAuthVo(int uid, String username){
        this.uid=uid;
        this.username=username;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UsersAuthVo{" +
                "uid=" + uid +
                ", username='" + username + "'" +
                "}";
    }

    @Override
    public List<String> getPermissions() {
        return Collections.singletonList("user");
    }

    @Override
    public void setClaims(Map<String, Object> claims) {
        uid = (int) claims.get("uid");
        username = (String) claims.get("username");
    }

    @Override
    public Map<String, Object> getClaims() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid",uid);
        map.put("username", username);
        return map;
    }
}
