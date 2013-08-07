package com.cadavre.APIcon.activities;

import com.cadavre.APIcon.OAuth2Data;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public class SonabisService {

    static final String BASE_URL = "http://api.sonabis.com/app_dev.php";

    static final String AUTH_URL = "/oauth/v2";

    public interface API {

        @GET("/s/game")
        void getGame(
                Callback<User> cb
        );
    }

    public interface OAuth2 {

        @GET(AUTH_URL + "/token")
        void getTokenWithUserCredentials(
                @Query("client_id") String appId,
                @Query("client_secret") String appSecret,
                @Query("grant_type") String grantType,
                @Query("username") String username,
                @Query("password") String password,
                Callback<OAuth2Data> cb
        );
    }
}
