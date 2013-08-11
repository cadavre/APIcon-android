package com.cadavre.APIcon;

import android.content.Context;
import com.cadavre.APIcon.exception.ApiNotInitializedException;

import java.io.File;

/**
 * APIcon class, takes care of initialization and managing whole API system.
 *
 * @author Seweryn Zeman
 * @version 1
 */
public final class APIcon {

    private static APIcon ourInstance;
    private ApiServer server;
    private NetworkStateChecker networkChecker;

    private File cacheDir;

    /**
     * Private constructor to use with singleton pattern.
     *
     * @param context
     * @param server
     */
    private APIcon(Context context, ApiServer server) {

        this.server = server;
        this.networkChecker = new NetworkStateChecker(context);
        this.cacheDir = context.getCacheDir();

        this.networkChecker.checkPermissions();
    }

    /**
     * Initialize APIcon system.
     *
     * @param context
     * @param server
     *
     * @return APIcon
     */
    public static APIcon initialize(Context context, ApiServer server) {

        if (ourInstance == null) {

            ourInstance = new APIcon(context, server);
        }

        return ourInstance;
    }

    /**
     * Get APIcon instance if initialized.
     *
     * @return APIcon
     */
    public static APIcon getInstance() {

        if (ourInstance == null) {
            throw new ApiNotInitializedException();
        }

        return ourInstance;
    }

    /**
     * Get API service.
     *
     * @param serviceInterface
     * @param <T>
     *
     * @return service
     */
    public static <T> T getService(Class<T> serviceInterface) {

        return getInstance().getServer().getService(serviceInterface);
    }

    /**
     * Get ApiServer binded to APIcon.
     *
     * @return ApiServer
     */
    /* package */ ApiServer getServer() {

        return server;
    }

    /**
     * Get NetworkStateChecker.
     *
     * @return NetworkStateChecker
     */
    /* package */ NetworkStateChecker getNetworkChecker() {

        return networkChecker;
    }

    /**
     * Get cache directory for project package.
     *
     * @return File
     */
    /* package */ File getCacheDir() {

        return cacheDir;
    }
}
