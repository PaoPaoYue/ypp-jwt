package com.github.ypp.jwt.exception.handler;

import com.github.ypp.jwt.exception.CheckLoginException;
import com.github.ypp.jwt.IJwtExceptionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CheckLoginExceptionHandler {

    private IJwtExceptionService service;

    public CheckLoginExceptionHandler(IJwtExceptionService service) {
        this.service = service;
    }

    @ExceptionHandler(CheckLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object handleRestException(CheckLoginException e) {
        return service.getLoginExceptionResponse();
    }
}