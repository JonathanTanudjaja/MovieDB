package com.hypeclub.www.moviedb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.hypeclub.www.moviedb.data.FavoriteMoviesContract.FavoriteMovieEntry;

/**
 * Created by Jo on 29-Jul-17.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FavoriteMovie.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " +
                FavoriteMovieEntry.TABLE_NAME + " ( " +
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT, " +
                FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT )";

        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
