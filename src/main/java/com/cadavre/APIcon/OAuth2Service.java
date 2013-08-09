package com.cadavre.APIcon;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Retrofit service interface for OAuth2.
 *
 * @author Seweryn Zeman
 * @version 1
 *          todo get url endpoints from APIcon
 */
interface OAuth2Service {

    @GET("/token?grant_type=password")
    void getTokensWithUserCredentials(
            @Query("client_id") String appId,
            @Query("client_secret") String appSecret,
            @Query("username") String username,
            @Query("password") String password,
            Callback<OAuth2ResponseData> cb
    );

    @GET("/token?grant_type=client_credentials")
    void getTokensWithClientCredentials(
            @Query("client_id") String appId,
            @Query("client_secret") String appSecret,
            Callback<OAuth2ResponseData> cb
    );

    @GET("/token?grant_type=refresh_token")
    void authorizeWithRefreshToken(
            @Query("client_id") String appId,
            @Query("client_secret") String appSecret,
            @Query("refresh_token") String refreshToken,
            Callback<OAuth2ResponseData> cb
    );
}
