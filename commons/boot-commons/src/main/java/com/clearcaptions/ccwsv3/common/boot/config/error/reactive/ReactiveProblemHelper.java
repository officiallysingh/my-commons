package com.clearcaptions.ccwsv3.common.boot.config.error.reactive;

import static com.clearcaptions.ccwsv3.common.boot.config.error.ProblemConstants.Keys.FIELD_ERRORS;
import static com.clearcaptions.ccwsv3.common.boot.config.error.ProblemConstants.Keys.VIOLATIONS;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.webflux.advice.AdviceTrait;

import com.clearcaptions.ccwsv3.common.boot.config.error.AbstractProblemHelper;
import com.clearcaptions.ccwsv3.common.boot.config.error.FieldErrorVM;
import com.clearcaptions.ccwsv3.common.boot.config.error.ProblemProperties;
import com.clearcaptions.ccwsv3.common.boot.config.error.ViolationVM;
import com.clearcaptions.ccwsv3.common.error.ApplicationException;
import com.clearcaptions.ccwsv3.common.error.ApplicationProblem;
import com.clearcaptions.ccwsv3.common.error.resolver.ErrorResolver;
import com.clearcaptions.ccwsv3.common.error.resolver.GeneralErrorResolver;

import reactor.core.publisher.Mono;

/**
 * @author Rajveer Singh
 */
public class ReactiveProblemHelper extends AbstractProblemHelper implements AdviceTrait {

	public ReactiveProblemHelper(final Environment env,
			final ProblemProperties properties) {
		super(env.getProperty("spring.webflux.base-path"), properties);
	}

	Mono<ResponseEntity<Problem>> createProblem(final ErrorResolver errorResolver, final Throwable exception,
			final ServerWebExchange request) {
		ProblemBuilder problemBuilder = problemBuilder(errorResolver, requestUri(request));
		problemBuilder.withType(generateType(requestUri(request), errorResolver.typeCode()));
		addDebugInfo(problemBuilder, exception);
		return create(exception, problemBuilder.build(), request);
	}

	Mono<ResponseEntity<Problem>> createProblem(final ApplicationProblem problem, final ServerWebExchange request) {
		ErrorResolver errorResolver = problem.getErrorResolver();
		ProblemBuilder problemBuilder = errorResolver != null ?
				problemBuilder(errorResolver, requestUri(request))
				: problemBuilder(problem.getTitle(), requestUri(request), 
						problem.getStatus(), problem.getMessage(), 
						problem.getLocalizedMessage(), time());
		problemBuilder.withType(errorResolver != null ? generateType(requestUri(request), 
				errorResolver.typeCode()) 
				: problem.getType());
		addDebugInfo(problemBuilder, problem);
		return create(problem, problemBuilder.build(), request);
	}

	Mono<ResponseEntity<Problem>> createProblem(final ApplicationException exception, final ServerWebExchange request) {
		ErrorResolver errorResolver = exception.getErrorResolver();
		ProblemBuilder problemBuilder = problemBuilder(errorResolver, requestUri(request));
		problemBuilder.withType(generateType(requestUri(request), errorResolver.typeCode()));
		addDebugInfo(problemBuilder, exception);
		return create(exception, problemBuilder.build(), request);
	}

	Mono<ResponseEntity<Problem>> createProblem(final ConstraintViolationException exception,
			final ServerWebExchange request) {
		ErrorResolver errorResolver = GeneralErrorResolver.CONSTRAINT_VIOLATIONS;
		ProblemBuilder problemBuilder = problemBuilder(errorResolver, requestUri(request));
		problemBuilder.withType(generateType(requestUri(request), errorResolver.typeCode()));
		List<ViolationVM> violations = exception.getConstraintViolations().stream().map(voilation -> ViolationVM.of(properties, voilation))
				.collect(Collectors.toList());
		problemBuilder.with(VIOLATIONS, violations);
		addDebugInfo(problemBuilder, exception);
		return create(exception, problemBuilder.build(), request);
	}

	Mono<ResponseEntity<Problem>> createProblem(final WebExchangeBindException exception,
			final ServerWebExchange request) {
		ErrorResolver errorResolver = GeneralErrorResolver.VALIDATION_FAILURE;
		ProblemBuilder problemBuilder = problemBuilder(errorResolver, requestUri(request));

		BindingResult result = exception.getBindingResult();
		List<FieldErrorVM> fieldErrors = result.getFieldErrors().stream().map(fieldError -> FieldErrorVM.of(properties, fieldError))
				.collect(Collectors.toList());

		problemBuilder.withType(generateType(requestUri(request), errorResolver.typeCode())).with(FIELD_ERRORS, fieldErrors);
		addDebugInfo(problemBuilder, exception);
		return create(exception, problemBuilder.build(), request);
	}

	Mono<ResponseEntity<Problem>> createProblem(final Throwable throwable, final ServerWebExchange request) {
		return create(throwable, problemInstance(throwable, requestUri(request)), request);
	}

	Mono<ResponseEntity<Problem>> createProblem(final Exception exception, ErrorResolver errorResolver,
			final ServerWebExchange request) {
		return create(exception, problemInstance(exception, errorResolver, requestUri(request)), request);
	}

	Mono<ResponseEntity<Problem>> createProblem(final ResponseStatusException exception,
			final ServerWebExchange request) {
		ErrorResolver errorResolver = GeneralErrorResolver.RESPONSE_STATUS_EXCEPTION;
		ProblemBuilder problemBuilder = problemBuilder(errorResolver.title(),
				requestUri(request), Status.valueOf(exception.getRawStatusCode()), errorResolver.message(), 
				errorResolver.localizedMessage(), time());
		addDebugInfo(problemBuilder, exception);
		return create(exception, problemBuilder.build(), request);
	}

	URI requestUri(final ServerWebExchange request) {
//		return request.getRequest().getURI();
		return URI.create(request.getRequest().getPath().pathWithinApplication().toString());
	}

}
