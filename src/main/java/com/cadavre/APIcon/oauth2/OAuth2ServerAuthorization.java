package com.cadavre.APIcon.oauth2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.cadavre.APIcon.ApiServer;
import com.cadavre.APIcon.ApiServerAuthorization;
import com.cadavre.APIcon.OnUserAuthorizationListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * OAuth2 server authorization implementation.
 *
 * @author Seweryn Zeman
 * @version 1
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

    private OnUserAuthorizationListener userAuthListener;

    public OAuth2ServerAuthorization(Context context, String authorizationEndpoint,
                                     int grantType, String appId, String appSecret) {

        // trim trailing slash
        this.authorizationEndpoint = authorizationEndpoint.replaceFirst("/$", "");
        this.grantType = grantType;

        // initialize OAuth2 helper
        helper = new OAuth2Helper(context.getApplicationContext(), appId, appSecret, refreshTokenLifetime);
    }

    /**
     * Set server value for refresh token expiration.
     *
     * @param refreshTokenLifetime
     *
     * @return OAuth2ServerAuthorization
     */
    public OAuth2ServerAuthorization setRefreshTokenLifetime(int refreshTokenLifetime) {

        this.refreshTokenLifetime = refreshTokenLifetime;

        return this;
    }

    /**
     * Set listener used when user interaction is needed to authorize.
     * For now - supported with GRANT_USER_CREDENTIALS. In this case
     * onAuthorizationRequired() method MUST return a Bundle object
     * with username and password:
     * `username` (String)
     * `password` (String)
     *
     * @param listener
     */
    public void setOnUserAuthorizationListener(OnUserAuthorizationListener listener) {

        this.userAuthListener = listener;
        this.userAuthListener.setAuthorization(this);
    }

    /* * Internal methods below * */

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
                if (userAuthListener != null) {
                    runOnUiThread(new Runnable() {

                        public void run() {

                            userAuthListener.onAuthorizationRequired();
                        }
                    });
                }

                return false;
            }
        } else if (helper.isAccessTokenExpired()) {
            responseData = getService().getTokensWithRefreshToken(
                    helper.getAppId(), helper.getAppSecret(), helper.getRefreshToken()
            );

            return helper.setResponseData(responseData);
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * Bundle for this authorization must contain two string extras:
     * - username
     * - password
     *
     * @param params
     */
    @Override
    public void tryRenewAuthDataWithUserInteraction(Bundle params) {

        if (grantType == GRANT_USER_CREDENTIALS) {
            getService().getTokensWithUserCredentials(
                    helper.getAppId(), helper.getAppSecret(),
                    params.getString("username"), params.getString("password"),
                    new Callback<OAuth2ResponseData>() {

                        @Override
                        public void success(OAuth2ResponseData responseData, Response response) {

                            helper.setResponseData(responseData);
                            if (userAuthListener != null) {
                                userAuthListener.onSuccess();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                            if (userAuthListener != null) {
                                userAuthListener.onFailure();
                            }
                        }
                    }
            );
        }
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

    /**
     * Private helper to run action on UI thread without access to Activity context.
     *
     * @param action
     */
    private void runOnUiThread(final Runnable action) {

        final Handler mHandler = new Handler(Looper.getMainLooper());
        new Thread() {

            public void run() {

                mHandler.post(action);
            }
        }.start();
    }
}
