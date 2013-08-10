package com.cadavre.APIcon;

import android.os.Bundle;

/**
 * Interface for creating server authorization handlers.
 * This interface supports:
 * - initialization
 * - providing auth Retrofit service with own ApiServer
 * - auth data renewal based on local informations
 * - auth data renewal based on user interaction thanks to OnUserAuthorizationListener
 * - providing authorization as "Authorization" header
 *
 * @author Seweryn Zeman
 * @version 1
 */
public interface ApiServerAuthorization {

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
     * Notice: this code will run on same Thread as request execution.
     *
     * @return true if new obtained auth data is fresh, false otherwise
     */
    public boolean tryRenewAuthData();

    /**
     * If auth data is for sure not sufficient to make successful request - try to obtain new data.
     * This method is used if user interaction is needed. Results of user interactions are passed through Bundle.
     * Notice: this code will run on UI thread!
     */
    public void tryRenewAuthDataWithUserInteraction(Bundle params);

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
