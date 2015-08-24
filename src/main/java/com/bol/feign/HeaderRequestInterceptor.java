package com.bol.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class HeaderRequestInterceptor implements RequestInterceptor {

    private final String key;
    private final String value;

    public HeaderRequestInterceptor(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header(key, value);
    }
}
