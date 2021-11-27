package com.clearcaptions.ccwsv3.common.boot.config.error.reactive;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.SECURITY_EXCEPTION_HANDLER_BEAN_NAME;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import org.zalando.problem.spring.webflux.advice.security.SecurityAdviceTrait;

import com.clearcaptions.ccwsv3.common.boot.config.error.ProblemProperties;
import com.clearcaptions.ccwsv3.common.boot.config.error.SecurityExceptionAdviceConfigurationCondition;
import com.clearcaptions.ccwsv3.common.error.resolver.GeneralErrorResolver;

import reactor.core.publisher.Mono;

/**
 * @author Rajveer Singh
 */
@Configuration(value = SECURITY_EXCEPTION_HANDLER_BEAN_NAME)
@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(SecurityExceptionAdviceConfigurationCondition.class)
@ConditionalOnClass(value = { SecurityConfig.class, ProblemHandling.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnMissingBean(name = SECURITY_EXCEPTION_HANDLER_BEAN_NAME)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ReactiveSecurityExceptionHandler extends AbstractAdviceTrait implements SecurityAdviceTrait {

	private ReactiveProblemHelper problemHelper;

	public ReactiveSecurityExceptionHandler(ReactiveProblemHelper problemHelper) {
		this.problemHelper = problemHelper;
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleAccessDenied(AccessDeniedException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(GeneralErrorResolver.ACCESS_DENIED, exception, request);
	}

	@Override
	public Mono<ResponseEntity<Problem>> handleAuthentication(AuthenticationException exception,
			ServerWebExchange request) {
		return this.problemHelper.createProblem(GeneralErrorResolver.AUTHENTICATION_EXCEPTION, exception, request);
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	protected Mono<ResponseEntity<Problem>> handleInsufficientAuthenticationException(
			final InsufficientAuthenticationException exception, ServerWebExchange request) {
		return this.problemHelper.createProblem(GeneralErrorResolver.INSUFFICIENT_AUTHENTICATION, exception, request);
	}

}
