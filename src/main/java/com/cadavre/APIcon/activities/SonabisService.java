package com.cadavre.APIcon.activities;

import com.cadavre.APIcon.annotation.Authorization;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public interface SonabisService {

    @Authorization
    @GET("/s/game")
    void getGame(
            Callback<User> cb
    );
}
