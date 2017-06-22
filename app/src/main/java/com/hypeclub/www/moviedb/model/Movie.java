package com.hypeclub.www.moviedb.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jo on 19-Jun-17.
 */

public class Movie implements Parcelable {

    String id;
    String title;
    String posterPath;
    String overview;
    String vote_avg;
    String release_date;

    public Movie(String id, String title, String posterPath, String overview, String vote_avg, String release_date) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.vote_avg = vote_avg;
        this.release_date = release_date;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        vote_avg = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_avg() {
        return vote_avg;
    }

    public void setVote_avg(String vote_avg) {
        this.vote_avg = vote_avg;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(vote_avg);
        dest.writeString(release_date);
    }
}
