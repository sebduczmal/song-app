package com.sebduczmal.songapp.data.local;

import com.google.gson.annotations.SerializedName;

/**
 * @author Sebastian Duczmal
 */
public class LocalSongModel {

    @SerializedName(value = "Song Clean") private String title;
    @SerializedName(value = "ARTIST CLEAN") private String artist;
    @SerializedName(value = "Release Year") private String year;
    @SerializedName(value = "collectionName", alternate = {"COMBINED"}) private String album;

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getYear() {
        return year;
    }

    public String getAlbum() {
        return album;
    }
}
