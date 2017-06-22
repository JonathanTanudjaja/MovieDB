package com.hypeclub.www.moviedb.utilities;

/**
 * Created by Jo on 18-Jun-17.
 */

public final class Preference {

    public static final String[] sortBy = {
            "popular",
            "top rated"
    };

    public static int sortByIndex = 0;

    // TODO: declare API_KEY here
    private static final String API_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static String getApiKey() {
        return API_KEY;
    }
}
