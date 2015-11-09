package com.github.kb36.imagesearch;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.GridView;

import com.squareup.picasso.Picasso;

/**
 * Main activity for displaying the Image Search Results
 */
public class ImageGridActivity extends AppCompatActivity implements IResultsAvailable {
    private static final String TAG = "ImageGridActivity";
    private RetainedFragment mDataFragment;
    private static final String RETAINED_FRAGMENT_TAG = "data";
    private QueryService mQs;
    private GridView mGridView;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        FragmentManager fm = getFragmentManager();
        mDataFragment = (RetainedFragment) fm.findFragmentByTag(RETAINED_FRAGMENT_TAG);
        if(mDataFragment == null) {
            mDataFragment = new RetainedFragment();
            fm.beginTransaction().add(mDataFragment, RETAINED_FRAGMENT_TAG).commit();
        }
        mGridView = (GridView) findViewById(R.id.gridView);
        init();
    }

    private void init() {
        mQs = new QueryService();
        mAdapter = new CustomAdapter(this, R.layout.image_item_layout, mDataFragment.getData());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                final Picasso picasso = Picasso.with(ImageGridActivity.this);
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    picasso.resumeTag(ImageGridActivity.this);
                } else {
                    picasso.pauseTag(ImageGridActivity.this);
                }
                if(scrollState == SCROLL_STATE_IDLE) {
                    Log.d(TAG, "last visible: " + mGridView.getLastVisiblePosition());
                    if(mGridView.getLastVisiblePosition() == mDataFragment.getData().size()-1) {
                        if (canFetchMore()) {
                            mQs.query(mDataFragment.getQuery(), mDataFragment.getData().size(), ImageGridActivity.this);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent.ACTION_SEARCH.equals(intent.getAction())) {
            mDataFragment.resetData();
            mDataFragment.resetLoadComplete();
            init();
            mDataFragment.setQuery(intent.getStringExtra(SearchManager.QUERY));
            Log.d(TAG, "Received query: " + mDataFragment.getQuery());
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    RecentQueryProvider.AUTHORITY, RecentQueryProvider.MODE);
            suggestions.saveRecentQuery(mDataFragment.getQuery(), null);

            if(canFetchMore())
                mQs.query(mDataFragment.getQuery(), mDataFragment.getData().size(), this);
        }
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
        if(mDataFragment.getData().size() <= 12 && canFetchMore()) {
                mQs.query(mDataFragment.getQuery(), mDataFragment.getData().size(), ImageGridActivity.this);
        }
    }

    private boolean canFetchMore() {
        Log.d(TAG, "mIsLoadComplete: " + mDataFragment.isLoadComplete() + " " + " is_running: " + mQs.isRunning());
        return !mDataFragment.isLoadComplete() && !mQs.isRunning();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem searchMenuItem = menu.findItem( R.id.search );
        //searchMenuItem.expandActionView();
        //MenuItemCompat.expandActionView(searchMenuItem);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        return true;
    }
}

