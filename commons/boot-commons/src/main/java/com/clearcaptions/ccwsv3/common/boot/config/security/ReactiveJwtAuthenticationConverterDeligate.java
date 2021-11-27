package com.clearcaptions.ccwsv3.common.boot.config.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.util.Assert;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveJwtAuthenticationConverterDeligate implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private String principalClaimName;

    private Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new ReactiveJwtGrantedAuthoritiesConverterAdapter(
            new JwtGrantedAuthoritiesConverter());

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        // @formatter:off
        return this.jwtGrantedAuthoritiesConverter.convert(jwt)
                .collectList()
                .map((authorities) -> this.principalClaimName == null
                    ? new JwtAuthenticationToken(jwt, authorities)
                            : new JwtAuthenticationToken(jwt, authorities, 
                                    jwt.getClaimAsString(this.principalClaimName)));
        // @formatter:on
    }

    /**
     * Sets the {@link Converter Converter&lt;Jwt, Flux&lt;GrantedAuthority&gt;&gt;}
     * to use. Defaults to a reactive {@link JwtGrantedAuthoritiesConverter}.
     * 
     * @param jwtGrantedAuthoritiesConverter The converter
     * @see JwtGrantedAuthoritiesConverter
     */
    public void setJwtGrantedAuthoritiesConverter(
            Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
    }

    /**
     * Sets the principal claim name. Defaults to {@link JwtClaimNames#SUB}.
     * 
     * @param principalClaimName The principal claim name
     */
    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }
}
