package constant;

// 成败信息类型2
public enum ResultCode {
    SUCCESS(1, "successful"),
    FAIL(0, "fail");

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
