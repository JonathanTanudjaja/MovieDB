package com.hypeclub.www.moviedb.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Jo on 29-Jul-17.
 */

public final class FavoriteMoviesContract {

    public static final String CONTENT_AUTHORITY = "com.hypeclub.www.moviedb";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE_MOVIE = "movie";

    private FavoriteMoviesContract() {}

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIE)
                .build();

        public static Uri buildWeatherUriWithDate(String movieID) {
            return CONTENT_URI.buildUpon()
                    .appendPath(movieID)
                    .build();
        }

        public static final String TABLE_NAME = "favorite_music";

        public static final String COLUMN_MOVIE_ID = "movieID";

        public static final String COLUMN_MOVIE_TITLE = "movieTitle";

        public static final String COLUMN_MOVIE_POSTER_PATH = "posterPath";

    }

}
