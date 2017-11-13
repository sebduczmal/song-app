package com.sebduczmal.songapp.list;


import com.sebduczmal.songapp.data.SongModel;

import java.util.List;

public interface SongListView {
    void showLoading();

    void hideLoading();

    void updateSongs(List<SongModel> songModels);
}
