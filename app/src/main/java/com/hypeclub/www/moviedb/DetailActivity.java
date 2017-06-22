package com.hypeclub.www.moviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    TextView titleTV;
    TextView ratingTV;
    TextView releaseTV;
    TextView overviewTV;
    ImageView posterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titleTV = (TextView) findViewById(R.id.movie_detail_Title);
        ratingTV = (TextView) findViewById(R.id.movie_detail_rating);
        releaseTV = (TextView) findViewById(R.id.movie_detail_release);
        overviewTV = (TextView) findViewById(R.id.movie_detail_overview);
        posterImage = (ImageView) findViewById(R.id.movie_detail_poster);

        Movie movie = getIntent().getParcelableExtra("movie");

        Glide.with(this)
                .asDrawable()
                .load(NetworkUtils.getMoviePosterBaseUri() + movie.getPosterPath())
                .into(posterImage);
        titleTV.setText(movie.getTitle());
        ratingTV.setText(movie.getVote_avg() + " / 10");
        releaseTV.setText(movie.getRelease_date());
        overviewTV.setText(movie.getOverview());
    }
}
