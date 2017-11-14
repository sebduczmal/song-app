package com.sebduczmal.songapp.list;

import com.sebduczmal.songapp.BasePresenter;
import com.sebduczmal.songapp.data.local.LocalSongsRepository;
import com.sebduczmal.songapp.data.remote.RemoteSongsRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;


public class SongListPresenter extends BasePresenter<SongListView> {

    private final RemoteSongsRepository remoteSongsRepository;
    private final LocalSongsRepository localSongsRepository;

    public SongListPresenter(RemoteSongsRepository remoteSongsRepository, LocalSongsRepository
            localSongsRepository) {
        this.remoteSongsRepository = remoteSongsRepository;
        this.localSongsRepository = localSongsRepository;
    }

    public void loadSongs(String searchQuery) {
        disposables.add(localSongsRepository.getSongsObservable(searchQuery)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    view().showLoading();
                })
                .subscribe(songModels -> {
                    view().updateSongs(songModels);
                    view().hideLoading();
                }));
    }

    @Override
    protected Class viewClass() {
        return SongListView.class;
    }
}
