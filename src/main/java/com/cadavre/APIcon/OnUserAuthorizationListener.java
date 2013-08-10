package com.cadavre.APIcon;

import android.os.Bundle;

/**
 * Listener for providing user interaction if needed in authorization.
 * Basically:
 * - implement onAuthorizationRequired() that can run on UI thread, show some dialog or something
 * - in onAuthorizationRequired() call processAuthorization() with user parameters Bundle
 * - setAuthorization() is called automatically
 * - implement onSuccess() and onFailure() to get to know how authorization process ended
 *
 * @author Seweryn Zeman
 * @version 1
 */
public abstract class OnUserAuthorizationListener {

    ApiServerAuthorization authorization;

    /**
     * Called if need user interaction in authorization.
     */
    public abstract void onAuthorizationRequired();

    /**
     * Call this method in onAuthorizationRequired(); with params
     * passed through Bundle. This method actually processes the authorization.
     *
     * @param params
     */
    public void processAuthorization(Bundle params) {

        this.authorization.tryRenewAuthDataWithUserInteraction(params);
    }

    /**
     * Set ApiServerAuthorization to perform processing.
     *
     * @param authorization
     */
    public void setAuthorization(ApiServerAuthorization authorization) {

        this.authorization = authorization;
    }

    /**
     * On authorization success.
     */
    public abstract void onSuccess();

    /**
     * On authorization failure.
     */
    public abstract void onFailure();
}
