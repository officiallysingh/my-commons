package com.clearcaptions.ccwsv3.common.boot.config.error;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.SECURITY_EXCEPTION_HANDLER_BEAN_NAME;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import com.clearcaptions.ccwsv3.common.error.resolver.GeneralErrorResolver;

/**
 * @author Rajveer Singh
 */
@Configuration(value = SECURITY_EXCEPTION_HANDLER_BEAN_NAME)
@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(SecurityExceptionAdviceConfigurationCondition.class)
@ConditionalOnClass(value = { WebSecurityConfiguration.class, ProblemHandling.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class SecurityExceptionHandler extends AbstractAdviceTrait implements SecurityAdviceTrait {

	private ProblemHelper problemHelper;
	
	public SecurityExceptionHandler(final ProblemHelper problemHelper) {
		this.problemHelper = problemHelper;
	}

	@Override
	public ResponseEntity<Problem> handleAccessDenied(final AccessDeniedException exception, final NativeWebRequest request) {
		return this.problemHelper.createProblem(GeneralErrorResolver.ACCESS_DENIED, exception, request);
	}

	@Override
	public ResponseEntity<Problem> handleAuthentication(final AuthenticationException exception, final NativeWebRequest request) {
		return this.problemHelper.createProblem(GeneralErrorResolver.AUTHENTICATION_EXCEPTION, exception, request);
	}

	@ExceptionHandler(InsufficientAuthenticationException.class)
	public ResponseEntity<Problem> handleInsufficientAuthenticationException(
			final InsufficientAuthenticationException exception, final NativeWebRequest request) {
		return this.problemHelper.createProblem(GeneralErrorResolver.INSUFFICIENT_AUTHENTICATION, exception, request);
	}

}
