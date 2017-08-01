package com.hypeclub.www.moviedb.utilities;

import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.model.MovieVideo;
import com.hypeclub.www.moviedb.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Jo on 18-Jun-17.
 */

public final class MovieDataUtils {

    private static final String RESULT_PATH = "results";

    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "original_title";
    private static final String POSTER_KEY = "poster_path";
    private static final String SYNOPSIS_KEY = "overview";
    private static final String RATE_KEY = "vote_average";
    private static final String DATE_KEY = "release_date";

    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";

    private static final String VIDEO_NAME = "name";
    private static final String VIDEO_KEY = "key";

    public static ArrayList<Movie> getMovieList(String sortBy) {
        int sortByIdx = 0;

        if (sortBy.equals(Preference.sortBy[1])) {
            sortByIdx = 1;
        }

        URL api = NetworkUtils.buildMovieListUrl(sortByIdx);

        try {
            String movieJson = NetworkUtils.getResponseFromHttpUrl(api);
            return getMovieArrayFromJson(movieJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Review> getMovieReview(String movieID) {
        URL api = NetworkUtils.buildMovieReviewUrl(movieID);

        try {
            String reviewJson = NetworkUtils.getResponseFromHttpUrl(api);
            return getMovieReviewArrayFromJson(reviewJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<MovieVideo> getMovieVideo(String movieID) {
        URL api = NetworkUtils.buildMovieVideosUrl(movieID);

        try {
            String videoJson = NetworkUtils.getResponseFromHttpUrl(api);
            return getMovieVideoArrayFromJson(videoJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<MovieVideo> getMovieVideoArrayFromJson(String videoJson) throws JSONException {
        JSONObject json = new JSONObject(videoJson);

        JSONArray videoJsonArray = json.getJSONArray(RESULT_PATH);
        ArrayList<MovieVideo> videoArray = new ArrayList<>();
        for (int i = 0 ; i < videoJsonArray.length() ; i++) {
            JSONObject videoJsonObject = videoJsonArray.getJSONObject(i);
            videoArray.add(new MovieVideo(
                    videoJsonObject.getString(VIDEO_NAME),
                    videoJsonObject.getString(VIDEO_KEY)
            ));
        }
        return videoArray;
    }

    private static ArrayList<Review> getMovieReviewArrayFromJson(String reviewJson) throws JSONException {
        JSONObject json = new JSONObject(reviewJson);

        JSONArray reviewJsonArray = json.getJSONArray(RESULT_PATH);
        ArrayList<Review> reviewArray = new ArrayList<>();
        for (int i = 0 ; i < reviewJsonArray.length() ; i++) {
            JSONObject reviewJsonObject = reviewJsonArray.getJSONObject(i);
            reviewArray.add(new Review(
                    reviewJsonObject.getString(ID_KEY),
                    reviewJsonObject.getString(AUTHOR_KEY),
                    reviewJsonObject.getString(CONTENT_KEY)
            ));
        }
        return reviewArray;
    }

    private static ArrayList<Movie> getMovieArrayFromJson(String movieJson) throws JSONException {

        JSONObject json = new JSONObject(movieJson);

        JSONArray movieJsonArray = json.getJSONArray(RESULT_PATH);
        ArrayList<Movie> movieArray = new ArrayList<>();
        for (int i = 0 ; i < movieJsonArray.length() ; i++) {
            JSONObject movieJsonObject = movieJsonArray.getJSONObject(i);
            movieArray.add(new Movie(
                    movieJsonObject.getString(ID_KEY),
                    movieJsonObject.getString(TITLE_KEY),
                    movieJsonObject.getString(POSTER_KEY),
                    movieJsonObject.getString(SYNOPSIS_KEY),
                    movieJsonObject.getString(RATE_KEY),
                    movieJsonObject.getString(DATE_KEY)
            ));
        }
        return movieArray;
    }
}
