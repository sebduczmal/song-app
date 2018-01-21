package com.sebduczmal.songapp.data;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Sebastian Duczmal
 */
public interface DataRepository {

    Single<List<SongModel>> getSongsObservable(String query);

}
