package com.bol.feign.provider;

public class StaticUrlProvider implements UrlProvider{
    private final String url;

    public StaticUrlProvider(String url) {
        this.url = url;
    }

    @Override
    public String get() {
        return url;
    }
}
