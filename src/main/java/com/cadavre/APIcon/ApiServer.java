package com.cadavre.APIcon;

import retrofit.RestAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

    private HashMap<Method, Pattern> endpointsRequiringAuthorization = new HashMap<Method, Pattern>();

    /**
     * Default constructor.
     *
     * @param baseUrl
     */
    public ApiServer(String baseUrl) {

        // trim trailing slash
        this.baseUrl = baseUrl.replaceFirst("/$", "");

        // create Retrofit REST adapter
        this.restAdapter = new RestAdapter.Builder()
            .setLog(new Logger())
            .setDebug(BuildConfig.DEBUG)
            .setServer(baseUrl)
            .setClient(new RestHttpClient())
            .setRequestInterceptor(new RestStatelessInterceptor())
            .setConverter(new RestSymfonyGsonConverter())
                // .setErrorHandler(new RestErrorHandler()) works only with synchronous Requests
            .build();
    }

    public void setAuthorization(ApiServerAuthorization authorization) {

        if (authorization != null) {
            authorization.initialize(baseUrl);
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

        // parse custom annotations
        endpointsRequiringAuthorization = AnnotationUtils.parseServiceInterfaceAuthorization(serviceInterface);

        // add service to the list
        serviceInterfaces.add(serviceInterface);

        return this;
    }

    /**
     * Get authorization handler used to authorize requests to server.
     * If no authorization needed may be null.
     *
     * @return ApiServerAuthorization
     */
    /* package */ ApiServerAuthorization getAuthorization() {

        return this.authorization;
    }

    /**
     * Get list of all service interfaces.
     *
     * @return List
     */
    /* package */ List<Class<?>> getServiceInterfaces() {

        return serviceInterfaces;
    }

    /**
     * Hash map of methods and urls requiring authorization.
     *
     * @return HashMap with methods and urls
     */
    /* package */ HashMap<Method, Pattern> getEndpointsRequiringAuthorization() {

        return endpointsRequiringAuthorization;
    }

    /**
     * Get service based on interface.
     *
     * @param serviceInterface
     * @param <T>
     *
     * @return service
     */
    /* package */ <T> T getService(Class<T> serviceInterface) {

        T service = (T) interfaceToServiceMap.get(serviceInterface);
        if (service == null) {
            service = restAdapter.create(serviceInterface);
            interfaceToServiceMap.put(serviceInterface, service);
        }

        return service;
    }
}
