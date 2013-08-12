package com.cadavre.APIcon.testy;

import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.mime.TypedOutput;

import java.util.List;

public class CachedRequest {

    private String url;
    private String method;
    private List<Header> headers;
    private TypedOutput body;

    public CachedRequest setRequest(Request request) {

        url = request.getUrl();
        method = request.getMethod();
        headers = request.getHeaders();
        body = request.getBody();

        return this;
    }

    public Request getRequest() {

        return new Request(method, url, headers, body);
    }
}