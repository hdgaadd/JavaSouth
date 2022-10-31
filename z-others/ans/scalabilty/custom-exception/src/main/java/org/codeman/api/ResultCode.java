package org.codeman.api;

/**
 * @author hdgaadd
 * created on 2021/12/08/19:45
 */
public enum ResultCode implements IErrorCode{
    SUCCESS(200, "successful"),
    FAILED(500,"fail");

    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
