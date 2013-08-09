package com.cadavre.APIcon;

import retrofit.RestAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APIcon main representation of particular server and it's authorization.
 *
 * @author Seweryn Zeman
 * @version 1
 */
public final class ApiServer {

    private String baseUrl;
    private ApiServerAuthorization authorization;

    private RestAdapter restAdapter;
    private List<Class<?>> serviceInterfaces = new ArrayList<Class<?>>();
    private Map<Class<?>, Object> interfaceToServiceMap = new HashMap<Class<?>, Object>();

    /**
     * Simple default constructor.
     *
     * @param baseUrl
     */
    public ApiServer(String baseUrl) {

        this(baseUrl, null);
    }

    /**
     * Default constructor.
     *
     * @param baseUrl
     * @param authorization
     */
    public ApiServer(String baseUrl, ApiServerAuthorization authorization) {

        this.baseUrl = baseUrl;
        this.restAdapter = new RestAdapter.Builder()
                .setLog(new Logger())
                .setDebug(BuildConfig.DEBUG)
                .setServer(baseUrl)
                .setClient(new RestHttpClient())
                .setRequestInterceptor(new RestStatelessInterceptor())
                .setConverter(new RestSymfonyGsonConverter())
                        // .setErrorHandler(new RestErrorHandler()) works only with synchronous Requests
                .build();

        if (authorization != null) {
            authorization.initialize(baseUrl); // todo see{OAuth2ServerAuthorization}
            this.authorization = authorization;
        }
    }

    /**
     * Get base API url. In most cases just like "http://api.example.com".
     *
     * @return String
     */
    public String getBaseUrl() {

        return this.baseUrl;
    }

    /**
     * Add service interface based on Retrofit implementation.
     *
     * @param serviceInterface
     *
     * @return ApiServer
     */
    public ApiServer addServiceInterface(Class<?> serviceInterface) {

        serviceInterfaces.add(serviceInterface);

        return this;
    }

    /**
     * Get authorization handler used to authorize requests to server.
     * If no authorization needed may be null.
     *
     * @return ApiServerAuthorization
     */
    ApiServerAuthorization getAuthorization() {

        return this.authorization;
    }

    /**
     * Get list of all service interfaces.
     *
     * @return List
     */
    List<Class<?>> getServiceInterfaces() {

        return serviceInterfaces;
    }

    /**
     * Get service based on interface.
     *
     * @param serviceInterface
     * @param <T>
     *
     * @return service
     */
    <T> T getService(Class<T> serviceInterface) {

        T service = (T) interfaceToServiceMap.get(serviceInterface);
        if (service == null) {
            service = restAdapter.create(serviceInterface);
            interfaceToServiceMap.put(serviceInterface, service);
        }

        return service;
    }

}
