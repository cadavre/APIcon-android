package com.cadavre.APIcon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Authorization annotation for Retrofit APIcon extension.
 *
 * @author Seweryn Zeman
 * @version 1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Authorization {

    public static final String OAUTH2 = "OAuth2";

    String value() default OAUTH2;
}
