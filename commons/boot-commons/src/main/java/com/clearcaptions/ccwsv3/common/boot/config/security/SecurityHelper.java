package com.clearcaptions.ccwsv3.common.boot.config.security;

import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.ALL_ACCESS_SCOPE;
import static com.clearcaptions.ccwsv3.common.boot.config.security.SecurityConstant.SCOPE_CLAIM_KEY;

import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.Jwt.Builder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.clearcaptions.ccwsv3.common.error.ApplicationProblem;
import com.clearcaptions.ccwsv3.common.error.resolver.GeneralErrorResolver;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.PlainJWT;
import com.nimbusds.jwt.proc.BadJWTException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SecurityHelper {

    private static Converter<Map<String, Object>, Map<String, Object>> claimSetConverter = MappedJwtClaimSetConverter
            .withDefaults(Collections.emptyMap());

    static Jwt decodeToken(String token) throws JwtException {
        log.debug("token --->>" + token);
        JWT jwt = parse(token);
        if (jwt instanceof PlainJWT) {
            log.trace("Failed to decode unsigned token");
            throw new BadJwtException("Unsupported algorithm of " + jwt.getHeader().getAlgorithm());
        }
        Jwt createdJwt = createJwt(token, jwt);
        log.debug("---------------- Headers ----------------\n");
        for (Entry<String, Object> entry : createdJwt.getHeaders().entrySet()) {
            log.debug(entry.getKey() + " : " + entry.getValue());
        }
        log.debug("------------------------------------------\n");
        log.debug("---------------- Claims ----------------\n");
        for (Entry<String, Object> entry : createdJwt.getClaims().entrySet()) {
            log.debug(entry.getKey() + " : " + entry.getValue());
        }
        log.debug("------------------------------------------");
        return createdJwt;
    }

    private static JWT parse(String token) {
        try {
            return JWTParser.parse(token);
        } catch (final Exception e) {
            log.trace("Failed to parse token", e);
            throw new ApplicationProblem(GeneralErrorResolver.BAD_JWT.parameters(e.getMessage()), e);
        }
    }

    private static Jwt createJwt(String token, JWT parsedJwt) {
        try {
            JWTClaimsSet jwtClaimsSet = extractJWTClaimsSet(parsedJwt);
            Map<String, Object> headers = new LinkedHashMap<>(parsedJwt.getHeader().toJSONObject());
            Map<String, Object> claims = claimSetConverter.convert(jwtClaimsSet.getClaims());
            claims.put(SCOPE_CLAIM_KEY, ALL_ACCESS_SCOPE);
            // @formatter:off
            Builder jwtBuilder = Jwt.withTokenValue(token)
                    .headers((h) -> h.putAll(headers))
                    .claims((c) -> c.putAll(claims));
                return jwtBuilder.build();
            // @formatter:on
        } catch (final Exception e) {
            log.trace("Failed to process JWT", e);
            if (e.getCause() instanceof ParseException) {
                throw new ApplicationProblem(GeneralErrorResolver.BAD_JWT.parameters("Malformed payload"), e);
            }
            throw new ApplicationProblem(GeneralErrorResolver.BAD_JWT.parameters(e.getMessage()), e);
        }
    }

    private static JWTClaimsSet extractJWTClaimsSet(final JWT jwt) throws BadJWTException {
        try {
            return jwt.getJWTClaimsSet();
        } catch (final ParseException e) {
            // Payload not a JSON object
            throw new ApplicationProblem(GeneralErrorResolver.BAD_JWT.parameters(e.getMessage()), e);
        }
    }

    static JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        return jwtGrantedAuthoritiesConverter;
    }
}
