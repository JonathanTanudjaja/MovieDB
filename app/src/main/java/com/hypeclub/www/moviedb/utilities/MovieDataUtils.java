package com.hypeclub.www.moviedb.utilities;

import com.hypeclub.www.moviedb.MainActivity;
import com.hypeclub.www.moviedb.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by Jo on 18-Jun-17.
 */

public final class MovieDataUtils {

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

    private static Movie[] getMovieArrayFromJson(String movieJson) throws JSONException {

        JSONObject json = new JSONObject(movieJson);

        JSONArray movieJsonArray = json.getJSONArray("results");
        Movie[] movieArray = new Movie[movieJsonArray.length()];
        for (int i = 0 ; i < movieJsonArray.length() ; i++) {
            JSONObject movieJsonObject = movieJsonArray.getJSONObject(i);
            movieArray[i] = new Movie(
                    movieJsonObject.getString("id"),
                    movieJsonObject.getString("original_title"),
                    movieJsonObject.getString("poster_path"),
                    movieJsonObject.getString("overview"),
                    movieJsonObject.getString("vote_average"),
                    movieJsonObject.getString("release_date")
            );
        }
        return movieArray;
    }
}
