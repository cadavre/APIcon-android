package com.cadavre.APIcon;

import android.util.Log;
import retrofit.RestAdapter;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 */
public class Logger implements RestAdapter.Log {

    @Override
    public void log(String message) {

        Log.d("APIcon", message);
    }
}
