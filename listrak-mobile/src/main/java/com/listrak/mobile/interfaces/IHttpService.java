package com.listrak.mobile.interfaces;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Manages HTTP requests
 * Created by Pam on 5/1/2017.
 */

public interface IHttpService {
    /**
     * Sends the request using the IHttpService for the given URl
     * @param url
     * @throws Exception
     */
    void sendRequest(String url);

    /**
     * Retrieve an okhttp3 Response for the specified Request.
     *
     * @param request the request to execute
     * @return okhttp3 Response
     */

    Response getResponse(Request request);
}
