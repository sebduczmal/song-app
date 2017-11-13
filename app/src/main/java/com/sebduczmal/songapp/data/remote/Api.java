package com.sebduczmal.songapp.data.remote;


import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("/search?entity=song")
    Single<ApiResponseModel> songs(@Query("term") String term, @Query("limit") int limit);

}
