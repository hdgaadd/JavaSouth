package com.codeman;

import com.codeman.util.CommonResult;

/**
 * @author hdgaadd
 * Created on 2022/05/02
 */
public class BaseController {

    public static <T>CommonResult setSuccessful(T data) {
        return CommonResult.success(data);
    }

    public static <T>CommonResult setFailed(T data) {
        return CommonResult.failed(data);
    }

}
