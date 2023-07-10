package com.ibuttimer.springecom.config;

import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

public class Config {

    public static final String ID_MARKER = "{id}";

    // product categories related
    public static final String PRODUCT_CATEGORIES_PATH = "product-category";
    public static final String PRODUCT_CATEGORIES_URL = "/" + PRODUCT_CATEGORIES_PATH;
    public static final String PRODUCT_CATEGORIES_ID_URL = String.format("%s/%s", PRODUCT_CATEGORIES_URL, ID_MARKER);

    // products related
    public static final String PRODUCTS_URL = "/products";
    public static final String PRODUCT_ID_URL = String.format("%s/%s", PRODUCTS_URL, ID_MARKER);

    // countries related
    public static final String COUNTRIES_URL = "/countries";
    public static final String COUNTRY_ID_URL = String.format("%s/%s", COUNTRIES_URL, ID_MARKER);

    // checkout related
    public static final String CHECKOUT_URL = "/checkout";
    public static final String PURCHASE_URL = "/purchase";
    public static final String PAYMENT_INTENT_URL = "/payment-intent";

    // orders related
    public static final String ORDER_URL = "/orders";

    /** List of <types> under api route root */
    public static final String[] API_TYPES = new String[] {
            ORDER_URL
    };

    // heroku related
    public static final String HELLO_URL = "/hello";

    private String apiBasePath;


    private Config() {
        // no-op
    }

    /**
     * Instance holder class for Initialization-on-demand holder idiom.
     * @see <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom.">Initialization-on-demand holder idiom</a>
     */
    private static class ConfigHolder {
        static final Config INSTANCE = new Config();

    }

    public static Config getInstance() {
        return ConfigHolder.INSTANCE;
    }

    public void setApiBasePath(String apiBasePath) {
        this.apiBasePath = apiBasePath;
    }

    /**
     * Replace the query placeholders in an url with values from the specified map.
     * Note: Spring Data REST base URI is *NOT* prepended to the url.
     * @param url   url template
     * @param map   map of placeholder/value pairs for url & query arguments
     * @return  url
     */
    public static String getUrl(String url, Map<String, Object> map) {
        if (map == null) {
            map = Map.of();
        }
        StringBuilder sb = new StringBuilder(
                url.endsWith("/") ? url.substring(0, url.length() - 1) : url);

        boolean start = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            int index = sb.indexOf(key);

            if (index >= 0) {
                // In url so do replace
                sb.replace(index, index + key.length(), value);
            } else {
                // Must be query parameter
                if (start) {
                    sb.append('?');
                    start = false;
                } else {
                    sb.append('&');
                }
                sb.append(key)
                        .append("=")
                        .append(value);
            }
        }
        return sb.toString();
    }

    /**
     * Replace the query placeholders in an url with values from the specified map.
     * Note: Spring Data REST base URI is prepended to the url.
     * @param url   url template
     * @param map   map of placeholder/value pairs for url & query arguments
     * @return  url
     */
    public String getBasedUrl(String url, Map<String, Object> map) {
        String basedUrl;
        if (apiBasePath.endsWith("/") && url.startsWith("/")) {
            basedUrl = apiBasePath + url.substring(1);
        } else {
            basedUrl = apiBasePath + url;
        }
        return getUrl(basedUrl, map);
    }


}
