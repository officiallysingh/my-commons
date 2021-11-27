package com.clearcaptions.ccwsv3.common.boot.config.swagger;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

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
@ConditionalOnProperty(prefix = "application.swagger", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "application.swagger")
@Valid
public class SwaggerProperties {

	private boolean enabled = false;

	@NotEmpty
	private String title = "Application API";

	@NotEmpty
	private String description = "API documentation";

	private String version = "0.0.1";

	private String termsOfServiceUrl;

	private String contactName;

	private String contactUrl;

	private String contactEmail;

	private String license;

	private String licenseUrl;

	private String host;

	private String[] protocols = {};
	
	private boolean useDefaultResponseMessages = true;

	private DefaultGroup defaultGroup = new DefaultGroup();
	
	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Valid
	public static class DefaultGroup {

		private boolean enabled = false;
		
		private String name = "default";

		private String[] inclusionPattern = { "/**" };

		private String[] exclusionPattern = { "" };
	}
}
