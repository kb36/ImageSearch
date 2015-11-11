package com.github.kb36.imagesearch.model;

/**
 * Interface for listening to the results
 * Created by nagarjuna.t1 on 10/23/2015.
 */
public interface IResultsAvailable {
    public void onResultsAvailable(QueryResult results);
}