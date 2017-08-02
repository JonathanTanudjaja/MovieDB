package com.hypeclub.www.moviedb;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypeclub.www.moviedb.adapter.MovieVideoAdapter;
import com.hypeclub.www.moviedb.adapter.ReviewListAdapter;
import com.hypeclub.www.moviedb.data.FavoriteMovieDbHelper;
import com.hypeclub.www.moviedb.data.FavoriteMoviesContract;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.model.MovieVideo;
import com.hypeclub.www.moviedb.model.Review;
import com.hypeclub.www.moviedb.task.FetchMovieReviewTask;
import com.hypeclub.www.moviedb.task.FetchMovieVideoTask;
import com.hypeclub.www.moviedb.utilities.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypeclub.www.moviedb.utilities.NetworkUtils.buildYoutubeVideoUrl;

public class DetailActivity extends AppCompatActivity
    implements FetchMovieReviewTask.OnTaskCompleted,
        FetchMovieVideoTask.OnTaskCompleted,
        MovieVideoAdapter.MovieVideoOnClickListener, View.OnClickListener {

    @BindView(R.id.app_bar_image) ImageView posterBar;
    @BindView(R.id.movie_detail_rating) TextView ratingTV;
    @BindView(R.id.movie_detail_release) TextView releaseTV;
    @BindView(R.id.movie_detail_overview) TextView overviewTV;
    @BindView(R.id.no_review) TextView noReview;
    @BindView(R.id.no_videos) TextView noVideo;
    @BindView(R.id.review_rv) RecyclerView reviewRV;
    @BindView(R.id.video_rv) RecyclerView videoRV;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.favorite_fab) FloatingActionButton favoriteFAB;

    private ReviewListAdapter reviewAdapter;
    private MovieVideoAdapter movieVideoAdapter;
    private Movie movie;

    private FavoriteMovieDbHelper favoriteMovieDbHelper;
    private Boolean isFavorite = false;

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

        favoriteMovieDbHelper = new FavoriteMovieDbHelper(this);

        checkFavoriteStatus();

        favoriteFAB.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        favoriteMovieDbHelper.close();
        super.onDestroy();
    }

    public void checkFavoriteStatus () {

        Cursor cursor = getContentResolver().query(
                FavoriteMoviesContract.FavoriteMovieEntry.buildWeatherUriWithDate(movie.getId()),
                new String[] {FavoriteMoviesContract.FavoriteMovieEntry._ID},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                setFavorite();
            } else {
                setUnfavorite();
            }
            cursor.close();
        }

    }

    @Override
    public void onFetchMovieReviewCompleted(ArrayList<Review> reviews) {
        reviewAdapter.setReviewData(reviews);
        if (null == reviews || reviews.isEmpty()) {
            noReview.setText("We don't have any reviews for "+movie.getTitle()+".");
        }
    }

    @Override
    public void onFetchMovieVideoCompleted(ArrayList<MovieVideo> movieVideos) {
        movieVideoAdapter.setMovieVideo(movieVideos);
        if (null == movieVideos || movieVideos.isEmpty()) {
            noVideo.setText("We don't have any videos for "+movie.getTitle()+".");
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

    @Override
    public void onClick(View v) {
        if (v.getId() == favoriteFAB.getId()) {
            if (isFavorite) {
                deleteFromFavorite();
            } else {
                insertToFavorite();
            }
        }
    }

    public void insertToFavorite() {

        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,movie.getId());
        values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE,movie.getTitle());
        values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH,movie.getPosterPath());
        values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_AVERAGE_VOTE,movie.getVote_avg());
        values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE,movie.getRelease_date());
        values.put(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW,movie.getOverview());

        getContentResolver().insert(
                FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                values
        );
        setFavorite();
    }

    public void deleteFromFavorite() {
        getContentResolver().delete(
                FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{movie.getId()}
        );
        setUnfavorite();
    }

    public void setFavorite() {
        isFavorite = true;
        favoriteFAB.setImageResource(R.drawable.ic_favorite_white_24dp);
    }
    public void setUnfavorite() {
        isFavorite = false;
        favoriteFAB.setImageResource(R.drawable.ic_favorite_border_white_24dp);
    }
}
