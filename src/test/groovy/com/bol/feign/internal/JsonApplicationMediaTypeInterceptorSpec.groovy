package com.bol.feign.internal

import feign.RequestTemplate
import spock.lang.Specification

class JsonApplicationMediaTypeInterceptorSpec extends Specification {

    def 'Sets the application/json content-type header'() {
        def interceptor = new JsonApplicationMediaTypeInterceptor()
        def template = new RequestTemplate()

        when:
        interceptor.apply(template)

        then:
        template.headers() == [
                'Content-Type': ['application/json']
        ]
    }
}
