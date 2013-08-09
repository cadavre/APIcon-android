package com.cadavre.APIcon;

/**
 * APIcon class, takes care of initialization and managing whole API system.
 *
 * @author Seweryn Zeman
 * @version 1
 */
public final class APIcon {

    private static APIcon ourInstance;
    private ApiServer server;

    /**
     * Private constructor to use with singleton pattern.
     *
     * @param server
     */
    private APIcon(ApiServer server) {

        this.server = server;
    }

    /**
     * Initialize APIcon system.
     *
     * @param server
     *
     * @return APIcon
     */
    public static APIcon initialize(ApiServer server) {

        if (ourInstance == null) {

            ourInstance = new APIcon(server);
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
     * Get API service.
     *
     * @param serviceInterface
     * @param <T>
     *
     * @return service
     */
    public <T> T getService(Class<T> serviceInterface) {

        return server.getService(serviceInterface);
    }

    /* package */ ApiServer getServer() {

        return server;
    }
}
