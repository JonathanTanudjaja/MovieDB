package com.hypeclub.www.moviedb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.movie_detail_poster) ImageView posterImage;
    @BindView(R.id.movie_detail_title) TextView titleTV;
    @BindView(R.id.movie_detail_rating) TextView ratingTV;
    @BindView(R.id.movie_detail_release) TextView releaseTV;
    @BindView(R.id.movie_detail_overview) TextView overviewTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra(MainActivity.MOVIE_EXTRA);

        Glide.with(this)
                .asDrawable()
                .load(NetworkUtils.getMoviePosterBaseUri() + movie.getPosterPath())
                .into(posterImage);
        titleTV.setText(movie.getTitle());
        ratingTV.setText(movie.getVote_avg());
        releaseTV.setText(movie.getRelease_date());
        overviewTV.setText(movie.getOverview());
    }
}
