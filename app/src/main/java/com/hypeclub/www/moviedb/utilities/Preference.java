package com.hypeclub.www.moviedb.utilities;

import com.hypeclub.www.moviedb.BuildConfig;

/**
 * Created by Jo on 18-Jun-17.
 */

public final class Preference {

    public static final String[] sortBy = {
            "popular",
            "top rated"
    };

    public static int sortByIndex = 0;

    private static final String API_KEY = BuildConfig.API_KEY;

    public static String getApiKey() {
        return API_KEY;
    }
}
