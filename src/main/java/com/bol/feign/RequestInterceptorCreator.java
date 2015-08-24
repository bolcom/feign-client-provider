package com.bol.feign;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;

public class RequestInterceptorCreator {

    public static RequestInterceptor basic(String username, String password) {
        return new BasicAuthRequestInterceptor(username, password);
    }

    public static RequestInterceptor json() {
        return new HeaderRequestInterceptor("Content-Type", "application/json");
    }

}
