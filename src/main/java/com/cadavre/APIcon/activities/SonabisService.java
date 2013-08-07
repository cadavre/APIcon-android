package com.cadavre.APIcon.activities;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public class SonabisService {

    static final String API_URL = "http://192.168.0.104/app_dev.php";

    interface API {

        @GET("/s/game")
        void getGame(
                Callback<User> cb
        );
    }
}
