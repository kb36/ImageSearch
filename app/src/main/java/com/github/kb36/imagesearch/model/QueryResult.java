package com.github.kb36.imagesearch.model;

import java.util.List;

/**
 * Structure of the JSON Result item returned by
 * search
 * Created by nagarjuna.t1 on 10/23/2015.
 */
public class QueryResult {
    public int responseStatus;
    public ResponseData responseData;

    public class ResponseData {
        public long estimatedResultCount;
        public List<Result> results;
    }

    public class Result {
        public int tbWidth;
        public int tbHeight;
        public String url;
        public String tbUrl;
    }

}
