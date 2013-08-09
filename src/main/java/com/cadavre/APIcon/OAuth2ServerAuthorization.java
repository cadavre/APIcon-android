package com.cadavre.APIcon;

import android.content.Context;

/**
 * OAuth2 server authorization implementation.
 *
 * @author Seweryn Zeman
 * @version 1
 *          todo take care of this shitty initialization()
 */
public final class OAuth2ServerAuthorization implements ApiServerAuthorization {

    private OAuth2Service authService;
    private OAuth2Helper helper;

    private String authorizationEndpoint;

    public OAuth2ServerAuthorization(Context context, String authorizationEndpoint, String appId, String appSecret) {

        this.authorizationEndpoint = authorizationEndpoint;

        // initialize OAuth2 helper
        helper = new OAuth2Helper(appId, appSecret);
        helper.getFromPrefs(OAuth2Helper.getDefaultSharedPrefs(context));
    }

    @Override
    public void initialize(String baseUrl) {

        authService = new ApiServer(baseUrl + authorizationEndpoint).getService(OAuth2Service.class);
    }

    @Override
    public boolean canTryDirectRequest() {

        if (!helper.isOAuth2DataAvailable() || helper.isRefreshTokenExpired()) {
            return false;
        } else if (helper.isAccessTokenExpired()) {
            return false;
        }

        return true;
    }

    @Override
    public OAuth2Service getService() {

        return authService;
    }

    @Override
    public String getAuthorizationEndpoint() {

        return authorizationEndpoint;
    }

    @Override
    public String getHeaderName() {

        return "Authorization";
    }

    @Override
    public String getHeaderValue() {

        return "Bearer " + helper.getAccessToken();
    }
}
