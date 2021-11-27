package com.clearcaptions.ccwsv3.common.boot.config.email;

import javax.validation.Valid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rajveer Singh
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ConditionalOnProperty(prefix = "application.email", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "application.email")
@Valid
public class EmailProperties {

	private boolean enabled = false;
	
	private String templatesLocation = "templates";
    
	private String replyTo;
	
	private boolean validateAddresses = false;
}
