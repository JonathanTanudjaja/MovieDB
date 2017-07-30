package com.hypeclub.www.moviedb.task;

import android.os.AsyncTask;

import com.hypeclub.www.moviedb.model.Review;
import com.hypeclub.www.moviedb.utilities.MovieDataUtils;

/**
 * Created by Jo on 22-Jun-17.
 */

public class FetchMovieReviewTask extends AsyncTask<String, Void, Review[]> {

    private OnTaskCompleted listener;

    public FetchMovieReviewTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    public interface OnTaskCompleted{
        void onFetchMovieReviewCompleted(Review[] reviews);
    }

    @Override
    protected Review[] doInBackground(String... params) {
        return MovieDataUtils.getMovieReview(params[0]);
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        listener.onFetchMovieReviewCompleted(reviews);
    }
}