package com.cadavre.APIcon.examples;

import com.cadavre.APIcon.annotation.Authorization;
import com.cadavre.APIcon.annotation.Cache;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public interface TestService {

    @Cache
    @Authorization
    @GET("/s/game")
    void getGame(
            Callback<User> cb
    );

    @Cache
    @Authorization
    @GET("/s/score/{leaderboardId}/{type}")
    void getScore(
            @Path("leaderboardId") int leaderboardId,
            @Path("type") String type,
            Callback<User> cb
    );

    @Cache
    @POST("/s/score/add")
    void putScore(
            @Body int value,
            Callback<User> cb
    );
}
