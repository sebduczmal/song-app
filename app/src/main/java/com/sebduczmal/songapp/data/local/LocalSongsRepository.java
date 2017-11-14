package com.sebduczmal.songapp.data.local;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sebduczmal.songapp.data.SongModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class LocalSongsRepository {

    private static final String LOCAL_SONGS_FILE_NAME = "songs-list.json";

    private Context context;

    public LocalSongsRepository(Context context) {
        this.context = context;
    }

    private String loadLocalSongsJson() {
        String json = null;
        try {
            final InputStream inputStream = context.getAssets().open(LOCAL_SONGS_FILE_NAME);
            final int bufferSize = inputStream.available();
            final byte buffer[] = new byte[bufferSize];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return json;
        }
        return json;
    }

    private List<SongModel> getLocalSongsList() {
        final Type type = new TypeToken<List<SongModel>>() {}.getType();
        return new Gson().fromJson(loadLocalSongsJson(), type);
    }

    public Single<List<SongModel>> getSongsObservable(String searchQuery) {
        if (TextUtils.isEmpty(searchQuery)) {
            return Single.just(Collections.emptyList());
        } else {
            return Observable
                    .fromIterable(getLocalSongsList())
                    .subscribeOn(Schedulers.io())
                    .filter(songModel -> songModel.getAlbum().toLowerCase().contains(searchQuery
                            .toLowerCase()))
                    .toList();
        }
    }
}
