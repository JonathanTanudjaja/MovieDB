package com.hypeclub.www.moviedb;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

    private static Parcelable rvState;
    private static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            rvState = savedInstanceState.getParcelable("test");
            position = savedInstanceState.getInt("pos");
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
        outState.putInt("pos",((GridLayoutManager)movieListRV.getLayoutManager()).findFirstVisibleItemPosition());
        outState.putParcelable("test",movieListRV.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onMovieClick(Movie movie) {
        if (movie != null) {
            Intent goToDetail = new Intent(this,DetailActivity.class);
            goToDetail.putExtra("movie",movie);
            startActivity(goToDetail);
        } else {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
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
                if(rvState != null) {
                    movieListRV.getLayoutManager().onRestoreInstanceState(rvState);
                    movieListRV.scrollToPosition(position);
                }
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
            sortByDialog.show(getFragmentManager(),"sortBy");
            return true;
        } else if (item.getItemId() == R.id.refresh_action) {
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
