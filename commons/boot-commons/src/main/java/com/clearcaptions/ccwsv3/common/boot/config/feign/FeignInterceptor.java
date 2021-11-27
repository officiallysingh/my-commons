package com.clearcaptions.ccwsv3.common.boot.config.feign;

import java.io.IOException;

import feign.Request;
import feign.Response;

@FunctionalInterface
public interface FeignInterceptor {

	Response intercept(final Request request, final Response response) throws IOException;
}
