package com.bol.feign.fake;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import dagger.Lazy;
import feign.Client;

public class FakeSSLClient {

    public static Client create() {
        Lazy<SSLSocketFactory> sslContextFactory = new Lazy<SSLSocketFactory>() {
            @Override
            public SSLSocketFactory get() {
                return new FakeSSLSocketFactory();
            }
        };

        Lazy<HostnameVerifier> hostnameVerifier = new Lazy<HostnameVerifier>() {

            @Override
            public HostnameVerifier get() {
                return new FakeHostnameVerifier();
            }
        };

        return new Client.Default(sslContextFactory, hostnameVerifier);
    }
}
