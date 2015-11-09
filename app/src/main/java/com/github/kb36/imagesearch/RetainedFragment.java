package com.github.kb36.imagesearch;
import android.app.Fragment;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
/**
 * Created by nagarjuna.t1 on 10/23/2015.
 */
public class RetainedFragment extends Fragment {
    private boolean mIsLoadingComplete;
    private List<QueryResult.Result> mResults;
    private String query;
    public RetainedFragment() {
        if(mResults == null) {
            mResults = new ArrayList<QueryResult.Result>();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public List<QueryResult.Result> getData() {
        return mResults;
    }

    public void addData(List<QueryResult.Result> data) {
        mResults.addAll(data);
    }

    public void addDataItem(QueryResult.Result dataItem) {
        mResults.add(dataItem);
    }

    public void resetData() {
        mResults.clear();
    }
    public void resetLoadComplete() {
        mIsLoadingComplete = false;
    }
    public void setLoadComplete() {
        mIsLoadingComplete = true;
    }

    public boolean isLoadComplete() {
        return mIsLoadingComplete;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String _query) {
        query = _query;
    }
}
