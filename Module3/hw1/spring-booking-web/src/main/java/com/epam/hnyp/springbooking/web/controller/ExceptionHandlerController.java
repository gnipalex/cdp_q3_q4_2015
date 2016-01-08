package com.epam.hnyp.springbooking.web.controller;

import java.text.MessageFormat;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleCommonExceptions(Exception e) {
        return MessageFormat.format("{0} occured during request processing, error is : {1}", e.getClass(), e.getMessage());
    }
    
    @ExceptionHandler(value = DataAccessException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleJdbcTemplateException(DataAccessException e) {
        return MessageFormat.format("Error while perform database query, error is : {0}", e.getMessage());
    }
    
}
