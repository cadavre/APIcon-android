package com.cadavre.APIcon.exception;

/**
 * APIcon was not initialized properly.
 */
public class ApiNotInitializedException extends RuntimeException {

    public ApiNotInitializedException() {

        super("APIcon was not initialized or something went wrong during initialization.");
    }
}
