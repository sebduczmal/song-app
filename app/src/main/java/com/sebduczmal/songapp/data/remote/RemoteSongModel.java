package com.sebduczmal.songapp.data.remote;

import com.google.gson.annotations.SerializedName;

/**
 * @author Sebastian Duczmal
 */
public class RemoteSongModel {

    @SerializedName(value = "trackName") private String title;
    @SerializedName(value = "artistName") private String artist;
    @SerializedName(value = "releaseDate") private String year;
    @SerializedName(value = "collectionName") private String album;
    @SerializedName("primaryGenreName") private String genre;
    @SerializedName("artworkUrl100") private String thumbnailUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
