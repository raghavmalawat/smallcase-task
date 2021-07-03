package com.smallcase.exception;

public class FatalCustomException extends Exception {

    private final Integer code;

    public FatalCustomException(Integer code) {
        super();
        this.code = code;
    }

    public FatalCustomException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public FatalCustomException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public FatalCustomException(Throwable cause, Integer code) {
        super(cause);
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}

