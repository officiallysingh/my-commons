package com.clearcaptions.ccwsv3.common.boot.config.security;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.SECURITY_CONFIGURATION_BEAN_NAME;
import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.ALL_ACCESS_SCOPE;
import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.HAS_AUTHORITY;
import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.HAS_AUTHORITY_REMPLATE;
import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.USERNAME_CLAIM_KEY;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.CollectionUtils;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import com.clearcaptions.ccwsv3.common.boot.config.security.SecurityProperties.SecuredUri;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Rajveer Singh
 */
@Configuration(value = SECURITY_CONFIGURATION_BEAN_NAME)
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "application.security", name = "enabled", havingValue = "true")
@ConditionalOnClass(WebSecurityConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnMissingBean(name = SECURITY_CONFIGURATION_BEAN_NAME)
@AutoConfigureBefore(SecurityAutoConfiguration.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Import(value = { SecurityProblemSupport.class })
@EnableWebSecurity
//Allow method annotations like @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) 
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final SecurityProperties properties;

    private final SecurityProblemSupport problemSupport;

    public SecurityConfiguration(final SecurityProperties properties, final SecurityProblemSupport problemSupport) {
        this.properties = properties;
        this.problemSupport = problemSupport;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // @formatter:off
    	http.csrf().disable()
      		.authorizeRequests()
          	.antMatchers("/swagger-resources/**", "/swagger-ui/**", "/v2/api-docs", "/webjars/**").permitAll()
            .antMatchers(this.properties.getUnsecuredUris()).permitAll()
            .antMatchers("/**").hasAuthority(ALL_ACCESS_SCOPE)
			.and().oauth2ResourceServer()
			.jwt().jwtAuthenticationConverter(this.jwtAuthenticationConverter()).decoder(this.jwtDecoder());
    	
    	if(!CollectionUtils.isEmpty(this.properties.getSecuredUris())) {
    		for(int i = 0; i < this.properties.getSecuredUris().size(); i++) {
    			SecuredUri securedUri = this.properties.getSecuredUris().get(i);
    			String authoritiesExpression = this.accessExpression(securedUri.getScopes(), 
						securedUri.getScopeCompositionType());
    			if(securedUri.getMethod() != null) {
    				log.debug(securedUri.getMethod() + " " 
        					+ Arrays.asList(securedUri.getAntPatterns()) + " --> " + authoritiesExpression);
    				http.authorizeRequests()
    					.antMatchers(securedUri.getMethod(), securedUri.getAntPatterns())
    					.access(authoritiesExpression);
    			} else {
    				log.debug(Arrays.asList(securedUri.getAntPatterns()) + " --> " + authoritiesExpression);
    				http.authorizeRequests()
						.antMatchers(securedUri.getAntPatterns())
    					.access(authoritiesExpression);
    			}
    		}
    		http.authorizeRequests().anyRequest().authenticated();
    	}
      
    	http.exceptionHandling().authenticationEntryPoint(this.problemSupport)
  			.accessDeniedHandler(this.problemSupport);
      // @formatter:on
    }

    private String accessExpression(final String[] scopes, final ScopeCompositionType scopeCompositionType) {
        return Arrays.stream(scopes).map(scope -> {
            if (scope.contains(HAS_AUTHORITY)) {
                return scope.indexOf(HAS_AUTHORITY) == scope.lastIndexOf(HAS_AUTHORITY) 
                        ? scope : "('" + scope + "')";
            } else {
                return String.format(HAS_AUTHORITY_REMPLATE, scope);
            }
        }).collect(Collectors.joining(" " + scopeCompositionType.operator() + " "));
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(SecurityHelper.jwtGrantedAuthoritiesConverter());
        jwtAuthenticationConverter.setPrincipalClaimName(USERNAME_CLAIM_KEY);
        return jwtAuthenticationConverter;
    }

    private JwtDecoder jwtDecoder() {
        return new CcwsJwtDecoder();
    }

    private static class CcwsJwtDecoder implements JwtDecoder {

        @Override
        public Jwt decode(String token) throws JwtException {
            return SecurityHelper.decodeToken(token);
        }
    }
}
