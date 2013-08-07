package com.cadavre.APIcon;

import retrofit.RequestInterceptor;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 * @version todo
 */
public class OAuth2Interceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {

        // always need to add this header due to stateless OAuth2 authorization nature
        request.addHeader("Connection", "Close");

        // add authorization header with Bearer if necessary
        // request.addHeader("Authorization",
        //        "Bearer NjcyZWExZjVkZTgzZTA2NzdmYjJmMTViMDkyOWM2NWY4OWMyMmExYzVkZDRjZGI4MTkwMzZkMjMxODcwNDlkNg");
        // tutaj można sprawdzić czy Bearer jest już przeterminowany (np. z Prefsów) i ustawić jakiś
        // charakterystyczny header pokroju X-Expired: true

        /*
        if (prefs.getAccessToken == null) {
            // nie ma w ogóle tokenów
            addHeader("X-OAuth2-Unauthorized", "Wstępna autoryzacja wymagana");
        } else if (prefs.isRefreshTokenExpired()) {
            // tokeny są, ale nawet refresh jest przeterminowany
            addHeader("X-OAuth2-Unauthorized", "Wstępna autoryzacja wymagana");
        } else if (prefs.isAccessTokenExpired()) {
            // token access jest przeterminowany, ale z refresha można pozyskać nowego
            addHeader("X-OAuth2-Unauthorized", "Pobierz access_token z refresh_token'a");
        }
        */
    }
}
