package com.github.kb36.imagesearch.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * provides search history suggestions
 * Created by kb36 on 11/8/2015.
 */
public class RecentQueryProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.github.kb36.imagesearch.provider.RecentQueryProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;
    public RecentQueryProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
