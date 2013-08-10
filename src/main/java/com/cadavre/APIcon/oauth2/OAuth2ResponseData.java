package com.cadavre.APIcon.oauth2;

/**
 * Object representing OAuth2 server response for tokens obtaining endpoint.
 *
 * @author Seweryn Zeman
 * @version 1
 */
final class OAuth2ResponseData {

    private String accessToken;
    private String refreshToken;
    private int expiresIn;

    /**
     * Get access_token.
     *
     * @return String
     */
    public String getAccessToken() {

        return accessToken;
    }

    /**
     * Get refresh_token.
     *
     * @return String
     */
    public String getRefreshToken() {

        return refreshToken;
    }

    /**
     * Get lifetime of access_token. In seconds.
     *
     * @return int
     */
    public int getExpiresIn() {

        return expiresIn;
    }
}
