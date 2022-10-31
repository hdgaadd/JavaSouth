package org.codeman;

import org.codeman.util.CommonResult;

/**
 * @author hdgaadd
 * created on 2022/05/02
 */
public class BaseController {

    public static <T>CommonResult setSuccessful(T data) {
        return CommonResult.success(data);
    }

    public static <T>CommonResult setFailed(T data) {
        return CommonResult.failed(data);
    }

}
