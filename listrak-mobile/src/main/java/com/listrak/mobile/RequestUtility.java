package com.listrak.mobile;

import android.support.annotation.Nullable;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IHttpService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Helper class to handle formatting and executing requests
 * Created by Pam on 5/1/2017.
 */

class RequestUtility {
    /**
     * Sends the request using the IHttpService for the given URl
     * @param url
     * @throws Exception
     */
    static void sendRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = Config.resolve(IHttpService.class).getResponse(request);
        if (!response.isSuccessful()) {
            // TODO: handle bad response?
        }
    }

    /**
     * Returns the formatted url containing any additional parameters and formatted according the configured context
     * @param host
     * @param path
     * @param additionalParams
     * @param args
     * @return
     * @throws UnsupportedEncodingException
     */
    static String getFormattedUrl(String host, String path, @Nullable Map<String, String> additionalParams, Object... args) throws UnsupportedEncodingException {
        IContext context = Config.resolve(IContext.class);

        String scheme = context.getUseHttps() ? "https" : "http";

        String hostOverride = context.getHostOverride();
        host = hostOverride == null || hostOverride.isEmpty() ? host : hostOverride;

        String baseUrl = scheme + "://" + host + "/" + path;
        String formattedUrl = String.format(baseUrl, args);

        if (additionalParams != null && !additionalParams.isEmpty()) {
            return appendQueryStringParameters(formattedUrl, additionalParams);
        }

        return formattedUrl;
    }


    private static String appendQueryStringParameters(String url, Map<String, String> additionalParameters) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();

        for (Map.Entry<String, String> entry : additionalParameters.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append('&');
            }

            queryString.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            queryString.append('=');
            queryString.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        queryString.insert(0, url.indexOf("?") > 0 ? '&' : '?');

        url = url + queryString;
        return url;
    }
}
