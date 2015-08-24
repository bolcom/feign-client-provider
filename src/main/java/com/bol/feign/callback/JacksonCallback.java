package com.bol.feign.callback;

import com.bol.feign.RequestInterceptorCreator;

import feign.Feign.Builder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSModule;

public class JacksonCallback implements BuilderCallback {
    @Override
    public void configure(Builder builder) {
        //@formatter:off
        builder.contract(new JAXRSModule.JAXRSContract())
        .decoder(new JacksonDecoder())
        .encoder(new JacksonEncoder())
        .requestInterceptor(RequestInterceptorCreator.json());
        //@formatter:on
    }
}
