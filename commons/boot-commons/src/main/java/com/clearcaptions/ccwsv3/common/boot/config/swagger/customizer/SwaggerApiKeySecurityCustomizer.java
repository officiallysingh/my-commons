package com.clearcaptions.ccwsv3.common.boot.config.swagger.customizer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import com.clearcaptions.ccwsv3.common.CCWSV3Constant;
import com.clearcaptions.ccwsv3.common.boot.config.security.SecurityProperties;
import com.clearcaptions.ccwsv3.common.boot.config.swagger.SwaggerProperties;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * A swagger customizer to setup {@link Docket} security settings.
 *
 * @author Rajveer Singh
 */
@Slf4j
public class SwaggerApiKeySecurityCustomizer implements SwaggerCustomizer, Ordered {

	private int order = Order.TWO.value();
	
	private static final AuthorizationScope[] API_KEY_AUTHORIZATION_SCOPES = {
    		new AuthorizationScope("global", "accessEverything")
    	};
	
	private final List<SecurityScheme> securitySchemes;
	
	private final SecurityContext securityContext;

	private static final String DEFAULT_SECURE_API_PATH = CCWSV3Constant.API_CONTEXT;

	private static String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
//	private static String AUTHORIZATION_HEADER = "X-Access-Token";
	
	public SwaggerApiKeySecurityCustomizer( 
			final SwaggerProperties swaggerProperties,
			final SecurityProperties securityProperties) {
		this(AUTHORIZATION_HEADER, swaggerProperties, securityProperties);
	}
	
	@SuppressWarnings("deprecation")
	public SwaggerApiKeySecurityCustomizer(final String authorizationHeader, 
			final SwaggerProperties swaggerProperties,
			final SecurityProperties securityProperties) {
		AUTHORIZATION_HEADER = authorizationHeader;
		Predicate<String> apiSelector = null;
		log.info("Default secure API Path : {}", DEFAULT_SECURE_API_PATH);
        if(ArrayUtils.isEmpty(securityProperties.getUnsecuredUris())) {
//        	this.apiSelector = PathSelectors.ant(DEFAULT_SECURE_API_PATH);
        	apiSelector = PathSelectors.any();
        } else {
        	log.info("Unsecure API Paths: {}", Arrays.stream(securityProperties.getUnsecuredUris())
        			.collect(Collectors.joining(", ")));
        	Predicate<String> predicate = Arrays.stream(securityProperties.getUnsecuredUris())
    			.map(uri -> PathSelectors.ant(uri).negate())
    			.reduce(Predicate::and).orElse(x -> true);
            apiSelector = predicate;
        }
        
        this.securitySchemes = Arrays.asList(apiKeySecurityScheme()); 
        this.securityContext = SecurityContext.builder()
        		.securityReferences(Arrays.asList(apiKeySecurityReference()))
        		.forPaths(apiSelector).build();
	}

	@Override
	public void customize(final Docket docket) {
		docket.securityContexts(Collections.singletonList(this.securityContext))
        	.securitySchemes(this.securitySchemes);
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}
    
	public static SecurityReference apiKeySecurityReference() {
		return new SecurityReference("JWT", API_KEY_AUTHORIZATION_SCOPES);
	}

	private SecurityScheme apiKeySecurityScheme() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }
}
