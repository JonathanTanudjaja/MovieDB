package com.hypeclub.www.moviedb.contract;

import android.provider.BaseColumns;

/**
 * Created by Jo on 29-Jul-17.
 */

public final class FavoriteMoviesContract {

    private FavoriteMoviesContract() {}

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorite_music";

        public static final String COLUMN_MOVIE_ID = "movieID";

    }

}
