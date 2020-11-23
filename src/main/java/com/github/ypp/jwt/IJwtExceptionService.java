package com.github.ypp.jwt;


/**
 * Implement this interface in your project to define the response json object
 * In the case of CheckLogin or CheckPermission exception
 */
public interface IJwtExceptionService {
    /**
     * @return the json object in response of CheckLogin exception
     */
    Object getLoginExceptionResponse();

    /**
     * @return the json object in response of CheckPermission exception
     */
    Object getPermissionExceptionResponse();
}
