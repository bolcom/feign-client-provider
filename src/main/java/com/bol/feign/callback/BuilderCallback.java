package com.bol.feign.callback;

import feign.Feign.Builder;

public interface BuilderCallback {

    public void configure(Builder builder);
}
