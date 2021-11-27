package com.clearcaptions.ccwsv3.common.boot.config.error.reactive;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.PROBLEM_AUTO_CONFIGURATION_BEAN_NAME;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;

import com.clearcaptions.ccwsv3.common.boot.config.CommonComponentsAutoConfiguration;
import com.clearcaptions.ccwsv3.common.boot.config.error.ProblemProperties;

/**
 * @author Rajveer Singh
 */
@Configuration(value = PROBLEM_AUTO_CONFIGURATION_BEAN_NAME)
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConditionalOnClass(ProblemHandling.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnMissingBean(name = PROBLEM_AUTO_CONFIGURATION_BEAN_NAME)
@AutoConfigureAfter(value = CommonComponentsAutoConfiguration.class)
@Import({ ReactiveApplicationExceptionHandler.class, ReactiveSecurityExceptionHandler.class, /*
																								 * CircuitBreakerExceptionHandler
																								 * .class,
																								 */
		ReactiveWebExceptionHandler.class })
public class ReactiveProblemAutoConfiguration {

	@ConditionalOnMissingBean(name = "problemHelper")
	@Bean
	public ReactiveProblemHelper problemHelper(@Value("${web-application-type}") WebApplicationType webApplicationType,
			final Environment env, final ProblemProperties problemProperties) {
		return new ReactiveProblemHelper(env, problemProperties);
	}

}
