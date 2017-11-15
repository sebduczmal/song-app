package com.sebduczmal.songapp.list;

import com.sebduczmal.songapp.BasePresenter;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.data.SongRepositoryType;
import com.sebduczmal.songapp.data.local.LocalSongsRepository;
import com.sebduczmal.songapp.data.remote.RemoteSongsRepository;
import com.sebduczmal.songapp.util.SortBy;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class SongListPresenter extends BasePresenter<SongListView> {

    private final RemoteSongsRepository remoteSongsRepository;
    private final LocalSongsRepository localSongsRepository;
    private Single<List<SongModel>> songModelSingle;

    public SongListPresenter(RemoteSongsRepository remoteSongsRepository, LocalSongsRepository
            localSongsRepository) {
        this.remoteSongsRepository = remoteSongsRepository;
        this.localSongsRepository = localSongsRepository;
    }

    public void loadSongs(String searchQuery, SongRepositoryType songRepositoryType, SortBy
            sortBy) {
        disposables.clear();
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
                .flattenAsObservable(songModels -> songModels)
                .toSortedList(sortBy.getComparator())
                .doOnSubscribe(disposable -> {
                    view().showLoading();
                })
                .subscribe(songModels -> {
                    view().updateSongs(songModels);
                    view().hideLoading();
                }, throwable -> {
                    view().onSongsLoadingError();
                    Timber.e(throwable, "Songs could not have been fetched");
                }));
    }

    @Override
    protected Class viewClass() {
        return SongListView.class;
    }

    public void sortSongs(List<SongModel> songs, SortBy sortBy) {
        disposables.add(Observable.fromIterable(songs).toSortedList(sortBy.getComparator())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songModels -> view().updateSongs(songModels)));
    }
}
