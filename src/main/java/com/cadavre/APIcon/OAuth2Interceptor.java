package com.cadavre.APIcon;

import retrofit.RequestInterceptor;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 * @version todo
 */
public class OAuth2Interceptor implements RequestInterceptor {

    private OAuth2Helper oAuth2Helper;

    public OAuth2Interceptor() {

        this.oAuth2Helper = APIcon.getInstance().getOAuth2Helper();
    }

    @Override
    public void intercept(RequestFacade request) {

        // always need to add this header due to stateless OAuth2 authorization nature
        request.addHeader("Connection", "Close");

        // check for OAuth2 availability and set proper headers
        if (!oAuth2Helper.isOAuth2DataAvailable() || oAuth2Helper.isRefreshTokenExpired()) {
            request.addHeader("X-OAuth2-Helper", "Reauthorize");
        } else if (oAuth2Helper.isAccessTokenExpired()) {
            request.addHeader("X-OAuth2-Helper", "Refresh");
        } else {
            // todo check if endpoint needs authorization
            request.addHeader("Authorization", "Bearer " + oAuth2Helper.getAccessToken());
        }
    }
}
