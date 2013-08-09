package com.cadavre.APIcon;

import android.util.Log;
import retrofit.RestAdapter;

/**
 * Basic Android Log wrapper to user always TAG tag.
 *
 * @author Seweryn Zeman
 * @version 1
 */
final class Logger implements RestAdapter.Log {

    static final String TAG = "APIcon";

    public Logger() {

        Logger.i("Using custom Logger");
    }

    @Override
    public void log(String message) {

        Log.d(TAG, message);
    }

    /**
     * Log information message.
     *
     * @param message
     */
    public static void i(String message) {

        Log.i(TAG, message);
    }

    /**
     * Log warning message.
     *
     * @param message
     */
    public static void w(String message) {

        Log.w(TAG, message);
    }

    /**
     * Log error message.
     *
     * @param message
     */
    public static void e(String message) {

        Log.e(TAG, message);
    }

    /**
     * Log error message and any Throwable.
     *
     * @param message
     * @param e
     */
    public static void e(String message, Throwable e) {

        Log.e(TAG, message, e);
    }
}
