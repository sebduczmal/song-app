package com.sebduczmal.songapp.data;


import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class SongModel implements Parcelable {

    @SerializedName(value = "trackName", alternate = {"Song Clean"}) private String title;
    @SerializedName(value = "artistName", alternate = {"ARTIST CLEAN"}) private String artist;
    @SerializedName(value = "releaseDate", alternate = {"Release Year"}) private String year;
    @SerializedName(value = "collectionName", alternate = {"COMBINED"}) private String album;
    @SerializedName("primaryGenreName") private String genre;
    @SerializedName("artworkUrl100") private String thumbnailUrl;

    protected SongModel(Parcel in) {
        title = in.readString();
        artist = in.readString();
        year = in.readString();
        album = in.readString();
        genre = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<SongModel> CREATOR = new Creator<SongModel>() {
        @Override
        public SongModel createFromParcel(Parcel in) {
            return new SongModel(in);
        }

        @Override
        public SongModel[] newArray(int size) {
            return new SongModel[size];
        }
    };

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean hasYear() {
        return !TextUtils.isEmpty(year);
    }

    public boolean hasGenre() {
        return !TextUtils.isEmpty(genre);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(artist);
        parcel.writeString(year);
        parcel.writeString(album);
        parcel.writeString(genre);
        parcel.writeString(thumbnailUrl);
    }
}
