package com.hypeclub.www.moviedb;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hypeclub.www.moviedb.dialog.SortByDialogFragment;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.task.FetchMovieTask;
import com.hypeclub.www.moviedb.utilities.Preference;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements MovieListAdapter.MoviePosterOnClickListener, SortByDialogFragment.SortByListener,
        FetchMovieTask.OnTaskCompleted{

    @BindView(R.id.movie_list_rv) RecyclerView movieListRV;
    @BindView(R.id.err_message_tv) TextView mErrMessage;
    @BindViews({R.id.rv_refresh_layout, R.id.empty_refresh_layout}) List<SwipeRefreshLayout> mSwipeRefreshLayout;

    ButterKnife.Action<SwipeRefreshLayout> SET_ON_REFRESH;
    ButterKnife.Setter<SwipeRefreshLayout,int[]> SET_VISIBILITY;
    ButterKnife.Setter<SwipeRefreshLayout, boolean[]> SET_REFRESHING;

    private static MovieListAdapter movieListAdapter;

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
        ButterKnife.bind(this);

        SET_ON_REFRESH = new ButterKnife.Action<SwipeRefreshLayout>() {
            @Override
            public void apply(@NonNull SwipeRefreshLayout view, int index) {
                view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadData();
                    }
                });
            }
        };

        SET_VISIBILITY = new ButterKnife.Setter<SwipeRefreshLayout, int[]>() {
            @Override
            public void set(@NonNull SwipeRefreshLayout view, int[] value, int index) {
                view.setVisibility(value[index]);
            }
        };

        SET_REFRESHING = new ButterKnife.Setter<SwipeRefreshLayout, boolean[]>() {
            @Override
            public void set(@NonNull SwipeRefreshLayout view, boolean[] value, int index) {
                view.setRefreshing(value[index]);
            }
        };

        ButterKnife.apply(mSwipeRefreshLayout,SET_ON_REFRESH);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            movieListRV.setLayoutManager(new GridLayoutManager(this,2));
        } else {
            movieListRV.setLayoutManager(new GridLayoutManager(this,4));
        }

        movieListRV.setHasFixedSize(true);
        movieListRV.setItemViewCacheSize(20);
        movieListRV.setDrawingCacheEnabled(true);
        movieListRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        movieListAdapter = new MovieListAdapter(this);
        movieListRV.setAdapter(movieListAdapter);

        ButterKnife.apply(mSwipeRefreshLayout,SET_REFRESHING, new boolean[]{true, false});
        loadData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY,((GridLayoutManager)movieListRV.getLayoutManager()).findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
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

    @Override
    public void sort(int which) {
        Preference.sortByIndex = which;
        loadData();
    }

    private void loadData() {
        new FetchMovieTask(this).execute(Preference.sortBy[Preference.sortByIndex]);
    }

    @Override
    public void onFetchMovieCompleted(Movie[] movies) {
        ButterKnife.apply(mSwipeRefreshLayout,SET_REFRESHING, new boolean[]{false, false});
        if (movies != null) {
            ButterKnife.apply(mSwipeRefreshLayout,SET_VISIBILITY, new int[]{View.VISIBLE, View.INVISIBLE});
            movieListAdapter.setMovieData(movies);
            movieListRV.scrollToPosition(position);
            position = 0;
        } else {
            ButterKnife.apply(mSwipeRefreshLayout,SET_VISIBILITY, new int[]{View.INVISIBLE, View.VISIBLE});
        }
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

}
