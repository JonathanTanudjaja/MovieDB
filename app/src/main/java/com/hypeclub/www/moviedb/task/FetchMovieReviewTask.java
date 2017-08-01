package com.hypeclub.www.moviedb.task;

import android.os.AsyncTask;

import com.hypeclub.www.moviedb.model.Review;
import com.hypeclub.www.moviedb.utilities.MovieDataUtils;

import java.util.ArrayList;

/**
 * Created by Jo on 22-Jun-17.
 */

public class FetchMovieReviewTask extends AsyncTask<String, Void, ArrayList<Review>> {

    private OnTaskCompleted listener;

    public FetchMovieReviewTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    public interface OnTaskCompleted{
        void onFetchMovieReviewCompleted(ArrayList<Review> reviews);
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        return MovieDataUtils.getMovieReview(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        listener.onFetchMovieReviewCompleted(reviews);
    }
}