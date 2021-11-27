package com.clearcaptions.ccwsv3.common.boot.config.feign;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import feign.Client;
import feign.slf4j.Slf4jLogger;

@Configuration
@ConditionalOnClass(FeignClient.class)
public class FeignAutoConfiguration {

	@Configuration
	@ConditionalOnClass(Slf4jLogger.class)
	public class FeignLoggingConfiguration {

		@Bean
	    public Slf4jLogger.Level feignLoggerLevel(
	    		@Value("${application.feign-client.logging-level:NONE}")
	    		final FeignLoggingLevel feignLoggingLevel) {
			
	        switch (feignLoggingLevel) {
	        case BASIC:
	            return Slf4jLogger.Level.BASIC;
	        case HEADERS:
	            return Slf4jLogger.Level.HEADERS;
	        case FULL:
	            return Slf4jLogger.Level.FULL;
	        case NONE:
	            return Slf4jLogger.Level.NONE;

	        default:
	            return Slf4jLogger.Level.FULL;
	        }
	    }
	}
	
	static enum FeignLoggingLevel {

		FULL, BASIC, HEADERS, NONE;

	}
	
	@Configuration
	@ConditionalOnProperty(prefix = "application.feign-client", name = "intercept", havingValue = "true")
	@ConditionalOnClass(Client.class)
	public class FeignInterceptorConfiguration {
		
		@Bean
		public InterceptableFeignClient interceptableFeignClient(@Nullable final List<FeignInterceptor> feignInterceptors) {
			return new InterceptableFeignClient(feignInterceptors);
		}
	}
}
