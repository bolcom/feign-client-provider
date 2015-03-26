package com.bol.feign

import spock.lang.Specification
import spock.lang.Unroll

class ExceptionsSpec extends Specification {

    @Unroll
    def '[#exception.simpleName] requires the http status and message and extends from [#exceptionType.simpleName]'() {
        def httpStatusCode = 200
        def message = 'descriptive message'

        expect:
        def e = exception.newInstance(httpStatusCode, message)
        exceptionType.isInstance(e)

        where:
        exception                || exceptionType
        FeignHttpException       || RuntimeException
        FeignHttpClientException || FeignHttpException
        FeignHttpServerException || FeignHttpException
    }

    @Unroll
    def '[#exception.simpleName] requires the http status, message and cause, and extends from [#exceptionType.simpleName]'() {
        def httpStatusCode = 200
        def message = 'descriptive message'
        def cause = new Throwable()

        expect:
        def e = exception.newInstance(httpStatusCode, message, cause)
        exceptionType.isInstance(e)

        where:
        exception                || exceptionType
        FeignHttpException       || RuntimeException
        FeignHttpClientException || FeignHttpException
        FeignHttpServerException || FeignHttpException
    }
}
