package com.hypeclub.www.moviedb.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hypeclub.www.moviedb.DetailActivity;
import com.hypeclub.www.moviedb.adapter.MovieListAdapter;
import com.hypeclub.www.moviedb.R;
import com.hypeclub.www.moviedb.dialog.SortByDialogFragment;
import com.hypeclub.www.moviedb.model.Movie;
import com.hypeclub.www.moviedb.task.FetchMovieTask;
import com.hypeclub.www.moviedb.utilities.Preference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.hypeclub.www.moviedb.MainActivity.MOVIE_EXTRA;
import static com.hypeclub.www.moviedb.MainActivity.POSITION_KEY;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment
        implements MovieListAdapter.MoviePosterOnClickListener,
        SortByDialogFragment.SortByListener,
        FetchMovieTask.OnTaskCompleted,
        AdapterView.OnItemSelectedListener  {

    @BindView(R.id.movie_list_rv) RecyclerView movieListRV;
    @BindView(R.id.sortSpinner) Spinner sortSpinner;
    @BindViews({R.id.rv_refresh_layout, R.id.empty_refresh_layout}) List<SwipeRefreshLayout> mSwipeRefreshLayout;

    ButterKnife.Action<SwipeRefreshLayout> SET_ON_REFRESH;
    ButterKnife.Setter<SwipeRefreshLayout,int[]> SET_VISIBILITY;
    ButterKnife.Setter<SwipeRefreshLayout, boolean[]> SET_REFRESHING;

    private static MovieListAdapter movieListAdapter;
    private static int position = 0;
    private boolean spinnerInit = false;

    private Context context;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = getActivity();

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(POSITION_KEY);
        }

        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        ButterKnife.bind(this, view);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.context ,
                R.array.sort_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sortSpinner.setAdapter(adapter);

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
            movieListRV.setLayoutManager(new GridLayoutManager(context,2));
        } else {
            movieListRV.setLayoutManager(new GridLayoutManager(context,4));
        }

        movieListRV.setHasFixedSize(true);
        movieListRV.setItemViewCacheSize(20);
        movieListRV.setDrawingCacheEnabled(true);
        movieListRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        movieListAdapter = new MovieListAdapter(this);
        movieListRV.setAdapter(movieListAdapter);

        ButterKnife.apply(mSwipeRefreshLayout,SET_REFRESHING, new boolean[]{true, false});
        sortSpinner.setOnItemSelectedListener(this);
        loadData();

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void sort(int which) {
        Preference.sortByIndex = which;
        loadData();
    }

    private void loadData() {
        ButterKnife.apply(mSwipeRefreshLayout,SET_REFRESHING, new boolean[]{true, true});
        new FetchMovieTask(this).execute(Preference.sortBy[Preference.sortByIndex]);
    }

    @Override
    public void onFetchMovieCompleted(ArrayList<Movie> movies) {
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
            Intent goToDetail = new Intent(this.context,DetailActivity.class);
            goToDetail.putExtra(MOVIE_EXTRA,movie);
            startActivity(goToDetail);
        } else {
            Toast.makeText(this.context,getString(R.string.error_message),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerInit) {
            this.sort(position);
        } else {
            spinnerInit = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY,((GridLayoutManager)movieListRV.getLayoutManager()).findFirstVisibleItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
