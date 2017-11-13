package com.sebduczmal.songapp.list;

import android.text.TextUtils;

import com.sebduczmal.songapp.BasePresenter;
import com.sebduczmal.songapp.data.remote.RemoteSongsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SongListPresenter extends BasePresenter<SongListView> {

    private final RemoteSongsRepository remoteSongsRepository;

    public SongListPresenter(RemoteSongsRepository remoteSongsRepository) {
        this.remoteSongsRepository = remoteSongsRepository;
    }

    public void loadSongs(String searchQuery) {
        if (TextUtils.isEmpty(searchQuery)) {
            return;
        }
        disposables.add(remoteSongsRepository
                .songs(searchQuery, RemoteSongsRepository.RESPONSE_LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(apiResponseModel -> view().updateSongs(apiResponseModel.getResults())));
    }

    @Override
    protected Class viewClass() {
        return SongListView.class;
    }
}
