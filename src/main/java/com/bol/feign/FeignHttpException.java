package com.bol.feign;

public class FeignHttpException extends RuntimeException {
    private int status;

    public FeignHttpException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public FeignHttpException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
