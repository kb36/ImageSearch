package com.github.kb36.imagesearch;

import java.util.Map;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by nagarjuna.t1 on 10/23/2015.
 */
public interface QueryResultFetcher {
    @GET("/ajax/services/search/images")
    Call<QueryResult> query(@QueryMap Map<String, String> options);
}
