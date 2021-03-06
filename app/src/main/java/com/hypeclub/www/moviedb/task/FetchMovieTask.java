package com.hypeclub.www.moviedb.task;

import android.os.AsyncTask;

import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.utilities.MovieDataUtils;

import java.util.ArrayList;

/**
 * Created by Jo on 22-Jun-17.
 */

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private OnTaskCompleted listener;

    public FetchMovieTask (OnTaskCompleted listener) {
        this.listener = listener;
    }

    public interface OnTaskCompleted{
        void onFetchMovieCompleted(ArrayList<Movie> movies);
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        return MovieDataUtils.getMovieList(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        listener.onFetchMovieCompleted(movies);
    }
}