package com.sebduczmal.songapp.data;


import com.google.gson.annotations.SerializedName;

public class SongModel {

    @SerializedName(value = "trackName", alternate = {"Song Clean"}) private String title;
    @SerializedName(value = "artistName", alternate = {"ARTIST CLEAN"}) private String artist;
    @SerializedName(value = "releaseDate", alternate = {"Release Year"}) private String year;
    @SerializedName(value = "collectionName", alternate = {"COMBINED"}) private String album;
    @SerializedName("primaryGenreName") private String genre;
    @SerializedName("artworkUrl100") private String thumbnailUrl;

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getYear() {
        return year;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }
}
