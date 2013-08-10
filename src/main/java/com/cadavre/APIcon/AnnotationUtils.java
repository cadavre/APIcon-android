package com.cadavre.APIcon;

import com.cadavre.APIcon.annotation.Authorization;
import retrofit.http.RestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for various annotations used across APIcon system.
 *
 * @author Seweryn Zeman
 * @version 1
 */
final class AnnotationUtils {

    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");

    /**
     * Parse Retrofit service interface for Authorization annotations.
     *
     * @param serviceInterface
     *
     * @return HashMap with methods and urls
     */
    public static HashMap<Method, Pattern> parseServiceInterfaceAuthorization(Class<?> serviceInterface) {

        HashMap<Method, Pattern> findings = new HashMap<Method, Pattern>();
        for (Method method : serviceInterface.getMethods()) {

            boolean needsAuthorization = false;
            String pattern = null;
            for (Annotation methodAnnotation : method.getAnnotations()) {
                Class<? extends Annotation> annotationType = methodAnnotation.annotationType();

                // look for our annotation
                if (annotationType == Authorization.class) {
                    needsAuthorization = true;
                    continue;
                }

                // look for a @RestMethod annotation indicating retrofit method
                for (Annotation innerAnnotation : annotationType.getAnnotations()) {
                    if (RestMethod.class == innerAnnotation.annotationType()) {
                        try {
                            pattern = (String) annotationType.getMethod("value").invoke(methodAnnotation);
                            pattern = parsePath(pattern);
                        } catch (Exception e) {
                            Logger.dummyException(e);
                        }
                        break;
                    }
                }
            }

            if (needsAuthorization && pattern != null && !pattern.isEmpty()) {
                findings.put(method, Pattern.compile(pattern));
            }
        }

        return findings;
    }

    /**
     * Parse path to get authorization pattern.
     *
     * @param path
     *
     * @return String
     */
    private static String parsePath(String path) {

        // get the relative URL path and existing query string, if present
        String url = path;
        String query;
        int question = path.indexOf('?');
        if (question != -1 && question < path.length() - 1) {
            url = path.substring(0, question);
            query = path.substring(question + 1);

            // ensure the query string does not have any named parameters
            Matcher queryParamMatcher = PARAM_URL_REGEX.matcher(query);
            if (queryParamMatcher.find()) {
                Logger.dummyError();
            }
        }

        Matcher m = PARAM_URL_REGEX.matcher(url);
        String pattern = "^" + m.replaceAll(".+");
        Logger.d("Adding auth pattern: \"" + pattern + "\"");

        return pattern;
    }
}
