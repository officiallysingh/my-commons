package com.clearcaptions.ccwsv3.common.boot.config;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.APPLICATION_EVENT_MULTICASTER_BEAN_NAME;
import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;

import com.clearcaptions.ccwsv3.common.boot.config.error.ProblemProperties;
import com.clearcaptions.ccwsv3.common.message.MessageProvider;

@Configuration
@EnableConfigurationProperties(
		value = { TaskExecutionProperties.class, TaskSchedulingProperties.class, ProblemProperties.class })
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class CommonComponentsAutoConfiguration {

	@ConditionalOnMissingBean(name = APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
	@Bean
	public ApplicationEventMulticaster applicationEventMulticaster(
			@Qualifier(APPLICATION_TASK_EXECUTOR_BEAN_NAME) final Executor taskExecutor) {
		SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
		eventMulticaster.setTaskExecutor(taskExecutor);
		return eventMulticaster;
	}

	// If you are using Spring Boot, this is all you need:
	// server.forward-headers-strategy = framework|native|none
	// If you are NOT using Spring Boot, then you must configure following bean:
	// @ConditionalOnMissingBean
	// @Bean
	// public ForwardedHeaderTransformer forwardedHeaderTransformer() {
	// return new ForwardedHeaderTransformer();
	// }

	@ConditionalOnMissingBean(value = MessageProvider.class)
	@Bean
	public MessageProvider messageProvider(final MessageSource messageSource) {
		return new MessageProvider(messageSource);
	}

	@ConditionalOnMissingBean(value = SpringProfiles.class)
	@Bean
	public SpringProfiles springProfiles(final Environment environment) {
		return new SpringProfiles(environment);
	}

	@ConditionalOnMissingBean(value = CorsConfiguration.class)
	@Bean
	@ConfigurationProperties(prefix = "application.cors")
	public CorsConfiguration corsConfiguration() {
		return new CorsConfiguration();
	}

	@ConditionalOnMissingBean(value = MessageSourceProperties.class)
	@Bean
	@ConfigurationProperties(prefix = "spring.messages")
	public MessageSourceProperties messageSourceProperties() {
		return new MessageSourceProperties();
	}

	@ConditionalOnMissingBean(value = MessageSource.class)
	@Bean
	public MessageSource messageSource(final MessageSourceProperties properties) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames(properties.getBasename().split(","));
		messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
		messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
		messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
		return messageSource;
	}

//	@Configuration
//	@ConditionalOnClass(JaxbAnnotationModule.class)
//	public static class MappingJackson2XmlHttpMessageConverterConfiguration {
//		
//		@ConditionalOnMissingBean(value = MappingJackson2XmlHttpMessageConverter.class)
//		@Bean
//	    public MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter() {
//	        var jaxbAnnotationModule = new JaxbAnnotationModule();
//	        var mappingJackson2XmlHttpMessageConverter = new MappingJackson2XmlHttpMessageConverter();
//	        mappingJackson2XmlHttpMessageConverter.getObjectMapper().registerModule(jaxbAnnotationModule);
//	        return mappingJackson2XmlHttpMessageConverter;
//	    }
//	}
}
