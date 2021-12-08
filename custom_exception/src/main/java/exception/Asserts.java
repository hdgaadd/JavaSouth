package exception;

import api.IErrorCode;

/**
 * @author hdgaadd
 * Created on 2021/12/08/19:42
 */
public class Asserts {

    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode iErrorCode) {
        throw new ApiException(iErrorCode);
    }

}
