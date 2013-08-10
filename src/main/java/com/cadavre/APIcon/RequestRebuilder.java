package com.cadavre.APIcon;

import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.mime.TypedOutput;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Builder helper for creating modified requests from already existing one.
 *
 * @author Seweryn Zeman
 * @version 1
 */
final class RequestRebuilder {

    private TypedOutput body;
    private ArrayList<Header> headers;
    private String method;
    private String url;

    /**
     * Create RequestRebuilder based on old request.
     *
     * @param request
     */
    public RequestRebuilder(Request request) {

        this.headers = new ArrayList<Header>();
        this.headers.addAll(request.getHeaders());

        this.body = request.getBody();
        this.method = request.getMethod();
        this.url = request.getUrl();
    }

    /**
     * Add new header. This method doesn't check if header already exists.
     *
     * @param header
     *
     * @return RequestRebuilder this
     */
    public RequestRebuilder addHeader(Header header) {

        this.headers.add(header);

        return this;
    }

    /**
     * Set particular header. If header with same name already existed - it will be overwritten.
     *
     * @param newHeader
     *
     * @return RequestRebuilder this
     */
    public RequestRebuilder setHeader(Header newHeader) {

        for (Header oldHeader : headers) {
            if (oldHeader.getName().equals(newHeader.getName())) {
                headers.remove(oldHeader);
                break;
            }
        }
        headers.add(newHeader);

        return this;
    }

    /**
     * Parse string to URL. Notice: will return null if @{MalformedURLException}.
     *
     * @return URL
     */
    public URL parseUrl() {

        try {
            return new URL(this.url);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Get HTTP method name.
     *
     * @return String
     */
    public String getMethod() {

        return method;
    }

    /**
     * Rebuild original request with modified data.
     *
     * @return Request
     */
    public Request rebuild() {

        return new Request(method, url, headers, body);
    }
}
