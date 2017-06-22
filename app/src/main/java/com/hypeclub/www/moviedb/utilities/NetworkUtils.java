package com.hypeclub.www.moviedb.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Jo on 20-Jun-17.
 */

public final class NetworkUtils {

    private static final String BASE_URI = "https://api.themoviedb.org/3/";

    private static final String POPULAR_MOVIE = "movie/popular";

    private static final String TOP_MOVIE = "movie/top_rated";

    private static final String MOVIE_POSTER_BASE_URI = "http://image.tmdb.org/t/p/w185/";

    private static final String API_KEY_QUERY_PARAM = "api_key";

    public static String getMoviePosterBaseUri() {
        return MOVIE_POSTER_BASE_URI;
    }

    static URL buildMovieListUrl(int sortBy) {
        Uri builtUri = Uri.parse(BASE_URI);
        if (sortBy == 1) {
            builtUri = Uri.withAppendedPath(builtUri,TOP_MOVIE);
        } else {
            builtUri = Uri.withAppendedPath(builtUri,POPULAR_MOVIE);
        }

        builtUri = builtUri.buildUpon().appendQueryParameter(API_KEY_QUERY_PARAM,Preference.getApiKey()).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    // from exercise in udacity.com
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream inputStream = httpURLConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
