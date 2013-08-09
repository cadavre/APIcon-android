package com.cadavre.APIcon;

import retrofit.RetrofitError;
import retrofit.client.Header;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

/**
 * APIcon class
 *
 * @author Seweryn Zeman
 * @version todo
 */
public class RestErrorHandler implements retrofit.ErrorHandler {

    @Override
    public Throwable handleError(RetrofitError error) {

        if (error.getCause() != null) {
            Logger.e("-- Error detected: " + error.getMessage(), error);
        } else {
            Logger.e("-- Error detected: " + error.getMessage());
        }
        Logger.i("-- isOAuth2Error: " + isOAuth2Error(error));

        return error;
    }

    /* OAuth2 fun below */

    private boolean isResolvableOAuth2Error(RetrofitError error) {

        if (isOAuth2Error(error)) {

        }

        return false;
    }

    /**
     * Check if RetrofitError is OAuth2 error.
     *
     * @param error
     *
     * @return true if is OAuth2 error
     */
    private boolean isOAuth2Error(RetrofitError error) {

        int responseCode = error.getResponse().getStatus();
        if (responseCode == HTTP_UNAUTHORIZED) {
            AuthenticationRealm realm = parseHeaders(error.getResponse().getHeaders(), "WWW-Authenticate");
            return isOAuth2Realm(realm);
        }

        return false;
    }

    /**
     * Parse headers to get AuthenticationRealm.
     *
     * @param responseHeaders
     * @param challengeHeader
     *
     * @return AuthenticationRealm
     */
    private AuthenticationRealm parseHeaders(List<Header> responseHeaders, String challengeHeader) {

        AuthenticationRealm result = null;
        for (int h = 0; h < responseHeaders.size(); h++) {
            Header header = responseHeaders.get(h);
            if (!challengeHeader.equalsIgnoreCase(header.getName())) {
                continue;
            }
            String value = header.getValue();
            int pos = 0;
            while (pos < value.length()) {
                int tokenStart = pos;
                pos = skipUntil(value, pos, " ");

                String scheme = value.substring(tokenStart, pos).trim();
                pos = skipWhitespace(value, pos);

                if (!value.regionMatches(pos, "realm=\"", 0, "realm=\"".length())) {
                    break; // unexpected challenge parameter
                }

                pos += "realm=\"".length();
                int realmStart = pos;
                pos = skipUntil(value, pos, "\"");
                String realm = value.substring(realmStart, pos);
                pos++; // Consume '"' close quote.
                pos = skipUntil(value, pos, ",");
                pos++; // Consume ',' comma.
                pos = skipWhitespace(value, pos);

                if (!value.regionMatches(pos, "error=\"", 0, "error=\"".length())) {
                    break; // unexpected challenge parameter
                }

                pos += "error=\"".length();
                int errorStart = pos;
                pos = skipUntil(value, pos, "\"");
                String error = value.substring(errorStart, pos);
                pos++; // Consume '"' close quote.
                pos = skipUntil(value, pos, ",");
                pos++; // Consume ',' comma.
                pos = skipWhitespace(value, pos);
                result = new AuthenticationRealm(scheme, realm, error);
            }
        }

        return result;
    }

    /**
     * Check if realm is OAuth2 realm.
     *
     * @param realm
     *
     * @return true if is OAuth2 realm
     */
    private boolean isOAuth2Realm(AuthenticationRealm realm) {

        return realm != null && realm.getRealm().equals("Service") && realm.getScheme().equals("Bearer");
    }

    /**
     * @see {HeaderParser}
     */
    private static int skipUntil(String input, int pos, String characters) {

        for (; pos < input.length(); pos++) {
            if (characters.indexOf(input.charAt(pos)) != -1) {
                break;
            }
        }
        return pos;
    }

    /**
     * @see {HeaderParser}
     */
    private static int skipWhitespace(String input, int pos) {

        for (; pos < input.length(); pos++) {
            char c = input.charAt(pos);
            if (c != ' ' && c != '\t') {
                break;
            }
        }
        return pos;
    }

    /**
     * Extracted "WWW-Authenticate" header data.
     */
    private class AuthenticationRealm {

        private String scheme;

        private String realm;

        private String error;

        AuthenticationRealm(String scheme, String realm, String error) {

            this.scheme = scheme;
            this.realm = realm;
            this.error = error;
        }

        /**
         * Get scheme.
         *
         * @return String
         */
        private String getScheme() {

            return scheme;
        }

        /**
         * Get realm.
         *
         * @return String
         */
        private String getRealm() {

            return realm;
        }

        /**
         * Get error short token.
         *
         * @return String
         */
        private String getError() {

            return error;
        }
    }
}
