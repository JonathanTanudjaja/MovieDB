package com.hypeclub.www.moviedb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypeclub.www.moviedb.adapter.MovieVideoAdapter;
import com.hypeclub.www.moviedb.adapter.ReviewListAdapter;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.model.MovieVideo;
import com.hypeclub.www.moviedb.model.Review;
import com.hypeclub.www.moviedb.task.FetchMovieReviewTask;
import com.hypeclub.www.moviedb.task.FetchMovieVideoTask;
import com.hypeclub.www.moviedb.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypeclub.www.moviedb.utilities.NetworkUtils.buildYoutubeVideoUrl;

public class DetailActivity extends AppCompatActivity
    implements FetchMovieReviewTask.OnTaskCompleted,
    FetchMovieVideoTask.OnTaskCompleted, MovieVideoAdapter.MovieVideoOnClickListener {

    @BindView(R.id.app_bar_image) ImageView posterBar;
    @BindView(R.id.movie_detail_rating) TextView ratingTV;
    @BindView(R.id.movie_detail_release) TextView releaseTV;
    @BindView(R.id.movie_detail_overview) TextView overviewTV;
    @BindView(R.id.no_review) TextView noReview;
    @BindView(R.id.review_rv) RecyclerView reviewRV;
    @BindView(R.id.video_rv) RecyclerView videoRV;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private ReviewListAdapter reviewAdapter;
    private MovieVideoAdapter movieVideoAdapter;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movie = getIntent().getParcelableExtra(MainActivity.MOVIE_EXTRA);

        Glide.with(this)
                .asDrawable()
                .load(NetworkUtils.getLargeMoviePosterBaseUri() + movie.getPosterPath())
                .into(posterBar);

        setTitle(movie.getTitle());
        ratingTV.setText(movie.getVote_avg());
        releaseTV.setText(movie.getRelease_date());
        overviewTV.setText(movie.getOverview());

        new FetchMovieReviewTask(this).execute(movie.getId());
        new FetchMovieVideoTask(this).execute(movie.getId());

        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager videoLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        reviewRV.setLayoutManager(reviewLayoutManager);
        reviewRV.setHasFixedSize(true);

        videoRV.setLayoutManager(videoLayoutManager);
        videoRV.setHasFixedSize(true);

        reviewAdapter = new ReviewListAdapter();
        movieVideoAdapter = new MovieVideoAdapter(this);

        reviewRV.setAdapter(reviewAdapter);
        videoRV.setAdapter(movieVideoAdapter);

        reviewRV.addItemDecoration(new DividerItemDecoration(this,reviewLayoutManager.getOrientation()));
        videoRV.addItemDecoration(new DividerItemDecoration(this,videoLayoutManager.getOrientation()));
    }

    @Override
    public void onFetchMovieReviewCompleted(Review[] reviews) {
        reviewAdapter.setReviewData(reviews);
        if (reviews.length == 0) {
            noReview.setText("We don't have any reviews for "+movie.getTitle()+".");
        }
    }

    @Override
    public void onFetchMovieVideoCompleted(MovieVideo[] movieVideos) {
        movieVideoAdapter.setMovieVideo(movieVideos);
        if (movieVideos.length == 0) {
            noReview.setText("We don't have any videos for "+movie.getTitle()+".");
        }
    }

    @Override
    public void onVideoClick(MovieVideo movieVideo) {
        openWebPage(buildYoutubeVideoUrl(movieVideo.getKey()));
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
