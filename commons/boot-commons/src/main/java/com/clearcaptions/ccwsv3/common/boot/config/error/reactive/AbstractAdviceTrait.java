package com.clearcaptions.ccwsv3.common.boot.config.error.reactive;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.webflux.advice.AdviceTrait;

import reactor.core.publisher.Mono;

/**
 * @author Rajveer Singh
 */
public abstract class AbstractAdviceTrait implements AdviceTrait {

	/**
	 * Post-process Problem payload to add the message key for front-end if needed,
	 * currently doing nothing
	 */
	@Override
	public Mono<ResponseEntity<Problem>> process(@Nullable ResponseEntity<Problem> entity,
			final ServerWebExchange request) {
		if (entity == null || entity.getBody() == null) {
			return Mono.empty();
		}
		else {
			return Mono.just(entity);
		}
	}

}
