package com.cadavre.APIcon.exception;

/**
 * Authorization was required during request but no ApiServerAuthorization was able to fulfill it.
 */
public class UserAuthorizationRequiredException extends RuntimeException {

    public UserAuthorizationRequiredException() {

        super("Current user needs to be reauthorized by ApiServer.");
    }
}
