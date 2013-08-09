package com.cadavre.APIcon.activities;

import com.cadavre.APIcon.annotation.Authorization;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

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

    @Authorization
    @GET("/s/score/{leaderboardId}/{type}")
    void getScore(
        @Path("leaderboardId") int leaderboardId,
        @Path("type") String type,
        Callback<User> cb
    );
}
