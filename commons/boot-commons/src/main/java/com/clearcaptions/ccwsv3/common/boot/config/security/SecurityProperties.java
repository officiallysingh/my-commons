package com.clearcaptions.ccwsv3.common.boot.config.security;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpMethod;

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
@ConditionalOnProperty(prefix = "application.security", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "application.security")
@Valid
public class SecurityProperties {
	
	public static final String IMPLICIT_SCOPE_PREFIX = "SCOPE_";
	 
	private boolean enabled = false;

	private String[] unsecuredUris = {};
	
	/**
	 * List of Secured URIs Ant patterns, HTTP Method and the Scope to be secured with
	 */
	private List<SecuredUri> securedUris;
	
	/**
	 * Secured URIs Ant patterns, HTTP Method and the Scope to be secured with
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	@ToString
	@Valid
	public static class SecuredUri {
		
		/**
		 * The List of Ant patterns to be secured
		 */
		@NotEmpty
		private String[] antPatterns;
		
		/**
		 * The HTTP Method for given ant patterns to be secured
		 */
		private HttpMethod method;

		/**
		 * Whether to apply conjunction or disjunction on given scopes, Default: DISJUNCTION 
		 */
		private ScopeCompositionType scopeCompositionType = ScopeCompositionType.DISJUNCTION;
		
		/**
		 * The Scope to be secured with. Prefixed automatically with 'SCOPE_clearcaptions/' 
		 */
		@NotEmpty
		private String[] scopes;
	}
	
}
