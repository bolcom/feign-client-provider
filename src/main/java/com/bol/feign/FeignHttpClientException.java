package com.bol.feign;

public class FeignHttpClientException extends FeignHttpException {
    public FeignHttpClientException(int status, String message, Throwable cause) {
        super(status, message, cause);
    }

    public FeignHttpClientException(int status, String message) {
        super(status, message);
    }
}
