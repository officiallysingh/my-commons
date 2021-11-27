package com.clearcaptions.ccwsv3.common.boot.config.error;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "application.problem", name = "enabled", havingValue = "true")
@ConditionalOnClass(ProblemHandling.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({ ApplicationExceptionHandler.class, SecurityExceptionHandler.class, WebExceptionHandler.class })
public class ProblemAutoConfiguration {

	@ConditionalOnMissingBean(name = "problemHelper")
	@Bean
	public ProblemHelper problemHelper(final Environment env, final ProblemProperties problemProperties) {
		return new ProblemHelper(env, problemProperties);
	}

}
