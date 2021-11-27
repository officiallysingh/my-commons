package com.clearcaptions.ccwsv3.common.boot.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Rajveer Singh
 */
@Configuration
@ConditionalOnMissingBean(JacksonAutoConfiguration.class)
@AutoConfigureBefore(value = org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class JacksonAutoConfiguration {

	@Configuration
	@ConditionalOnClass(JavaTimeModule.class)
	public static class JavaTimeModuleConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public JavaTimeModule javaTimeModule() {
			return new JavaTimeModule();
		}

	}

	@Configuration
	@ConditionalOnMissingClass("org.zalando.problem.spring.web.autoconfigure.ProblemJacksonAutoConfiguration")
	@ConditionalOnClass(ProblemModule.class)
	public static class ProblemModuleConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public ProblemModule problemModule() {
			return new ProblemModule();
		}

	}

	@Configuration
	@ConditionalOnMissingClass("org.zalando.problem.spring.web.autoconfigure.ProblemJacksonAutoConfiguration")
	@ConditionalOnClass(ConstraintViolationProblemModule.class)
	public static class ConstraintViolationProblemModuleConfiguration {

		@Bean
		@ConditionalOnMissingBean
		public ConstraintViolationProblemModule constraintViolationProblemModule() {
			return new ConstraintViolationProblemModule();
		}

	}

	public ObjectMapper objectMapper(final Iterable<? extends Module> modules) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModules(modules);
		return objectMapper;
	}
}
