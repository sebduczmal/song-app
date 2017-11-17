package com.sebduczmal.songapp.data.local;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sebduczmal.songapp.data.SongModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class AssetsHelper {

    private static final String LOCAL_SONGS_FILE_NAME = "songs-list.json";

    private Context context;

    public AssetsHelper(Context context) {
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

    public List<SongModel> getLocalSongsList() {
        final Type type = new TypeToken<List<SongModel>>() {}.getType();
        return new Gson().fromJson(loadLocalSongsJson(), type);
    }
}
