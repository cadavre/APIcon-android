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
     * Log debug message.
     * Notice: debug messages are NOT logged if BuildConfig.DEBUG is false.
     *
     * @param message
     */
    public static void d(String message) {

        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
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

    /**
     * Log type, class occurrence and line number of thrown exception that don't need to be catched.
     *
     * @param e
     */
    public static void dummyException(Throwable e) {

        StackTraceElement trace = Thread.currentThread().getStackTrace()[3];
        Log.e(TAG, "Dummy exception " + e.getClass().getCanonicalName() + " in " + trace.getClassName() + " at " +
                trace.getFileName());
    }

    /**
     * Log class occurrence and line number of error that don't need to be handled.
     */
    public static void dummyError() {

        StackTraceElement trace = Thread.currentThread().getStackTrace()[3];
        Log.e(TAG, "Dummy error in " + trace.getClassName() + " at " + trace.getFileName());
    }
}
