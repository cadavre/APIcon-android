package com.cadavre.APIcon.annotation;

/**
 * Authorization annotation for Retrofit APIcon extension.
 *
 * @author Seweryn Zeman
 * @version 1
 */
public @interface Authorization {

    public static final int OAUTH2 = 0;

    int value() default OAUTH2;
}
