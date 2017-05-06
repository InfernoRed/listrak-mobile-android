package com.listrak.mobile.interfaces;

/**
 * Manage HTTP requests
 * Created by Pam on 5/6/2017.
 */

public interface IHttpService {
    /**
     * Sends the HTTP request for the given URL and returns true if it succeeded with out any errors
     * @param url
     * @return
     */
    boolean getResponse(String url);
}
