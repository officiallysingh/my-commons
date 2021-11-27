package com.clearcaptions.ccwsv3.common.boot.config.swagger.customizer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.Ordered;

import com.clearcaptions.ccwsv3.common.CCWSV3Constant;
import com.clearcaptions.ccwsv3.common.boot.config.security.SecurityProperties;
import com.clearcaptions.ccwsv3.common.boot.config.swagger.SwaggerProperties;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
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
public class SwaggerOAuth2SecurityCustomizer implements SwaggerCustomizer, Ordered {

	private int order = Order.TWO.value();

	private static final AuthorizationScope[] OAUTH2_AUTHORIZATION_SCOPES = {
    		new AuthorizationScope("read", "Read All"),
    		new AuthorizationScope("write", "Write All")
    	};
	
	private final List<SecurityScheme> securitySchemes;
	
	private final SecurityContext securityContext;

	private static final String DEFAULT_SECURE_API_PATH = CCWSV3Constant.API_CONTEXT;
	
	@SuppressWarnings("deprecation")
	public SwaggerOAuth2SecurityCustomizer(final SwaggerProperties swaggerProperties,
			final SecurityProperties securityProperties, final String authTokenUri) {
		
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

        this.securitySchemes = Arrays.asList(oauth2SecurityScheme(authTokenUri)); 
        this.securityContext = SecurityContext.builder()
        		.securityReferences(Arrays.asList(oauth2SecurityReference()))
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

	public static SecurityReference oauth2SecurityReference() {
		return new SecurityReference("oauth2", OAUTH2_AUTHORIZATION_SCOPES);
	}

	public static OAuth oauth2SecurityScheme(final String authTokenUri) {
		List<AuthorizationScope> authorizationScopeList = Lists.newArrayList(OAUTH2_AUTHORIZATION_SCOPES);
        List<GrantType> grantTypes = Lists.newArrayList(
        		new ResourceOwnerPasswordCredentialsGrant(authTokenUri));
        return new OAuth("oauth2", authorizationScopeList, grantTypes);
	}
}
