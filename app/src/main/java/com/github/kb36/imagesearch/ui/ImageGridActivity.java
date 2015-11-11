package com.github.kb36.imagesearch.ui;

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
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.kb36.imagesearch.adapter.CustomAdapter;
import com.github.kb36.imagesearch.R;
import com.github.kb36.imagesearch.provider.RecentQueryProvider;
import com.github.kb36.imagesearch.datastore.RetainedFragment;
import com.github.kb36.imagesearch.model.IResultsAvailable;
import com.github.kb36.imagesearch.model.QueryResult;
import com.github.kb36.imagesearch.model.QueryService;
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
        mGridView.setEmptyView((ImageView) findViewById(R.id.image_background));
        init();
    }

    /**
     * initialize the system
     */
    private void init() {
        mQs = new QueryService();
        mAdapter = new CustomAdapter(this, 0, mDataFragment.getData());
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
                if (scrollState == SCROLL_STATE_IDLE) {
                    Log.d(TAG, "last visible: " + mGridView.getLastVisiblePosition());
                    if (mGridView.getLastVisiblePosition() == mDataFragment.getData().size() - 1) {
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
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QueryResult.Result result = mDataFragment.getDataItem(position);
                Intent intent = new Intent(ImageGridActivity.this, ImageDetailActivity.class);
                intent.putExtra("url", result.url);
                startActivity(intent);
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Handles new search query
     * @param intent
     */
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

    /**
     * call implemented for processing resutls
     * @param qr result set
     */
    @Override
    public void onResultsAvailable(QueryResult qr) {
        Log.d(TAG, "Received results");
        if(qr.responseStatus != 200)
            mDataFragment.setLoadComplete();
        else {
            if(qr.responseData.results.size() == 0 &&
                    mDataFragment.getData().size() == 0) {
                Toast.makeText(ImageGridActivity.this, "No Results found", Toast.LENGTH_LONG).show();
                return;
            }
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

    /**
     * checks whether more data can be fetched.
     * @return
     */
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

