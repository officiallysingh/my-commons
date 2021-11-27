package com.clearcaptions.ccwsv3.common.boot.config.feign;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import feign.Client;
import feign.Request;
import feign.Response;

public class InterceptableFeignClient extends Client.Default {

	private final List<FeignInterceptor> feignInterceptors;
	
	public InterceptableFeignClient(final List<FeignInterceptor> feignInterceptors) {
		super(null, null);
		this.feignInterceptors = CollectionUtils.isEmpty(feignInterceptors) 
				? Collections.emptyList() : feignInterceptors;
	}
	
	@Override
    public Response execute(final Request request, final Request.Options options) throws IOException {
        Response response = super.execute(request, options);
        Response returnResponse = response;
        for(FeignInterceptor feignInterceptor: this.feignInterceptors) {
        	returnResponse = feignInterceptor.intercept(request, returnResponse);
        }
        return returnResponse;
    }

}
