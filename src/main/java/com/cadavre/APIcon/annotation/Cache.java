package com.cadavre.APIcon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cache request if no connection is available or if timeout occurred.
 * Cache annotation for Retrofit APIcon extension.
 *
 * @author Seweryn Zeman
 * @version 1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Cache {

}
