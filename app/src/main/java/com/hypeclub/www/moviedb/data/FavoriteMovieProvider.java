package com.hypeclub.www.moviedb.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Jo on 01-Aug-17.
 */

public class FavoriteMovieProvider extends ContentProvider {

    public static final int CODE_FAVORITE_MOVIE = 200;
    public static final int CODE_FAVORITE_MOVIE_WITH_MOVIEID = 202;

    public static final UriMatcher URI_MATCHER = buildUriMatcher();

    private FavoriteMovieDbHelper favoriteMovieDbHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority,FavoriteMoviesContract.PATH_FAVORITE_MOVIE, CODE_FAVORITE_MOVIE);

        uriMatcher.addURI(authority,FavoriteMoviesContract.PATH_FAVORITE_MOVIE + "/#", CODE_FAVORITE_MOVIE_WITH_MOVIEID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        favoriteMovieDbHelper = new FavoriteMovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case CODE_FAVORITE_MOVIE:
                cursor = favoriteMovieDbHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;

            case CODE_FAVORITE_MOVIE_WITH_MOVIEID:

                String movieID = uri.getLastPathSegment();
                selectionArgs = new String[]{movieID};

                cursor = favoriteMovieDbHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        FavoriteMoviesContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (URI_MATCHER.match(uri)) {

            case CODE_FAVORITE_MOVIE:
                long rowID = favoriteMovieDbHelper.getWritableDatabase().insert(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        null,
                        values
                );

                if (rowID > 0) {
                    Uri itemUri = ContentUris.withAppendedId(uri,rowID);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return itemUri;
                }

                throw new SQLException("Failed to add a new record");

            default:
                throw new RuntimeException("Unknown uri:  " + uri);

        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) selection = "1";

        switch (URI_MATCHER.match(uri)) {
            case CODE_FAVORITE_MOVIE: {

                numRowsDeleted = favoriteMovieDbHelper.getWritableDatabase().delete(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) getContext().getContentResolver().notifyChange(uri, null);

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        throw new RuntimeException("Unknown uri:  " + uri);
    }
}
