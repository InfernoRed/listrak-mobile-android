package com.listrak.mobile;

import com.listrak.mobile.interfaces.IHttpService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * IHttpService implementation that uses okHttpClient to execute the request
 * Created by Pam on 5/6/2017.
 */

class HttpService implements IHttpService {
    private static OkHttpClient mOkHttpClient = buildOkHttpClient();

    @Override
    public boolean getResponse(String url) {
        Response response;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            response = mOkHttpClient.newCall(request).execute();
        } catch (Exception e) {
            response = null;
        }
        return response != null && response.isSuccessful();
    }

    private static OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }

}
