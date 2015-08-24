package com.bol.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import static feign.FeignException.errorStatus;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            return new FeignHttpClientException(
                    response.status(),
                    response.reason()
            );
        }
        if (response.status() >= 500 && response.status() <= 599) {
            return new FeignHttpServerException(
                    response.status(),
                    response.reason()
            );
        }
        return errorStatus(methodKey, response);
    }
}
