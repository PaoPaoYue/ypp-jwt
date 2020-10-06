package com.ypp.yppjwt.exception.handler;

import com.ypp.yppjwt.IJwtExceptionService;
import com.ypp.yppjwt.exception.CheckLoginException;
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