package com.cadavre.APIcon;

import retrofit.RequestInterceptor;

class OAuth2Interceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {

        request.addHeader("Authorization", "Bearer XXX");
    }
}
