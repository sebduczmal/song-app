package com.sebduczmal.songapp.list;

import com.sebduczmal.songapp.BasePresenter;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.data.SongRepositoryType;
import com.sebduczmal.songapp.data.local.LocalSongsRepository;
import com.sebduczmal.songapp.data.remote.RemoteSongsRepository;
import com.sebduczmal.songapp.util.Constants;
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

    public void loadSongs(String query, SongRepositoryType repositoryType, SortBy sortBy) {
        disposables.clear();
        songModelSingle = getDataSource(query, repositoryType);
        disposables.add(songModelSingle.observeOn(AndroidSchedulers.mainThread())
                .flattenAsObservable(songModels -> songModels)
                .toSortedList(sortBy.getComparator())
                .doOnSubscribe(disposable -> {
                    view().showLoading();
                })
                .subscribe(songModels -> {
                    view().updateSongs(songModels);
                    view().hideLoading();
                    view().setSpinnersToHeaders();
                    getAllFilters(songModels);
                }, throwable -> {
                    view().onSongsLoadingError();
                    Timber.e(throwable, "Songs could not have been fetched");
                }));
    }

    private Single<List<SongModel>> getDataSource(String query, SongRepositoryType repositoryType) {
        switch (repositoryType) {
            case ALL:
                return getZippedDataSources(query);
            case REMOTE:
                return remoteSongsRepository.getSongsObservable(query);
            case LOCAL:
            default:
                return localSongsRepository.getSongsObservable(query);
        }
    }

    private Single<List<SongModel>> getZippedDataSources(String query) {
        return Single.zip(localSongsRepository.getSongsObservable(query),
                remoteSongsRepository.getSongsObservable(query),
                (songModelsRemote, songModelsLocal) -> {
                    songModelsRemote.addAll(songModelsLocal);
                    return songModelsRemote;
                });
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

    public void filterSongsByTitle(List<SongModel> songs, String title) {
        disposables.add(Observable.fromIterable(songs)
                .filter(songModel -> songModel.getTitle().equalsIgnoreCase(title))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songModels -> {
                    view().applyFilter(songModels);
                    getAllFilters(songModels);
                }));
    }

    public void filterSongsByArtist(List<SongModel> songs, String artist) {
        disposables.add(Observable.fromIterable(songs)
                .filter(songModel -> songModel.getArtist().equalsIgnoreCase(artist))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songModels -> {
                    view().applyFilter(songModels);
                    getAllFilters(songModels);
                }));
    }

    public void filterSongsByYear(List<SongModel> songs, String year) {
        disposables.add(Observable.fromIterable(songs)
                .filter(songModel -> songModel.getYear().equalsIgnoreCase(year))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songModels -> {
                    view().applyFilter(songModels);
                    getAllFilters(songModels);
                }));
    }

    public void getAllFilters(List<SongModel> songs) {
        getArtistFilters(songs);
        getTitleFilters(songs);
        getYearFilters(songs);
    }

    private void getTitleFilters(List<SongModel> songs) {
        disposables.add(Observable.fromIterable(songs)
                .distinct(songModel -> songModel.getTitle())
                .map(songModel -> songModel.getTitle())
                .sorted()
                .startWith(Constants.FILTER_TITLE)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> view().updateTitleFilter(strings)));
    }

    private void getArtistFilters(List<SongModel> songs) {
        disposables.add(Observable.fromIterable(songs)
                .distinct(songModel -> songModel.getArtist())
                .map(songModel -> songModel.getArtist())
                .sorted()
                .startWith(Constants.FILTER_ARTISTS)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> view().updateArtistFilter(strings)));
    }

    private void getYearFilters(List<SongModel> songs) {
        disposables.add(Observable.fromIterable(songs)
                .distinct(songModel -> songModel.getYear())
                .map(songModel -> songModel.getYear())
                .sorted()
                .startWith(Constants.FILTER_YEAR)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> view().updateYearFilter(strings)));
    }
}
