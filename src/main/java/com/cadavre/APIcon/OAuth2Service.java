package com.cadavre.APIcon;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Retrofit service interface for OAuth2.
 *
 * @author Seweryn Zeman
 * @version 1
 */
interface OAuth2Service {

    @GET("/token?grant_type=password")
    OAuth2ResponseData getTokensWithUserCredentials(
        @Query("client_id") String appId,
        @Query("client_secret") String appSecret,
        @Query("username") String username,
        @Query("password") String password
    );

    @GET("/token?grant_type=client_credentials")
    OAuth2ResponseData getTokensWithClientCredentials(
        @Query("client_id") String appId,
        @Query("client_secret") String appSecret
    );

    @GET("/token?grant_type=refresh_token")
    OAuth2ResponseData authorizeWithRefreshToken(
        @Query("client_id") String appId,
        @Query("client_secret") String appSecret,
        @Query("refresh_token") String refreshToken
    );
}
