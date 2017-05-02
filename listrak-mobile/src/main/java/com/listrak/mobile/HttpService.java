package com.listrak.mobile;


import com.listrak.mobile.interfaces.IHttpService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * IHttpService implementation that executes requests
 * Created by Pam on 5/1/2017.
 */

class HttpService implements IHttpService {
    private static OkHttpClient _okHttpClient = getOkHttpClient();

    @Override
    public Response getResponse(Request request) {
        Response response = null;

        try {
            response = _okHttpClient.newCall(request).execute();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (NullPointerException ex) {
            response = null;
        }

        return response;
    }

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }
}
