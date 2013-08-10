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

    /**
     * Authorization to server needs only client_id and client_secret.
     */
    public static final int GRANT_CLIENT_CREDENTIALS = 0;

    /**
     * Authorization to server needs user to pass his username and password.
     */
    public static final int GRANT_USER_CREDENTIALS = 1;

    /**
     * Default lifetime of refresh_token. In seconds.
     */
    public static final int DEFAULT_REFRESH_TOKEN_LIFETIME = 1209600;

    private OAuth2Service authService;
    private OAuth2Helper helper;

    private int refreshTokenLifetime = DEFAULT_REFRESH_TOKEN_LIFETIME;
    private String authorizationEndpoint;
    private int grantType;

    public OAuth2ServerAuthorization(Context context, String authorizationEndpoint,
                                     int grantType, String appId, String appSecret) {

        // trim trailing slash
        this.authorizationEndpoint = authorizationEndpoint.replaceFirst("/$", "");
        this.grantType = grantType;

        // initialize OAuth2 helper
        helper = new OAuth2Helper(context, appId, appSecret, refreshTokenLifetime);
    }

    public OAuth2ServerAuthorization setRefreshTokenLifetime(int refreshTokenLifetime) {

        this.refreshTokenLifetime = refreshTokenLifetime;

        return this;
    }

    @Override
    public void initialize(String baseUrl) {

        authService = new ApiServer(baseUrl + authorizationEndpoint).getService(OAuth2Service.class);
    }

    @Override
    public boolean isAuthDataSufficient() {

        if (!helper.isOAuth2DataAvailable() || helper.isRefreshTokenExpired()) {
            return false;
        } else if (helper.isAccessTokenExpired()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean tryRenewAuthData() {

        OAuth2ResponseData responseData;
        if (!helper.isOAuth2DataAvailable() || helper.isRefreshTokenExpired()) {
            if (grantType == GRANT_CLIENT_CREDENTIALS) {
                responseData = getService().getTokensWithClientCredentials(helper.getAppId(), helper.getAppSecret());

                return helper.setResponseData(responseData);
            } else if (grantType == GRANT_USER_CREDENTIALS) {
                return false;
            }
        } else if (helper.isAccessTokenExpired()) {
            responseData = getService().authorizeWithRefreshToken(
                helper.getAppId(), helper.getAppSecret(), helper.getRefreshToken()
            );

            return helper.setResponseData(responseData);
        }

        return false;
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
