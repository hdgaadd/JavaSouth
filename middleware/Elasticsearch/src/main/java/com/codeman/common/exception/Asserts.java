package com.codeman.common.exception;

import com.codeman.common.constant.ResultCode;

/**
 * @author hdgaadd
 * created on 2021/12/08/19:42
 */
public class Asserts {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(ResultCode resultCode) {
        throw new ApiException(resultCode);
    }

}
