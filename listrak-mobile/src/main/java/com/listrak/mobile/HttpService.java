package com.listrak.mobile;


import com.listrak.mobile.interfaces.IHttpService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
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
    public void sendRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Config.resolve(IHttpService.class).getResponse(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
    }

    @Override
    public void getResponse(Request request, Callback callback) {
       _okHttpClient.newCall(request).enqueue(callback);
    }

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }
}
