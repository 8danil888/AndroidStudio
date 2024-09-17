package com.topcommentapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RedditApiService {
    @GET("r/popular/top/.json")
    Call<RedditResponse> getPopularPosts(@Query("limit") int limit, @Query("t") String timeFrame, @Query("after") String after);
}