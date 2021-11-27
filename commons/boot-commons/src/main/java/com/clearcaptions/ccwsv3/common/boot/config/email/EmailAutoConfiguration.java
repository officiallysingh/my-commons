package com.clearcaptions.ccwsv3.common.boot.config.email;

import javax.activation.MimeType;
import javax.mail.internet.MimeMessage;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.clearcaptions.ccwsv3.common.boot.config.email.EmailAutoConfiguration.EmailAutoConfigurationCondition;


/**
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties({ MailProperties.class, EmailProperties.class })
@ConditionalOnProperty(prefix = "application.email", name = "enabled", havingValue = "true")
@Conditional(EmailAutoConfigurationCondition.class)
@ConditionalOnClass({ MimeMessage.class, MimeType.class, SpringTemplateEngine.class })
@ConditionalOnMissingBean(EmailAutoConfiguration.class)
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import(MailSenderAutoConfiguration.class)
public class EmailAutoConfiguration {

	@Bean
	public EmailClient emailClient(final JavaMailSender mailSender, 
			final SpringTemplateEngine templateEngine,
			final EmailProperties properties) {
		return new EmailClient(mailSender, templateEngine, properties);
	}

	/**
	 * Condition to trigger the creation of a {@link MailSender}. This kicks in if either
	 * the host or jndi name property is set.
	 */
	static class EmailAutoConfigurationCondition extends AnyNestedCondition {

		EmailAutoConfigurationCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "spring.mail", name = "host")
		static class HostProperty {

		}

		@ConditionalOnProperty(prefix = "spring.mail", name = "jndi-name")
		static class JndiNameProperty {

		}
	}
}
