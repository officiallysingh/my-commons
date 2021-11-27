package com.clearcaptions.ccwsv3.common.boot.config.error.reactive;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.spring.webflux.advice.network.CircuitBreakerOpenAdviceTrait;

import com.clearcaptions.ccwsv3.common.boot.config.error.ProblemProperties;

import net.jodah.failsafe.CircuitBreakerOpenException;
import reactor.core.publisher.Mono;

/**
 * @author Rajveer Singh
 */
@Configuration
@ConditionalOnClass(value = { ProblemHandling.class })
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ReactiveCircuitBreakerExceptionHandler extends AbstractAdviceTrait implements CircuitBreakerOpenAdviceTrait {

	// private ProblemHelper problemHelper;
	//
	// public CircuitBreakerExceptionHandler(ProblemHelper problemHelper) {
	// this.problemHelper = problemHelper;
	// }

	@Override
	public Mono<ResponseEntity<Problem>> handleCircuitBreakerOpen(CircuitBreakerOpenException exception,
			ServerWebExchange request) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Auto-generated method stub");
	}

}
