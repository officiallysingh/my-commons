package com.clearcaptions.ccwsv3.common.boot.config.error;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.APPLICATION_EXCEPTION_HANDLER_BEAN_NAME;

import java.net.SocketTimeoutException;

import javax.annotation.Nonnull;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import com.clearcaptions.ccwsv3.common.error.resolver.GeneralErrorResolver;

/**
 * @author Rajveer Singh
 */
@Configuration(value = APPLICATION_EXCEPTION_HANDLER_BEAN_NAME)
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConditionalOnClass(ProblemHandling.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnMissingBean(name = APPLICATION_EXCEPTION_HANDLER_BEAN_NAME)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class WebExceptionHandler extends AbstractAdviceTrait implements ProblemHandling {

	private ProblemHelper problemHelper;

	public WebExceptionHandler(ProblemHelper problemHelper) {
		this.problemHelper = problemHelper;
	}

	@Override
	public ResponseEntity<Problem> handleProblem(ThrowableProblem problem, NativeWebRequest request) {
		return create(problem, request);
	}

	@Override
	public ResponseEntity<Problem> handleThrowable(Throwable throwable, NativeWebRequest request) {
		return this.problemHelper.createProblem(throwable, request);
	}

	@Override
	public ResponseEntity<Problem> handleConstraintViolation(ConstraintViolationException exception,
			NativeWebRequest request) {
		return this.problemHelper.createProblem(exception, request);
	}

	@Override
	public ResponseEntity<Problem> handleMissingServletRequestParameter(
			MissingServletRequestParameterException exception, @Nonnull NativeWebRequest request) {
		return this.problemHelper.createProblem(exception, request);
	}

	@Override
	public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			@Nonnull NativeWebRequest request) {
		return this.problemHelper.createProblem(exception, request);
	}

	@Override
	public ResponseEntity<Problem> handleUnsupportedOperation(UnsupportedOperationException exception,
			NativeWebRequest request) {
		return this.problemHelper.createProblem(exception, GeneralErrorResolver.UNSUPPORTED_OPERATION_EXCEPTION,
				request);
	}

	@Override
	public ResponseEntity<Problem> handleResponseStatusException(ResponseStatusException exception,
			NativeWebRequest request) {
		return this.problemHelper.createProblem(exception, request);
	}

	@Override
	public ResponseEntity<Problem> handleSocketTimeout(SocketTimeoutException exception, NativeWebRequest request) {
		return this.problemHelper.createProblem(exception, GeneralErrorResolver.SOCKET_TIMEOUT_EXCEPTION, request);
	}
	
}
