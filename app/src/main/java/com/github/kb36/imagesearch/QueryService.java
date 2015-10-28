package com.github.kb36.imagesearch;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nagarjuna.t1 on 10/23/2015.
 */
public class QueryService {
    private static final String LOG_TAG = "QueryService";
    private boolean IS_RUNNING = false;
    private static final String API_URL = "http://ajax.googleapis.com";
    Retrofit mRetrofit;
    public QueryService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void query(String query, int begin, final IResultsAvailable iResultsAvailable)  {
        Log.d(LOG_TAG, "Received query: " + query + " begin: "+ begin);
        QueryResultFetcher qrf = mRetrofit.create(QueryResultFetcher.class);
        Map<String, String> options = new HashMap<String, String>();
        options.put("v", "1.0");
        options.put("q", query);
        options.put("rsz", "4");
        options.put("start", String.valueOf(begin));

        Call<QueryResult> call = qrf.query(options);
        IS_RUNNING = true;
        call.enqueue(new Callback<QueryResult>() {
            @Override
            public void onResponse(Response<QueryResult> response, Retrofit retrofit) {
                Log.d(LOG_TAG, "Received response: "+ response.code());
                IS_RUNNING = false;
                if(response.code() == 200 && response.body().responseStatus == 200) {
                    iResultsAvailable.onResultsAvailable(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(LOG_TAG, "Received exception");
                IS_RUNNING = false;
            }
        });
    }

    public boolean isRunning() {
        return IS_RUNNING;
    }
}
