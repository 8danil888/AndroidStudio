package com.topcommentapp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class RedditResponse {
    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public class Child {
        @SerializedName("data")
        private Post data;

        public Post getData() {
            return data;
        }
    }

    public static class Data {
        @SerializedName("after")
        private String after;

        @SerializedName("before")
        private String before;

        @SerializedName("children")
        private List<Child> children;

        public String getAfter() {
            return after;
        }

        public String getBefore() {
            return before;
        }

        public List<Post> getChildrenPosts() {
            List<Post> postList = new ArrayList<>();
            for (Child child : children) {
                postList.add(child.getData());
            }
            return postList;
        }
    }
}