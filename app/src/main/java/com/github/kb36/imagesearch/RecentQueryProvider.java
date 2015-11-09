package com.github.kb36.imagesearch;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by kb36 on 11/8/2015.
 */
public class RecentQueryProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.github.kb36.imagesearch.RecentQueryProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;
    public RecentQueryProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
