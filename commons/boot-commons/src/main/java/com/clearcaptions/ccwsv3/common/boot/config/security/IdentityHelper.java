package com.clearcaptions.ccwsv3.common.boot.config.security;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.clearcaptions.ccwsv3.common.CCWSV3Constant;

import lombok.Value;

/**
 * @author Rajveer Singh
 */
@Value
public final class IdentityHelper {

    private IdentityHelper() {
        // This class is not supposed to be instantiated
    }
   
    public static String getLoginName() {
    	SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    if (authentication != null) {
	    	return authentication.getName();
	    }
	    else {
	    	throw new InsufficientAuthenticationException("Authentication required");
	    }
    }

    public static String getAuditUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        else {
            return CCWSV3Constant.SYSTEM_USER;
        }
    }
    
    public static Principal getPrinciple() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (Principal) authentication.getPrincipal();
        }
        else {
            throw new InsufficientAuthenticationException("Authentication required");
        }
    }

    public static List<String> getScopes() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
            		.map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        }
        else {
            throw new InsufficientAuthenticationException("Authentication required");
        }
    }
    
    public static boolean hasScope(final String scope) {
    	return getScopes().contains(scope);
    }

}
