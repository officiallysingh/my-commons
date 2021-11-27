package com.clearcaptions.ccwsv3.common.boot.config.security;

public interface SecurityConstant {
    
    String USERNAME_CLAIM_KEY = "username";

    String SCOPE_CLAIM_KEY = "scope";

    String HAS_AUTHORITY = "hasAuthority";
    
    String HAS_AUTHORITY_REMPLATE = HAS_AUTHORITY + "('%s')";

    String ALL_ACCESS_SCOPE = "no_scope";
    
}
