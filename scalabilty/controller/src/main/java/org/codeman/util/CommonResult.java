package org.codeman.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codeman.constant.ResultCode;

/**
 * @author hdgaadd
 * created on 2021/12/13
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonResult<T> {

    private long code;

    private T data;

    private String message;

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(), data, ResultCode.SUCCESS.getMessage());
    }

    public static <T> CommonResult<T> failed(T data) {
        return new CommonResult<T>(ResultCode.FAILED.getCode(), data, ResultCode.FAILED.getMessage());
    }
}
