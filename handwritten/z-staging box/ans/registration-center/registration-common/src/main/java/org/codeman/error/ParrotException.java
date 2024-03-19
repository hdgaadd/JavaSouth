package org.codeman.error;

public class ParrotException extends RuntimeException {

    public ParrotException(String message) {
        super(message);
    }

    public ParrotException(Throwable cause) {
        super(cause);
    }

    public ParrotException(String message, Throwable cause) {
        super(message, cause);
    }

}
