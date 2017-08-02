package com.hypeclub.www.moviedb.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hypeclub.www.moviedb.DetailActivity;
import com.hypeclub.www.moviedb.R;
import com.hypeclub.www.moviedb.adapter.MovieListAdapter;
import com.hypeclub.www.moviedb.data.FavoriteMoviesContract;
import com.hypeclub.www.moviedb.model.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypeclub.www.moviedb.MainActivity.MOVIE_EXTRA;

public class FavoriteFragment extends Fragment
        implements MovieListAdapter.MoviePosterOnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.no_favorite_tv) TextView  noFavMovieTV;
    @BindView(R.id.favorite_movie_rv) RecyclerView favMovieRV;

    private final String TAG = this.getClass().getSimpleName();

    private static MovieListAdapter movieListAdapter;
    private static int position = 0;
    private Context context;

    private static final int TASK_LOADER_ID = 1;
    public static final String POSITION_KEY = "favoriteposition";

    public FavoriteFragment() {
        // Required empty public constructor
    }

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(POSITION_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = getActivity();

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        ButterKnife.bind(this, view);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            favMovieRV.setLayoutManager(new GridLayoutManager(context,2));
        } else {
            favMovieRV.setLayoutManager(new GridLayoutManager(context,4));
        }

        favMovieRV.setHasFixedSize(true);
        favMovieRV.setItemViewCacheSize(20);
        favMovieRV.setDrawingCacheEnabled(true);
        favMovieRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        movieListAdapter = new MovieListAdapter(this);
        favMovieRV.setAdapter(movieListAdapter);

        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY,((GridLayoutManager)favMovieRV.getLayoutManager()).findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (movie != null) {
            Intent goToDetail = new Intent(this.context,DetailActivity.class);
            goToDetail.putExtra(MOVIE_EXTRA,movie);
            startActivity(goToDetail);
        } else {
            Toast.makeText(this.context,getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getContext()) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    String projection[] = {
                            FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                            FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE,
                            FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH,
                            FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_AVERAGE_VOTE,
                            FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                            FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW
                    };

                    return getActivity().getContentResolver().query(
                            FavoriteMoviesContract.FavoriteMovieEntry.CONTENT_URI,
                            projection,
                            null,
                            null,
                            null
                    );
                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        ArrayList<Movie> movies = new ArrayList<>();

        final int indexId = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID);
        final int indexTitle = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE);
        final int indexPosterPath = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH);
        final int indexVoteAvg = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_AVERAGE_VOTE);
        final int indexReleaseDate = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE);
        final int indexOverview = data.getColumnIndex(FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW);

        while (data.moveToNext()) {
            movies.add(new Movie(
                    data.getString(indexId),
                    data.getString(indexTitle),
                    data.getString(indexPosterPath),
                    data.getString(indexOverview),
                    data.getString(indexVoteAvg),
                    data.getString(indexReleaseDate)
            ));
        }

        if (!movies.isEmpty()) {
            noFavMovieTV.setVisibility(View.INVISIBLE);
            favMovieRV.setVisibility(View.VISIBLE);

            favMovieRV.scrollToPosition(position);
            position = 0;
        } else {
            noFavMovieTV.setVisibility(View.VISIBLE);
            favMovieRV.setVisibility(View.INVISIBLE);
        }

        movieListAdapter.setMovieData(movies);

        data.close();
    }

    @Override
    public void onResume() {
        super.onResume();

        // re-queries for all tasks
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieListAdapter.setMovieData(new ArrayList<Movie>());
    }
}
