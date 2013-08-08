package com.cadavre.APIcon;

import android.content.Context;
import com.octo.android.robospice.SpiceManager;

/**
 * APIcon class, takes care of initialization and managing whole API system.
 *
 * @author Seweryn Zeman
 */
public class APIcon {

    private static APIcon ourInstance;

    private SpiceManager spiceManager = new SpiceManager(ApiSpiceService.class);
    private OAuth2Helper oAuth2Helper;

    /**
     * Private constructor to use with singleton pattern.
     *
     * @param appId
     * @param appSecret
     * @param context
     */
    private APIcon(String appId, String appSecret, Context context) {

        this.spiceManager.start(context);
        this.oAuth2Helper = new OAuth2Helper(appId, appSecret);
    }

    /**
     * Initialize APIcon system.
     *
     * @param appId
     * @param appSecret
     * @param context
     *
     * @return APIcon
     */
    public static APIcon initialize(String appId, String appSecret, Context context) {

        if (ourInstance == null) {

            ourInstance = new APIcon(appId, appSecret, context);
        }

        return ourInstance;
    }

    /**
     * Get APIcon instance if initialized.
     *
     * @return APIcon
     */
    public static APIcon getInstance() {

        return ourInstance;
    }

    /**
     * Get SpiceManager with binded service.
     *
     * @return APIcon
     */
    public SpiceManager getSpiceManager() {

        return spiceManager;
    }

    /**
     * Get OAuth2Helper.
     *
     * @return OAuth2Helper
     */
    public OAuth2Helper getOAuth2Helper() {

        return oAuth2Helper;
    }
}