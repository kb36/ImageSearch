package com.github.kb36.imagesearch;

import java.util.List;

/**
 * Created by nagarjuna.t1 on 10/23/2015.
 */
public class QueryResult {
    int responseStatus;
    ResponseData responseData;

    class ResponseData {
        long estimatedResultCount;
        List<Result> results;
    }

    class Result {
        int tbWidth;
        int tbHeight;
        String url;
        String tbUrl;
    }

}
