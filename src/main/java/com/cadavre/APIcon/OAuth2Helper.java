package com.cadavre.APIcon;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

/**
 * OAuth2 helper class for maintaining application-layer data.
 *
 * @author Seweryn Zeman
 * @version 1
 */
public final class OAuth2Helper {

    public static final String SHARED_PREFS_NAME = "authorization_data";

    public static final String ACCESS_TOKEN_FIELD = "access_token";
    public static final String REFRESH_TOKEN_FIELD = "refresh_token";
    public static final String ACCESS_TOKEN_EXPIRATION_FIELD = "access_token_expiration";
    public static final String REFRESH_TOKEN_EXPIRATION_FIELD = "refresh_token_expiration";

    private String appId;
    private String appSecret;

    private String accessToken;
    private String refreshToken;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;

    public OAuth2Helper(String appId, String appSecret) {

        this.appId = appId;
        this.appSecret = appSecret;
    }

    public static boolean isOAuthError(String errorMessage) {

        String[] oauthErrors = {
                // fetching access token
                "invalid_client",           // wrong client_id/client_secret
                "invalid_request",          // params missing
                "invalid_grant",            // wrong refresh_token
                "unsupported_grant_type",   // wrong grant_type
                // secured resources access
                "invalid_grant",            // wrong access_token, expired...
                "access_denied"             // user with weak ROLE
        };

        return Arrays.asList(oauthErrors).contains(errorMessage);
    }

    /**
     * Get or create if not exist a SharedPreferences by filename.
     *
     * @param context
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPrefs(Context context) {

        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save current OAuth2Helper state to SharedPreferences.
     *
     * @param preferences
     *
     * @return true if success, false if failure
     */
    public boolean saveToPrefs(SharedPreferences preferences) {

        SharedPreferences.Editor editor = preferences.edit();
        editor
                .putString(ACCESS_TOKEN_FIELD, accessToken)
                .putString(REFRESH_TOKEN_FIELD, refreshToken)
                .putLong(ACCESS_TOKEN_EXPIRATION_FIELD, accessTokenExpiration)
                .putLong(REFRESH_TOKEN_EXPIRATION_FIELD, refreshTokenExpiration);

        return editor.commit();
    }

    /**
     * Get OAuth2Helper state from SharedPreferences.
     *
     * @param preferences
     */
    public void getFromPrefs(SharedPreferences preferences) {

        accessToken = preferences.getString(ACCESS_TOKEN_FIELD, "");
        refreshToken = preferences.getString(REFRESH_TOKEN_FIELD, "");
        accessTokenExpiration = preferences.getLong(ACCESS_TOKEN_EXPIRATION_FIELD, System.currentTimeMillis());
        refreshTokenExpiration = preferences.getLong(REFRESH_TOKEN_EXPIRATION_FIELD, System.currentTimeMillis());
    }

    /**
     * Set all data received by default from server.
     *
     * @param accessToken
     * @param refreshToken
     * @param accessTokenLifetime
     * @param refreshTokenLifetime
     */
    public void setReceivedData(String accessToken, String refreshToken, int accessTokenLifetime, int refreshTokenLifetime) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        setAccessTokenLifetime(accessTokenLifetime);
        setRefreshTokenLifetime(refreshTokenLifetime);
    }

    /**
     * Check if access token is already expired.
     *
     * @return true if is expires, false otherwise
     */
    public boolean isAccessTokenExpired() {

        return System.currentTimeMillis() < accessTokenExpiration;
    }

    /**
     * Check if refresh token is already expired.
     *
     * @return true if is expires, false otherwise
     */
    public boolean isRefreshTokenExpired() {

        return System.currentTimeMillis() < refreshTokenExpiration;
    }

    /**
     * Get OAuth2 application ID.
     *
     * @return String
     */
    public String getAppId() {

        return appId;
    }

    /**
     * Get OAuth2 application secret token.
     *
     * @return String
     */
    public String getAppSecret() {

        return appSecret;
    }

    /**
     * Get OAuth2 access token called Bearer.
     *
     * @return String
     */
    public String getAccessToken() {

        return accessToken;
    }

    /**
     * Set OAuth2 access token called Bearer.
     *
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {

        this.accessToken = accessToken;
    }

    /**
     * Get OAuth2 refresh token.
     *
     * @return String
     */
    public String getRefreshToken() {

        return refreshToken;
    }

    /**
     * Set OAuth2 refresh token.
     *
     * @param refreshToken
     */
    public void setRefreshToken(String refreshToken) {

        this.refreshToken = refreshToken;
    }

    /**
     * Get time of access token (Bearer) expiration.
     *
     * @return long
     */
    public long getAccessTokenExpiration() {

        return accessTokenExpiration;
    }

    /**
     * Set time of access token (Bearer) expiration.
     *
     * @param accessTokenExpiration
     */
    public void setAccessTokenExpiration(long accessTokenExpiration) {

        this.accessTokenExpiration = accessTokenExpiration;
    }

    /**
     * Set lifetime of access token (Bearer).
     *
     * @param lifetime in seconds
     */
    public void setAccessTokenLifetime(int lifetime) {

        this.accessTokenExpiration = System.currentTimeMillis() + (lifetime * 1000);
    }

    /**
     * Get time of refresh token expiration.
     *
     * @return long
     */
    public long getRefreshTokenExpiration() {

        return refreshTokenExpiration;
    }

    /**
     * Set time of refresh token expiration.
     *
     * @param refreshTokenExpiration
     */
    public void setRefreshTokenExpiration(long refreshTokenExpiration) {

        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Set lifetime of refresh token.
     *
     * @param lifetime in seconds
     */
    public void setRefreshTokenLifetime(int lifetime) {

        this.refreshTokenExpiration = System.currentTimeMillis() + (lifetime * 1000);
    }
}
