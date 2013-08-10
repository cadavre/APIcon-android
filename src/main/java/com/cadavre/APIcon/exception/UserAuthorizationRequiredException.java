package com.cadavre.APIcon.exception;

/**
 * User needs to authorize to server.
 */
public class UserAuthorizationRequiredException extends RuntimeException {

    public UserAuthorizationRequiredException() {

        super("Current user needs to be authorized by ApiServer.");
    }

    public UserAuthorizationRequiredException(String msg) {

        super(msg);
    }
}
