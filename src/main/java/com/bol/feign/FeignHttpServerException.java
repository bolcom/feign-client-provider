package com.bol.feign;

public class FeignHttpServerException extends FeignHttpException {
    public FeignHttpServerException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    public FeignHttpServerException(int status, String message) {
        super(status, message);
    }
}
