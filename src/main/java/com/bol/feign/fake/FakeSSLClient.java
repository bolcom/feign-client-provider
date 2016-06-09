package com.bol.feign.fake;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import feign.Client;

public class FakeSSLClient {

    public static Client create() {


        return new Client.Default(new FakeSSLSocketFactory(),  new FakeHostnameVerifier());
    }
}
