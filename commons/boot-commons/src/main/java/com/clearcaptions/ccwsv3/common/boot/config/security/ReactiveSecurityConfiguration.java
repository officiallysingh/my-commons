package com.clearcaptions.ccwsv3.common.boot.config.security;

import static com.clearcaptions.ccwsv3.common.BootConstant.BeanName.SECURITY_CONFIGURATION_BEAN_NAME;
import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.ALL_ACCESS_SCOPE;
import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.USERNAME_CLAIM_KEY;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.util.CollectionUtils;
import org.zalando.problem.spring.webflux.advice.security.SecurityProblemSupport;

import com.clearcaptions.ccwsv3.common.boot.config.security.SecurityProperties.SecuredUri;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Rajveer Singh
 */
@Configuration(value = SECURITY_CONFIGURATION_BEAN_NAME)
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "application.security", name = "enabled", havingValue = "true")
@ConditionalOnClass(WebSecurityConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnMissingBean(name = SECURITY_CONFIGURATION_BEAN_NAME)
@Import(SecurityProblemSupport.class)
@EnableWebFluxSecurity
//Allow method annotations like @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@Slf4j
public class ReactiveSecurityConfiguration {

    private final SecurityProperties properties;

    private final SecurityProblemSupport problemSupport;

    public ReactiveSecurityConfiguration(final SecurityProperties properties,
            final SecurityProblemSupport problemSupport) {
        this.properties = properties;
        this.problemSupport = problemSupport;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        // @formatter:off
	    
        http.csrf().disable()
            .authorizeExchange()
            .pathMatchers("/swagger-resources/**", "/swagger-ui/**", "/v2/api-docs", "/webjars/**").permitAll()
            .pathMatchers(this.properties.getUnsecuredUris()).permitAll()
            .pathMatchers("/**").hasAuthority(ALL_ACCESS_SCOPE)
            .and().oauth2ResourceServer()
            .jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
            .jwtDecoder(jwtDecoder());
        
        if(!CollectionUtils.isEmpty(this.properties.getSecuredUris())) {
            for(int i = 0; i < this.properties.getSecuredUris().size(); i++) {
                SecuredUri securedUri = this.properties.getSecuredUris().get(i);
//                String authoritiesExpression = SecurityHelper.getAuthoritiesExpression(securedUri.getScopes(), 
//                        securedUri.getScopeCompositionType());
                if(securedUri.getMethod() != null) {
//                    log.debug(securedUri.getMethod() + " " 
//                            + Arrays.asList(securedUri.getAntPatterns()) + " --> " + authoritiesExpression);
                    http.authorizeExchange()
                        .pathMatchers(securedUri.getMethod(), securedUri.getAntPatterns())
                        /*.access(authoritiesExpression)*/;
                } else {
//                    log.debug(Arrays.asList(securedUri.getAntPatterns()) + " --> " + authoritiesExpression);
                    http.authorizeExchange()
                        .pathMatchers(securedUri.getAntPatterns())
                        /*.access(authoritiesExpression)*/;
                }
            }
            http.authorizeExchange().anyExchange().authenticated();
        }
      
        http.exceptionHandling().authenticationEntryPoint(this.problemSupport)
            .accessDeniedHandler(this.problemSupport);
        
        return http.build();
        
      // @formatter:on
    }
    
    private ReactiveAuthorizationManager<AuthorizationContext> manager() {
        
        return null;
    }
    
//    ReactiveAuthorizationManager<AuthorizationContext> hasRole1AndRole2 = (authN, ctx) -> 
//    Flux.just(hasRole("1"), hasRole("2"))
//        .flatMap(a -> a.check(authN, ctx))
//        .filter(a -> !a.isGranted()).hasElements()
//        .map(b -> b.booleanValue() ? new AuthorizationDecision(false) : new AuthorizationDecision(true));
//
//    http.authorizeExchange().anyExchange().access(hasRole1AndRole2);

//    ReactiveAuthorizationManager<AuthorizationContext> hasRole1OrRole2 = (authN, ctx) -> 
//    Flux.just(hasRole("1"), hasRole("2"))
//        .flatMap(a -> a.check(authN, ctx))
//        .filter(a -> a.isGranted()).defaultIfEmpty(new AuthorizationDecision(false)).next();
//
//    http.authorizeExchange().anyExchange().access(hasRole1OrRole2);

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverterDeligate jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverterDeligate();
        ReactiveJwtGrantedAuthoritiesConverterAdapter jwtGrantedAuthoritiesConverter = new ReactiveJwtGrantedAuthoritiesConverterAdapter(
                SecurityHelper.jwtGrantedAuthoritiesConverter());
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        jwtAuthenticationConverter.setPrincipalClaimName(USERNAME_CLAIM_KEY);
        return jwtAuthenticationConverter;
    }

    private ReactiveJwtDecoder jwtDecoder() {
        return new CcwsReactiveJwtDecoder();
    }

    private static class CcwsReactiveJwtDecoder implements ReactiveJwtDecoder {

        @Override
        public Mono<Jwt> decode(String token) throws JwtException {
            return Mono.fromCallable(() -> {
                return SecurityHelper.decodeToken(token);
            }).subscribeOn(Schedulers.boundedElastic());
        }
    }
}
