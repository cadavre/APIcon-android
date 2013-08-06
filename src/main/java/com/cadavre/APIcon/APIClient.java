package com.cadavre.APIcon;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

import java.util.List;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public class APIClient {

    static final String API_URL = "http://api.sonabis.com";

    interface API {

        @GET("/{id}/{type}")
        void getSomething(
                @Path("id") int id,
                @Path("type") String type,
                Callback<List<?>> cb
        );
    }
}
