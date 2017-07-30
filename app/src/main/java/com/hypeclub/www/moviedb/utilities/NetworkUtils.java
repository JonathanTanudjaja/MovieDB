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

    private static final String MOVIE = "movie";

    private static final String REVIEW = "reviews";
    private static final String VIDEO = "videos";

    private static final String MOVIE_POSTER_BASE_URI = "http://image.tmdb.org/t/p/w185/";

    private static final String LARGE_MOVIE_POSTER_BASE_URI = "http://image.tmdb.org/t/p/w500/";

    private static final String API_KEY_QUERY_PARAM = "api_key";

    private static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";

    public static String getMoviePosterBaseUri() {
        return MOVIE_POSTER_BASE_URI;
    }

    public static String getLargeMoviePosterBaseUri() {
        return LARGE_MOVIE_POSTER_BASE_URI;
    }

    public static String buildYoutubeVideoUrl(String key) {
        return YOUTUBE_VIDEO_BASE_URL + key;
    }

    private static Uri buildMovieUri(String movieID) {
        Uri builtUri = Uri.parse(BASE_URI);
        builtUri = Uri.withAppendedPath(builtUri,MOVIE);
        builtUri = Uri.withAppendedPath(builtUri, movieID);
        return builtUri;
    }

    static URL buildMovieReviewUrl (String movieID) {
        Uri builtUri = buildMovieUri(movieID);
        builtUri = builtUri.buildUpon().appendQueryParameter(API_KEY_QUERY_PARAM,Preference.getApiKey())
                .appendPath(REVIEW).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    static URL buildMovieVideosUrl (String movieID) {
        Uri builtUri = buildMovieUri(movieID);
        builtUri = builtUri.buildUpon().appendQueryParameter(API_KEY_QUERY_PARAM,Preference.getApiKey())
                .appendPath(VIDEO).build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
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
