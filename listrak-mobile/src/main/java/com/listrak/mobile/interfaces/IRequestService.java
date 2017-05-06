package com.listrak.mobile.interfaces;

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Manages requests sent to the HTTP service
 * Created by Pam on 5/1/2017.
 */

public interface IRequestService {
    /**
     * Queues the request to be sent to the IHttpService
     * @param url
     * @throws Exception
     */
    void enqueueRequest(String url);

    /**
     * Returns the formatted url containing any additional parameters and formatted according the configured context
     * @param host
     * @param path
     * @param additionalParams
     * @param args
     * @return
     * @throws UnsupportedEncodingException
     */
    String getFormattedUrl(String host, String path, @Nullable Map<String, String> additionalParams, Object... args) throws UnsupportedEncodingException;
}
