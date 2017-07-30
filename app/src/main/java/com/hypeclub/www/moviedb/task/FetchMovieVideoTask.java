package com.hypeclub.www.moviedb.task;

import android.os.AsyncTask;

import com.hypeclub.www.moviedb.model.MovieVideo;
import com.hypeclub.www.moviedb.utilities.MovieDataUtils;

/**
 * Created by Jo on 30-Jul-17.
 */

public class FetchMovieVideoTask extends AsyncTask<String, Void, MovieVideo[]> {

    private FetchMovieVideoTask.OnTaskCompleted listener;

    public FetchMovieVideoTask(FetchMovieVideoTask.OnTaskCompleted listener) {
        this.listener = listener;
    }

    public interface OnTaskCompleted{
        void onFetchMovieVideoCompleted(MovieVideo[] movieVideos);
    }

    @Override
    protected MovieVideo[] doInBackground(String... params) {
        return MovieDataUtils.getMovieVideo(params[0]);
    }

    @Override
    protected void onPostExecute(MovieVideo[] movieVideos) {
        listener.onFetchMovieVideoCompleted(movieVideos);
    }
}
