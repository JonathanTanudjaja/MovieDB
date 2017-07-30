package com.hypeclub.www.moviedb.model;

/**
 * Created by Jo on 30-Jul-17.
 */

public class MovieVideo {

    private String name;
    private String key;


    public MovieVideo(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
