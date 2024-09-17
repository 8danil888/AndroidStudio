package com.topcommentapp;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("num_comments")
    private int numComments;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("url")
    private String imageUrl;

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getNumComments() {
        return numComments;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
