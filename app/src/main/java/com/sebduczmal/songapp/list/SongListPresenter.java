package com.sebduczmal.songapp.list;

import com.sebduczmal.songapp.BasePresenter;


public class SongListPresenter extends BasePresenter<SongListView> {

    @Override
    protected Class viewClass() {
        return SongListView.class;
    }
}
