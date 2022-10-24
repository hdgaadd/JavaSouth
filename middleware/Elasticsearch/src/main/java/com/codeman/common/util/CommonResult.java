package com.codeman.common.util;

import com.codeman.common.constant.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hdgaadd
 * created on 2021/12/13
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonResult<T>{

    private long code;

    private T data;

    private String message;

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), data, ResultCode.SUCCESS.getMessage());
    }

    public static <T> CommonResult<T> failed(T data) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), data, ResultCode.FAILED.getMessage());
    }

    public static <T> CommonResult<T> failed(T data, String message) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), data, message);
    }

    public static void main(String[] args) {
        System.out.println(CommonResult.success(666));
    }
}
