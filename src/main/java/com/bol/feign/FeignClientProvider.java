package com.bol.feign;

import com.bol.feign.fake.FakeHostnameVerifier;
import com.bol.feign.fake.FakeSSLSocketFactory;
import com.bol.feign.internal.FeignErrorDecoder;
import com.bol.feign.internal.JsonApplicationMediaTypeInterceptor;
import dagger.Lazy;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSModule;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.util.ArrayList;
import java.util.List;

public class FeignClientProvider {
    private String username;
    private String password;
    private boolean requiresAuthentication = false;
    private final String url;

    private Client client;
    private Decoder decoder;
    private Encoder encoder;
    private Logger logger;
    private Logger.Level logLevel = Logger.Level.BASIC;
    private List<RequestInterceptor> requestInterceptors = new ArrayList<RequestInterceptor>();

    public static <T> T create(Class<T> target, String url) {
        return new FeignClientProvider(url).createClient(target);
    }

    public static <T> T create(Class<T> target, String url, String username, String password) {
        return new FeignClientProvider(url).authenticated(username, password).createClient(target);
    }

    public static <T> T create(Class<T> target, Client client, String url, String username, String password) {
        return new FeignClientProvider(url).withClient(client).authenticated(username, password).createClient(target);
    }

    public static <T> T createFakeSSL(Class<T> target, String url, String username, String password) {
        return new FeignClientProvider(url).authenticated(username, password).withFakeSSL().createClient(target);
    }

    public FeignClientProvider(final String url) {
        if (url == null) {
            throw new IllegalArgumentException("url must be provided");
        }
        this.url = url.replaceAll("/\\z", "");
    }

    public FeignClientProvider withClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("client must be provided");
        }
        this.client = client;
        return this;
    }

    public FeignClientProvider withFakeSSL() {
        Lazy<SSLSocketFactory> sslContextFactory = new Lazy<SSLSocketFactory>() {

            private FakeSSLSocketFactory sslSocketFactory = new FakeSSLSocketFactory();

            @Override
            public SSLSocketFactory get() {
                return sslSocketFactory;
            }
        };

        Lazy<HostnameVerifier> hostnameVerifier = new Lazy<HostnameVerifier>() {

            private FakeHostnameVerifier fakeHostnameVerifier = new FakeHostnameVerifier();

            @Override
            public HostnameVerifier get() {
                return fakeHostnameVerifier;
            }
        };

        client = new Client.Default(sslContextFactory, hostnameVerifier);
        return this;
    }

    public FeignClientProvider withLogger(Logger logger) {
        return withLogger(logger, logLevel);
    }

    public FeignClientProvider addRequestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptors.add(requestInterceptor);
        return this;
    }

    public FeignClientProvider withLogger(Logger logger, Logger.Level level) {
        if (logger == null) {
            throw new IllegalArgumentException("logger must be provided");
        }
        if (level == null) {
            throw new IllegalArgumentException("log level must be provided");
        }
        this.logger = logger;
        this.logLevel = level;
        return this;
    }

    public FeignClientProvider withEncoder(Encoder encoder) {
        this.encoder = encoder;
        return this;
    }

    public FeignClientProvider withDecoder(Decoder decoder) {
        this.decoder = decoder;
        return this;
    }

    public FeignClientProvider authenticated(final String username, final String password) {
        if (username == null) {
            throw new IllegalArgumentException("username must be provided");
        }
        if (password == null) {
            throw new IllegalArgumentException("password level must be provided");
        }
        this.username = username;
        this.password = password;
        this.requiresAuthentication = true;
        return this;
    }

    public <T> T createClient(Class<T> target) {
        Feign.Builder builder = Feign.builder();
        if (client != null) {
            builder.client(client);
        }
        Decoder decoder = this.decoder;
        if (decoder==null) {
            decoder = new JacksonDecoder();
        }
        Encoder encoder = this.encoder;
        if (encoder == null) {
            encoder = new JacksonEncoder();
        }

        builder.contract(new JAXRSModule.JAXRSContract())
                .decoder(decoder)
                .encoder(encoder)
                .errorDecoder(new FeignErrorDecoder())
                .requestInterceptors(createRequestInterceptors());

        if (logger != null) {
            builder.logger(logger)
                    .logLevel(logLevel);
        }
        return builder.target(target, url);
    }

    private Iterable<RequestInterceptor> createRequestInterceptors() {
        requestInterceptors.add(new JsonApplicationMediaTypeInterceptor());
        if (requiresAuthentication) {
            requestInterceptors.add(new BasicAuthRequestInterceptor(username, password));
        }
        return requestInterceptors;
    }
}
