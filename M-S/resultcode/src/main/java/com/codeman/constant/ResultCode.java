package com.codeman.constant;

/**
 * @author hdgaadd
 * Created on 2021/12/13
 */
public enum ResultCode {
    SUCCESS(200, "operate successful"),
    FAILED(500, "operation failure");
    private final long code;
    private final String message;

    ResultCode (long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
