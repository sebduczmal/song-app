package com.sebduczmal.songapp.data;


import com.google.gson.annotations.SerializedName;

public class SongModel {

    @SerializedName("trackName") private String title;
    @SerializedName("artistName") private String artist;
    @SerializedName("releaseDate") private String year;
    @SerializedName("artworkUrl100") private String thumbnailUrl;
    @SerializedName("primaryGenreName") private String genre;
    @SerializedName("collectionName") private String album;

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
