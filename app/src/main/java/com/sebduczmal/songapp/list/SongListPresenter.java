package com.sebduczmal.songapp.list;

import com.sebduczmal.songapp.BasePresenter;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.data.SongRepositoryType;
import com.sebduczmal.songapp.data.local.LocalSongsRepository;
import com.sebduczmal.songapp.data.remote.RemoteSongsRepository;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class SongListPresenter extends BasePresenter<SongListView> {

    private final RemoteSongsRepository remoteSongsRepository;
    private final LocalSongsRepository localSongsRepository;
    private Single<List<SongModel>> songModelSingle;

    public SongListPresenter(RemoteSongsRepository remoteSongsRepository, LocalSongsRepository
            localSongsRepository) {
        this.remoteSongsRepository = remoteSongsRepository;
        this.localSongsRepository = localSongsRepository;
    }

    public void loadSongs(String searchQuery, SongRepositoryType songRepositoryType) {
        switch (songRepositoryType) {
            case ALL:
                songModelSingle = Single.zip(localSongsRepository.getSongsObservable(searchQuery),
                        remoteSongsRepository.getSongsObservable(searchQuery),
                        (songModelsRemote, songModelsLocal) -> {
                            songModelsRemote.addAll(songModelsLocal);
                            return songModelsRemote;
                        });
                break;
            case LOCAL:
                songModelSingle = localSongsRepository.getSongsObservable(searchQuery);
                break;
            case REMOTE:
                songModelSingle = remoteSongsRepository.getSongsObservable(searchQuery);
                break;
        }
        disposables.add(songModelSingle.observeOn(AndroidSchedulers.mainThread())
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
