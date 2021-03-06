package com.github.ypp.jwt;

import org.springframework.stereotype.Service;

@Service
public class JwtExceptionServiceImpl implements IJwtExceptionService {

    @Override
    public Object getLoginExceptionResponse() {
        return "not login";
    }

    @Override
    public Object getPermissionExceptionResponse() {
        return "no permission";
    }
}
