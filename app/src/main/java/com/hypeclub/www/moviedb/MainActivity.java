package com.hypeclub.www.moviedb;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hypeclub.www.moviedb.adapter.MainFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabViewPager) ViewPager tabViewPager;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    public static final String MOVIE_EXTRA = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        tabViewPager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(tabViewPager);
    }
}
