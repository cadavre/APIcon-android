package com.cadavre.APIcon;

import retrofit.RetrofitError;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public class ApiErrorHandler implements retrofit.ErrorHandler {

    @Override
    public Throwable handleError(RetrofitError cause) {

        return new Exception(cause.getMessage());
    }
}
