package com.sebduczmal.songapp.data.remote;


import com.google.gson.annotations.SerializedName;
import com.sebduczmal.songapp.data.SongModel;

import java.util.List;

public class ApiResponseModel {

    @SerializedName("resultCount") private int resultCount;
    @SerializedName("results") private List<SongModel> results;

    public int getResultCount() {
        return resultCount;
    }

    public List<SongModel> getResults() {
        return results;
    }
}
