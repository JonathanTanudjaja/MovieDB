package com.hypeclub.www.moviedb.task;

import android.os.AsyncTask;

import com.hypeclub.www.moviedb.model.MovieVideo;
import com.hypeclub.www.moviedb.utilities.MovieDataUtils;

import java.util.ArrayList;

/**
 * Created by Jo on 30-Jul-17.
 */

public class FetchMovieVideoTask extends AsyncTask<String, Void, ArrayList<MovieVideo>> {

    private FetchMovieVideoTask.OnTaskCompleted listener;

    public FetchMovieVideoTask(FetchMovieVideoTask.OnTaskCompleted listener) {
        this.listener = listener;
    }

    public interface OnTaskCompleted{
        void onFetchMovieVideoCompleted(ArrayList<MovieVideo> movieVideos);
    }

    @Override
    protected ArrayList<MovieVideo> doInBackground(String... params) {
        return MovieDataUtils.getMovieVideo(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<MovieVideo> movieVideos) {
        listener.onFetchMovieVideoCompleted(movieVideos);
    }
}
