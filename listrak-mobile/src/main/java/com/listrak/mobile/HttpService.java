package com.listrak.mobile;

import android.os.AsyncTask;
import android.util.Log;

import com.listrak.mobile.interfaces.IHttpService;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * IHttpService implementation that executes requests
 * Created by Pam on 5/1/2017.
 */

class HttpService implements IHttpService {
    private static OkHttpClient mOkHttpClient = buildOkHttpClient();
    private static Queue<String> mRequestQueue = new LinkedList<>();
    private static boolean mIsRunning = false;

    @Override
    public void enqueueRequest(String url) {
        mRequestQueue.add(url);
        if (!mIsRunning) {
            mIsRunning = true;
            new EnqueueRequestTask().execute();
        }
    }

    protected boolean getResponse(String url) {
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

    class EnqueueRequestTask extends AsyncTask<Void, Void, Void> {
        final long timeout = 2000;

        @Override
        public Void doInBackground(Void... voids) {
            int count = 0;
            while (!mRequestQueue.isEmpty()) {
                String url = mRequestQueue.peek();

                Log.d(this.getClass().getName(), "count=" + count + " for " + url);

                if (getResponse(url)) {
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
