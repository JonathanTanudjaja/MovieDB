package com.hypeclub.www.moviedb.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hypeclub.www.moviedb.fragment.ExploreFragment;
import com.hypeclub.www.moviedb.fragment.FavoriteFragment;

/**
 * Created by Jo on 30-Jul-17.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private String pageTabTitle[] = new String[]{
            "Explore",
            "Favorite"
    };
    private Fragment pages[] = new Fragment[]{
            ExploreFragment.newInstance(),
            FavoriteFragment.newInstance()
    };

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return pages.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTabTitle[position];
    }
}
