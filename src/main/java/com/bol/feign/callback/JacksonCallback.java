package com.bol.feign.callback;

import com.bol.feign.RequestInterceptorCreator;

import feign.Feign.Builder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;

public class JacksonCallback implements BuilderCallback {
    @Override
    public final void configure(Builder builder) {
        //@formatter:off
        builder.contract(new JAXRSContract())
        .decoder(new JacksonDecoder())
        .encoder(new JacksonEncoder())
        .requestInterceptor(RequestInterceptorCreator.json());
        postConfigure(builder);;
        //@formatter:on
    }

    public void postConfigure(Builder builder) {
        // Nothing to see here, can be overridden
    }
}
