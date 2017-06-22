package com.hypeclub.www.moviedb;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hypeclub.www.moviedb.dialog.SortByDialogFragment;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.utilities.MovieDataUtils;
import com.hypeclub.www.moviedb.utilities.Preference;

public class MainActivity extends AppCompatActivity
        implements MovieListAdapter.MoviePosterOnClickListener, SortByDialogFragment.SortByListener{

    private static MovieListAdapter movieListAdapter;
    private RecyclerView movieListRV;
    private ProgressBar mLoader;
    private TextView mErrMessage;

    public static final String MOVIE_EXTRA = "movie";
    public static final String POSITION_KEY = "positionkey";
    public static final String SORT_BY_TAG = "sort_dialog";
    private static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(POSITION_KEY);
        }

        setContentView(R.layout.activity_main);

        movieListRV = (RecyclerView) findViewById(R.id.movie_list_rv);
        mLoader = (ProgressBar) findViewById(R.id.loading_indicator);
        mErrMessage = (TextView) findViewById(R.id.err_message_tv);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        movieListRV.setLayoutManager(gridLayoutManager);

        movieListRV.setHasFixedSize(true);
        movieListRV.setItemViewCacheSize(20);
        movieListRV.setDrawingCacheEnabled(true);
        movieListRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        movieListAdapter = new MovieListAdapter(this);
        movieListRV.setAdapter(movieListAdapter);

        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY,((GridLayoutManager)movieListRV.getLayoutManager()).findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (movie != null) {
            Intent goToDetail = new Intent(this,DetailActivity.class);
            goToDetail.putExtra(MOVIE_EXTRA,movie);
            startActivity(goToDetail);
        } else {
            Toast.makeText(this,getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        movieListRV.setVisibility(View.INVISIBLE);
        mErrMessage.setVisibility(View.INVISIBLE);
        mLoader.setVisibility(View.VISIBLE);
        new FetchMovieTask().execute(Preference.sortBy[Preference.sortByIndex]);
    }

    @Override
    public void sort(int which) {
        Preference.sortByIndex = which;
        loadData();
    }

    private class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {
            return MovieDataUtils.getMovieList(params[0]);
        }

        @Override
        protected void onPostExecute(Movie[] movieData) {
            mLoader.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                movieListAdapter.setMovieData(movieData);
                movieListRV.setVisibility(View.VISIBLE);
                movieListRV.scrollToPosition(position);
                position = 0;
            } else {
                mErrMessage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_action) {
            DialogFragment sortByDialog = new SortByDialogFragment();
            sortByDialog.show(getFragmentManager(),SORT_BY_TAG);
            return true;
        } else if (item.getItemId() == R.id.refresh_action) {
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
