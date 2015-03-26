package com.bol.feign.internal

import com.bol.feign.FeignHttpClientException
import com.bol.feign.FeignHttpServerException
import feign.FeignException
import feign.Response
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.charset.Charset

class FeignErrorDecoderSpec extends Specification {
    private FeignErrorDecoder decoder = new FeignErrorDecoder();

    @Unroll
    def 'Client exception is throw on status [#httpStatus] response'() {
        String methodKey = null
        Response response = getResponseWithStatus(httpStatus)

        when:
        Exception exception = decoder.decode(methodKey, response)

        then:
        exception instanceof FeignHttpClientException
        FeignHttpClientException ex = (FeignHttpClientException) exception
        httpStatus == ex.getStatus()

        where:
        httpStatus << (400..499)
    }

    @Unroll
    def 'Server exception is throw on status [#httpStatus] response'() {
        String methodKey = null
        Response response = getResponseWithStatus(httpStatus)

        when:
        Exception exception = decoder.decode(methodKey, response)

        then:
        exception instanceof FeignHttpServerException
        FeignHttpServerException ex = (FeignHttpServerException) exception
        httpStatus == ex.getStatus()

        where:
        httpStatus << (500..599)
    }

    @Unroll
    def 'Generic Feign exception is throw on status [#httpStatus] response'() {
        String methodKey = null
        Response response = getResponseWithStatus(httpStatus)

        when:
        Exception exception = decoder.decode(methodKey, response)

        then:
        exception instanceof FeignException

        where:
        httpStatus << createGenericHttpStatusCodesList()
    }

    private Collection createGenericHttpStatusCodesList() {
        def statusses = new ArrayList(200..600)
        statusses.removeAll(400..499)
        statusses.removeAll(500..599)
        return statusses
    }

    private Response getResponseWithStatus(int httpStatus) {
        Map<String, Collection<String>> headers = new LinkedHashMap<>()
        String bodyText = null
        Charset charset = null
        return Response.create(
                httpStatus,
                "Some error description", headers, bodyText, charset
        )
    }
}
