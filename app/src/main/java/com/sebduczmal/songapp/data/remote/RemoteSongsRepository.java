package com.sebduczmal.songapp.data.remote;


import android.text.TextUtils;

import com.sebduczmal.songapp.BuildConfig;
import com.sebduczmal.songapp.data.DataRepository;
import com.sebduczmal.songapp.data.SongModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteSongsRepository implements DataRepository, Api {

    public static final int RESPONSE_LIMIT = 25;
    private static final String API_URL = "https://itunes.apple.com/";
    private static final String ISO_DATE_PATTERN = "yyyy-MM-dd'T'hh:mm:ss'Z'";

    private final Api api;

    public RemoteSongsRepository() {
        api = initializeApi();
    }

    @Override
    public Single<ApiResponseModel> songs(String term, int limit) {
        return api.songs(term, limit);
    }

    @Override
    public Single<List<SongModel>> getSongsObservable(String searchQuery) {
        if (TextUtils.isEmpty(searchQuery)) {
            return Single.just(Collections.emptyList());
        } else {
            final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO_DATE_PATTERN);
            final Calendar calendar = Calendar.getInstance();
            return songs(searchQuery, RESPONSE_LIMIT)
                    .subscribeOn(Schedulers.io())
                    .flattenAsObservable(apiResponseModel -> apiResponseModel.getResults())
                    .map(songModel -> {
                        calendar.setTime(dateFormat.parse(songModel.getYear()));
                        songModel.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
                        return songModel;
                    }).toList();
        }
    }

    private Api initializeApi() {
        final OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        addDebugInterceptor(okHttpClientBuilder);

        return new Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(API_URL)
                .build().create(Api.class);
    }

    private void addDebugInterceptor(OkHttpClient.Builder httpClient) {
        if (BuildConfig.DEBUG) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor
                    (HttpLoggingInterceptor.Logger.DEFAULT);
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor);
        }
    }
}
