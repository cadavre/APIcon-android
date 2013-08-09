package com.cadavre.APIcon;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit.converter.GsonConverter;

/**
 * GsonConverter adapted to Symfony2 API servers.
 *
 * @author Seweryn Zeman
 * @version 1
 */
final class RestSymfonyGsonConverter extends GsonConverter {

    public RestSymfonyGsonConverter() {

        super(createGson());
    }

    private static Gson createGson() {

        GsonBuilder gson = new GsonBuilder();
        gson.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        gson.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

        return gson.create();
    }
}
