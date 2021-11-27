package com.clearcaptions.ccwsv3.common.boot.config.error;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import com.clearcaptions.ccwsv3.common.error.ApplicationException;
import com.clearcaptions.ccwsv3.common.error.ApplicationProblem;

/**
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConditionalOnClass(ProblemHandling.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ApplicationExceptionHandler extends AbstractAdviceTrait {

	private ProblemHelper problemHelper;

	public ApplicationExceptionHandler(ProblemHelper problemHelper) {
		this.problemHelper = problemHelper;
	}

	@ExceptionHandler(ApplicationProblem.class)
	public ResponseEntity<Problem> handleApplicationProblem(ApplicationProblem problem, NativeWebRequest request) {
		return this.problemHelper.createProblem(problem, request);
	}

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<Problem> handleApplicationException(ApplicationException exception,
			NativeWebRequest request) {
		return this.problemHelper.createProblem(exception, request);
	}

	// Add more custom exception handlers below

}
