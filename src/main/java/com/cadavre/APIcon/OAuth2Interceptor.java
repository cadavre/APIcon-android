package com.cadavre.APIcon;

import retrofit.RequestInterceptor;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 * @version todo
 */
public class OAuth2Interceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {

        // always need to add this header due to stateless OAuth2 authorization nature
        request.addHeader("Connection", "Close");

        // add authorization header with Bearer if necessary
        // request.addHeader("Authorization", "Bearer NjcyZWExZjVkZTgzZTA2NzdmYjJmMTViMDkyOWM2NWY4OWMyMmExYzVkZDRjZGI4MTkwMzZkMjMxODcwNDlkNg");
    }
}
