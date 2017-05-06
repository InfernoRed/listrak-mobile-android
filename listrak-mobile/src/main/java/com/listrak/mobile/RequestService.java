package com.listrak.mobile;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.listrak.mobile.interfaces.IContext;
import com.listrak.mobile.interfaces.IHttpService;
import com.listrak.mobile.interfaces.IRequestService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * IRequestService implementation that manages a requests
 * Created by Pam on 5/1/2017.
 */

class RequestService implements IRequestService {
    private static Queue<String> mRequestQueue = new LinkedList<>();
    private static boolean mIsRunning = false;

    /**
     * Execute the request for the given task
     * @param url
     * @param task
     */
    public void enqueueRequest(String url, EnqueueRequestTask task) {
        mRequestQueue.add(url);
        if (!mIsRunning) {
            mIsRunning = true;
            task.execute();
        }
    }

    @Override
    public void enqueueRequest(String url) {
        enqueueRequest(url, new EnqueueRequestTask());
    }

    @Override
    public String getFormattedUrl(String host, String path, @Nullable Map<String, String> additionalParams, Object... args) throws UnsupportedEncodingException {
        IContext context = Config.resolve(IContext.class);

        String scheme = context.getUseHttps() ? "https" : "http";

        String hostOverride = context.getHostOverride();
        host = hostOverride == null || hostOverride.isEmpty() ? host : hostOverride;

        // if path contains slash in the beginning already, remove it so we don't have double slashes
        path = path.startsWith("/") ? path.substring(1) : path;

        String baseUrl = scheme + "://" + host + "/" + path;
        String formattedUrl = String.format(baseUrl, args);

        if (additionalParams != null && !additionalParams.isEmpty()) {
            return appendQueryStringParameters(formattedUrl, additionalParams);
        }

        return formattedUrl;
    }

    private String appendQueryStringParameters(String url, Map<String, String> additionalParameters) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();

        for (Map.Entry<String, String> entry : additionalParameters.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append('&');
            }

            queryString.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            queryString.append('=');
            queryString.append(entry.getValue() == null ? "" : URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        queryString.insert(0, url.indexOf("?") > 0 ? '&' : '?');

        url = url + queryString;
        return url;
    }

    class EnqueueRequestTask extends AsyncTask<Void, Void, Void> {
        final long timeout = 2000;

        @Override
        public Void doInBackground(Void... voids) {
            int count = 0;
            while (!mRequestQueue.isEmpty()) {
                String url = mRequestQueue.peek();

                //// Uncomment log output to see request counts and urls
                //Log.d(this.getClass().getName(), "count=" + count + " for " + url);

                if (Config.resolve(IHttpService.class).getResponse(url)) {
                    mRequestQueue.remove();
                } else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }

            mIsRunning = false;
            return null;
        }
    }
}
