package com.cadavre.APIcon;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
interface ApiServerAuthorization {

    /**
     * Initialize authorizator if needed.
     * For example: create Retrofit service to use network communication.
     *
     * @param baseUrl
     */
    void initialize(String baseUrl);

    /**
     * If initialized and created Retrofit service - get it.
     * Notice: create service in initialize();
     *
     * @param <T>
     *
     * @return
     */
    <T> T getService();

    /**
     * Check if current auth data should be enough to send successful request.
     *
     * @return false if authorization will certainly fail, true otherwise
     */
    public boolean isAuthDataSufficient();

    /**
     * If auth data is for sure not sufficient to make successful request - try to obtain new data.
     *
     * @return true if new obtained auth data is fresh, false otherwise
     */
    public boolean tryRenewAuthData();

    /**
     * Get endpoint for obtaining new, fresh authorization data like OAuth2 tokens.
     *
     * @return String
     */
    public String getAuthorizationEndpoint();

    /**
     * Get name for authorization header.
     *
     * @return String
     */
    public String getHeaderName();

    /**
     * Get value for authorization header.
     *
     * @return String
     */
    public String getHeaderValue();
}
