package com.hypeclub.www.moviedb.utilities;

import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.model.MovieVideo;
import com.hypeclub.www.moviedb.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

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

    public static final String VIDEO_NAME = "name";
    public static final String VIDEO_KEY = "key";

    public static Movie[] getMovieList(String sortBy) {
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

    public static Review[] getMovieReview(String movieID) {
        URL api = NetworkUtils.buildMovieReviewUrl(movieID);

        try {
            String reviewJson = NetworkUtils.getResponseFromHttpUrl(api);
            return getMovieReviewArrayFromJson(reviewJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MovieVideo[] getMovieVideo(String movieID) {
        URL api = NetworkUtils.buildMovieVideosUrl(movieID);

        try {
            String videoJson = NetworkUtils.getResponseFromHttpUrl(api);
            return getMovieVideoArrayFromJson(videoJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static MovieVideo[] getMovieVideoArrayFromJson(String videoJson) throws JSONException {
        JSONObject json = new JSONObject(videoJson);

        JSONArray videoJsonArray = json.getJSONArray(RESULT_PATH);
        MovieVideo[] videoArray = new MovieVideo[videoJsonArray.length()];
        for (int i = 0 ; i < videoJsonArray.length() ; i++) {
            JSONObject videoJsonObject = videoJsonArray.getJSONObject(i);
            videoArray [i] = new MovieVideo(
                    videoJsonObject.getString(VIDEO_NAME),
                    videoJsonObject.getString(VIDEO_KEY)
            );
        }
        return videoArray;
    }

    private static Review[] getMovieReviewArrayFromJson(String reviewJson) throws JSONException {
        JSONObject json = new JSONObject(reviewJson);

        JSONArray reviewJsonArray = json.getJSONArray(RESULT_PATH);
        Review[] reviewArray = new Review[reviewJsonArray.length()];
        for (int i = 0 ; i < reviewJsonArray.length() ; i++) {
            JSONObject reviewJsonObject = reviewJsonArray.getJSONObject(i);
            reviewArray[i] = new Review(
                    reviewJsonObject.getString(ID_KEY),
                    reviewJsonObject.getString(AUTHOR_KEY),
                    reviewJsonObject.getString(CONTENT_KEY)
            );
        }
        return reviewArray;
    }

    private static Movie[] getMovieArrayFromJson(String movieJson) throws JSONException {

        JSONObject json = new JSONObject(movieJson);

        JSONArray movieJsonArray = json.getJSONArray(RESULT_PATH);
        Movie[] movieArray = new Movie[movieJsonArray.length()];
        for (int i = 0 ; i < movieJsonArray.length() ; i++) {
            JSONObject movieJsonObject = movieJsonArray.getJSONObject(i);
            movieArray[i] = new Movie(
                    movieJsonObject.getString(ID_KEY),
                    movieJsonObject.getString(TITLE_KEY),
                    movieJsonObject.getString(POSTER_KEY),
                    movieJsonObject.getString(SYNOPSIS_KEY),
                    movieJsonObject.getString(RATE_KEY),
                    movieJsonObject.getString(DATE_KEY)
            );
        }
        return movieArray;
    }
}
