package com.cadavre.APIcon;

import retrofit.RequestInterceptor;

/**
 * This simple interceptor adds only Connection: Close header to every request.
 *
 * @author Seweryn Zeman
 * @version 1
 */
final class RestStatelessInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {

        // always need to add this header due to stateless OAuth2 authorization nature
        request.addHeader("Connection", "Close");
    }
}
