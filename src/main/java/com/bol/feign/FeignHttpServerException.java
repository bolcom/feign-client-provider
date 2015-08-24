package com.bol.feign;

public class FeignHttpServerException extends FeignHttpException {

    private static final long serialVersionUID = 6528603942752352262L;

    public FeignHttpServerException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    public FeignHttpServerException(int status, String message) {
        super(status, message);
    }
}
