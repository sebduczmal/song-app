package com.sebduczmal.songapp.list;


import com.sebduczmal.songapp.data.SongModel;

import java.util.List;

public interface SongListView {
    void showLoading();

    void hideLoading();

    void updateSongs(List<SongModel> songModels);

    void onSongsLoadingError();

    void updateTitleFilter(List<String> titles);

    void updateArtistFilter(List<String> artists);

    void updateYearFilter(List<String> years);

    void setSpinnersToHeaders();

    void applyFilter(List<SongModel> songs);
}
