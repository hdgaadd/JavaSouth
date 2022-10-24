package org.codeman.exception;

import org.codeman.api.IErrorCode;

/**
 * @author hdgaadd
 * created on 2021/12/08/19:42
 */
public class Asserts {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode iErrorCode) {
        throw new ApiException(iErrorCode);
    }

}
