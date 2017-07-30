package com.hypeclub.www.moviedb.model;

/**
 * Created by Jo on 29-Jul-17.
 */

public class Review {

    private String author;
    private String content;
    private String id;

    public Review(String id, String author, String content) {
        this.author = author;
        this.content = content;
        this.id = id;
    }


    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }
}
