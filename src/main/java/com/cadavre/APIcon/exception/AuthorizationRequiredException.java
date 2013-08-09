package com.cadavre.APIcon.exception;

/**
 * Authorization was required during request but no ApiServerAuthorization was able to fulfill it.
 */
public class AuthorizationRequiredException extends RuntimeException {

    public AuthorizationRequiredException() {

        super("Requested API endpoint claims to need authorization but current ApiServer settings cannot fulfill " +
            "authorization requirements. Using @Authorization and not set ApiServerAuthorization?");
    }
}
