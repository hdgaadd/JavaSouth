package com.codeman.common.exception;


import com.codeman.common.constant.ResultCode;

/**
 * @author hdgaadd
 * created on 2021/12/08/19:43
 */
public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }

    public ApiException(ResultCode resultCode) {
        super(resultCode.getMessage());
    }
}
