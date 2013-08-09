package com.cadavre.APIcon;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
interface ApiServerAuthorization {

    void initialize(String baseUrl);
    <T> T getService();
    public String getAuthEndpoint();
    public String getTokenEndpoint();
    public String getHeaderName();
    public String getHeaderValue();
}
