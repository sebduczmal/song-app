package com.sebduczmal.songapp.data.local;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sebduczmal.songapp.data.DataRepository;
import com.sebduczmal.songapp.data.SongModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LocalSongsRepository implements DataRepository {

    private AssetsHelper assetsHelper;

    public LocalSongsRepository(AssetsHelper assetsHelper) {
        this.assetsHelper = assetsHelper;
    }

    @Override
    public Single<List<SongModel>> getSongsObservable(String searchQuery) {
        if (TextUtils.isEmpty(searchQuery)) {
            return Single.just(Collections.emptyList());
        } else {
            return Observable
                    .fromIterable(assetsHelper.getLocalSongsList())
                    .subscribeOn(Schedulers.io())
                    .filter(localSongModel -> localSongModel.getAlbum().toLowerCase().contains(searchQuery
                            .toLowerCase()))
                    .map(new Function<LocalSongModel, SongModel>() {
                        @Override
                        public SongModel apply(LocalSongModel localSongModel) throws Exception {
                            return null;
                        }
                    })
                    .toList();
        }
    }
}
