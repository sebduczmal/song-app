package com.sebduczmal.songapp.list;


import com.sebduczmal.songapp.data.SongModel;

import java.util.List;

public interface SongListView {
    void updateSongs(List<SongModel> songModels);
}
