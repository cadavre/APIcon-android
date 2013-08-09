package com.cadavre.APIcon;

import android.content.Context;

/**
 * OAuth2 server authorization implementation.
 *
 * @author Seweryn Zeman
 * @version 1
 * todo take care of this shitty initialization()
 */
public final class OAuth2ServerAuthorization implements ApiServerAuthorization {

    private OAuth2Service authService;

    private OAuth2Helper helper;

    private String authEndpoint;
    private String tokenEndpoint;

    public OAuth2ServerAuthorization(Context context, String appId, String appSecret) {

        // initialize OAuth2 helper
        helper = new OAuth2Helper(appId, appSecret);
        helper.getFromPrefs(OAuth2Helper.getDefaultSharedPrefs(context));
    }

    /**
     * Set authorization endpoints. Auth endpoint may refer to null.
     *
     * @param tokenEndpoint
     * @param authEndpoint
     */
    public void setEndpoints(String tokenEndpoint, String authEndpoint) {

        this.authEndpoint = authEndpoint;
        this.tokenEndpoint = tokenEndpoint;
    }

    @Override
    public void initialize(String baseUrl) {

        authService = new ApiServer(baseUrl).getService(OAuth2Service.class);
    }

    @Override
    public OAuth2Service getService() {

        return authService;
    }

    @Override
    public String getAuthEndpoint() {

        return authEndpoint;
    }

    @Override
    public String getTokenEndpoint() {

        return tokenEndpoint;
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
