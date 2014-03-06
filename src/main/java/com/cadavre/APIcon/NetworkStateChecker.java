/**
 * All rights reserved to Robospice project.
 */

package com.cadavre.APIcon;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Consider network to be available when one network connection is up, whatever
 * it is. This class will also ensure that both:
 * - android.permission.ACCESS_NETWORK_STATE
 * - android.permission.ACCESS_NETWORK
 * are granted.
 *
 * @author sni
 */
public class NetworkStateChecker {

    private Context context;

    /**
     * Default constructor.
     *
     * @param context
     */
    public NetworkStateChecker(Context context) {

        this.context = context;
    }

    /**
     * Determine whether network is available or not.
     *
     * @return boolean
     */
    public boolean isNetworkAvailable() {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo[] allNetworkInfos = connectivityManager
                .getAllNetworkInfo();
        for (final NetworkInfo networkInfo : allNetworkInfos) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED
                    || networkInfo.getState() == NetworkInfo.State.CONNECTING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if all permissions necessary to determine network state and use
     * network are granted to a given context.
     */
    public void checkPermissions() {

        checkHasPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        checkHasPermission(Manifest.permission.INTERNET);
    }

    /**
     * Check if has permission.
     *
     * @param permissionName
     *
     * @return boolean
     */
    private boolean checkHasPermission(final String permissionName) {

        final boolean hasPermission = context.getPackageManager()
                .checkPermission(permissionName, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (!hasPermission) {
            throw new SecurityException(
                    "Application doesn\'t declare <uses-permission android:name=\""
                            + permissionName + "\" />");
        }
        return true;
    }
}