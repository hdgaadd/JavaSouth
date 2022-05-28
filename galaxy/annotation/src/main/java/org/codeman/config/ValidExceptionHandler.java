package org.codeman.config;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author hdgaadd
 * Created on 2022/05/28
 *
 * 设置将@Valid，所造成的异常进行返回
 */
@RestControllerAdvice
public class ValidExceptionHandler {

    @ExceptionHandler(BindException.class)
    public String validExceptionHandler(BindException bindException) {
        return bindException.getAllErrors().get(0).getDefaultMessage();
    }
}
