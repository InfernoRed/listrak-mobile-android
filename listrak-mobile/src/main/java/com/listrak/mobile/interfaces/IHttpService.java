package com.listrak.mobile.interfaces;

import java.util.concurrent.ExecutionException;

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
    void enqueueRequest(String url);
}
