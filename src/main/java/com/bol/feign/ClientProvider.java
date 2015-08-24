package com.bol.feign;

import com.bol.feign.callback.BuilderCallback;
import com.bol.feign.provider.StaticUrlProvider;
import com.bol.feign.provider.UrlProvider;

import feign.Feign;
import feign.Feign.Builder;

public class ClientProvider {

    private final BuilderCallback callback;
    private final UrlProvider urlProvider;

    public ClientProvider(String url) {
        this(url, new DefaultCallback());
    }

    public ClientProvider(String url, BuilderCallback callback) {
        this(new StaticUrlProvider(url), callback);
    }

    public ClientProvider(UrlProvider u, BuilderCallback callback) {
        this.callback = callback;
        this.urlProvider = u;
    }

    public <T> T create(Class<T> target) {
        Feign.Builder builder = Feign.builder();
        builder.errorDecoder(new FeignErrorDecoder());
        callback.configure(builder);
        return builder.target(target, urlProvider.get());
    }

    static final class DefaultCallback implements BuilderCallback {
        @Override
        public void configure(Builder builder) {

        }
    }

}
