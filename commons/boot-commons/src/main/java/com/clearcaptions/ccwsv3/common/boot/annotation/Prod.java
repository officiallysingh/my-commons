package com.clearcaptions.ccwsv3.common.boot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Profile;

import com.clearcaptions.ccwsv3.common.boot.config.SpringProfiles;

/**
 * @author Rajveer Singh
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Profile(SpringProfiles.PRODUCTION)
public @interface Prod {

}
