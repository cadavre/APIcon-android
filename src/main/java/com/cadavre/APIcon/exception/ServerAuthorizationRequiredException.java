package com.cadavre.APIcon.exception;

/**
 * Authorization was required during request but no ApiServerAuthorization was able to fulfill it.
 */
public class ServerAuthorizationRequiredException extends RuntimeException {

    public ServerAuthorizationRequiredException() {

        super("Requested API endpoint claims to need authorization but current ApiServer settings cannot fulfill " +
            "authorization requirements.");
    }
}
