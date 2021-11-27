package com.clearcaptions.ccwsv3.common.boot.config.error.reactive;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.APPLICATION_EXCEPTION_HANDLER_BEAN_NAME;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.webflux.advice.AdviceTrait;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;

import com.clearcaptions.ccwsv3.common.boot.config.error.ProblemProperties;
import com.clearcaptions.ccwsv3.common.error.ApplicationException;
import com.clearcaptions.ccwsv3.common.error.ApplicationProblem;

import reactor.core.publisher.Mono;

/**
 * @author Rajveer Singh
 */
@Configuration(value = APPLICATION_EXCEPTION_HANDLER_BEAN_NAME)
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConditionalOnClass(ProblemHandling.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnMissingBean(name = APPLICATION_EXCEPTION_HANDLER_BEAN_NAME)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ReactiveApplicationExceptionHandler extends AbstractAdviceTrait implements AdviceTrait {

	private ReactiveProblemHelper problemHelper;

	public ReactiveApplicationExceptionHandler(ReactiveProblemHelper problemHelper) {
		this.problemHelper = problemHelper;
	}

	@ExceptionHandler(ApplicationProblem.class)
	public Mono<ResponseEntity<Problem>> handleApplicationProblem(ApplicationProblem problem,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(problem, request);
	}

	@ExceptionHandler(ApplicationException.class)
	public Mono<ResponseEntity<Problem>> handleApplicationException(ApplicationException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, request);
	}

	// Add more custom exception handlers below

}
