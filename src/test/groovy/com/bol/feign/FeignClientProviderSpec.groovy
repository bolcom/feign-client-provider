package com.bol.feign

import feign.Client
import feign.Logger
import spock.lang.Specification

class FeignClientProviderSpec extends Specification {

    def "Can instantiate the provider directly"() {
        def client = new FeignClientProvider('url')

        expect:
        client
    }

    def "Can create a client"() {
        def client = new FeignClientProvider('url')

        expect:
        client.createClient(TestApi) instanceof TestApi
    }

    def "Can create a client with authentication credentials"() {
        def client = new FeignClientProvider('url')
        client.authenticated('user', 'password')

        expect:
        client.createClient(TestApi) instanceof TestApi
    }

    def "Can create a client with fake SSL support"() {
        def client = new FeignClientProvider('url')
        client.withFakeSSL()

        expect:
        client.createClient(TestApi) instanceof TestApi
    }

    def "Can create a client with a Logger"() {
        def client = new FeignClientProvider('url')
        client.withLogger(new Logger.JavaLogger())

        expect:
        client.createClient(TestApi) instanceof TestApi
    }

    def "Can create a client with a Logger and specify log level"() {
        def client = new FeignClientProvider('url')
        client.withLogger(new Logger.JavaLogger(), Logger.Level.FULL)

        expect:
        client.createClient(TestApi) instanceof TestApi
    }

    def "Can create a client with a custom Client"() {
        def client = new FeignClientProvider('url')
        client.withClient(new Client.Default(null, null))

        expect:
        client.createClient(TestApi) instanceof TestApi
    }
}
