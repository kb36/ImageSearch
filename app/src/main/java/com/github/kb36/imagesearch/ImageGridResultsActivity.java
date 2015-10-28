package com.github.kb36.imagesearch;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

public class ImageGridResultsActivity extends Activity implements IResultsAvailable {
    private static final String TAG = "ImageGridActivity";
    private RetainedFragment mDataFragment;
    private static final String RETAINED_FRAGMENT_TAG = "data";
    private QueryService mQs;
    private GridView mGridView;
    private CustomAdapter mAdapter;
    private String mCurrentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid_results);
        FragmentManager fm = getFragmentManager();
        mDataFragment = (RetainedFragment) fm.findFragmentByTag(RETAINED_FRAGMENT_TAG);
        if(mDataFragment == null) {
            mDataFragment = new RetainedFragment();
            fm.beginTransaction().add(mDataFragment, RETAINED_FRAGMENT_TAG).commit();
        }

        mCurrentQuery = getIntent().getStringExtra("QUERY");
        mQs = new QueryService();
        mGridView = (GridView) findViewById(R.id.gridView);
        mAdapter = new CustomAdapter(this, R.layout.image_item_layout, mDataFragment.getData());
        if(canFetchMore())
            mQs.query(mCurrentQuery, mDataFragment.getData().size(), this);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResultsAvailable(QueryResult qr) {
        Log.d(TAG, "Received results");
        if(qr.responseStatus != 200)
            mDataFragment.setLoadComplete();
        else {
            if(qr.responseData.results.size() < 4)
                mDataFragment.setLoadComplete();
            mDataFragment.addData(qr.responseData.results);
            mAdapter.notifyDataSetChanged();
            Log.d(TAG, "result size: " + mDataFragment.getData().size());
        }

        if(canFetchMore()) {
                mQs.query(mCurrentQuery, mDataFragment.getData().size(), ImageGridResultsActivity.this);
        }
    }

    private boolean canFetchMore() {
        Log.d(TAG, "mIsLoadComplete: " + mDataFragment.isLoadComplete() + " " + " is_running: " + mQs.isRunning());
        return !mDataFragment.isLoadComplete() && !mQs.isRunning();
    }
}

