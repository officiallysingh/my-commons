package com.clearcaptions.ccwsv3.common.boot.config.error.reactive;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.WEB_EXCEPTION_HANDLER_BEAN_NAME;

import java.net.SocketTimeoutException;

import javax.validation.ConstraintViolationException;

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
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;

import com.clearcaptions.ccwsv3.common.boot.config.error.ProblemProperties;
import com.clearcaptions.ccwsv3.common.error.resolver.GeneralErrorResolver;

import reactor.core.publisher.Mono;

/**
 * @author Rajveer Singh
 */
@Configuration(value = WEB_EXCEPTION_HANDLER_BEAN_NAME)
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConditionalOnClass(ProblemHandling.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnMissingBean(name = WEB_EXCEPTION_HANDLER_BEAN_NAME)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
public class ReactiveWebExceptionHandler extends AbstractAdviceTrait implements ProblemHandling {

	private ReactiveProblemHelper problemHelper;

	public ReactiveWebExceptionHandler(ReactiveProblemHelper problemHelper) {
		this.problemHelper = problemHelper;
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleProblem(ThrowableProblem problem, ServerWebExchange request) {
		return create(problem, request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleThrowable(Throwable throwable, ServerWebExchange request) {
		return this.problemHelper.createProblem(throwable, request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleConstraintViolation(ConstraintViolationException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleBindingResult(WebExchangeBindException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleUnsupportedOperation(UnsupportedOperationException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, GeneralErrorResolver.UNSUPPORTED_OPERATION_EXCEPTION,
				request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleRequestMethodNotSupportedException(MethodNotAllowedException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, GeneralErrorResolver.METHOD_NOT_ALLOWED_EXCEPTION, request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleMediaTypeNotAcceptable(NotAcceptableStatusException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, GeneralErrorResolver.NOT_ACCEPTABLE_STATUS_EXCEPTION,
				request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleMediaTypeNotSupportedException(
			UnsupportedMediaTypeStatusException exception, ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, GeneralErrorResolver.UNSUPPORTED_MEDIA_TYPE_STATUS_EXCEPTION,
				request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleResponseStatusException(ResponseStatusException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleSocketTimeout(SocketTimeoutException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(exception, GeneralErrorResolver.SOCKET_TIMEOUT_EXCEPTION, request);
	}

}
