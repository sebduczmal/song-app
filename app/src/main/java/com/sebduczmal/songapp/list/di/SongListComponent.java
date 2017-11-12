package com.sebduczmal.songapp.list.di;

import com.sebduczmal.songapp.di.ApplicationComponent;
import com.sebduczmal.songapp.list.SongListActivity;
import com.sebduczmal.songapp.list.SongListPresenter;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Component(dependencies = {ApplicationComponent.class},
        modules = {SongListComponent.SongListModule.class})
public interface SongListComponent {

    void inject(SongListActivity songListActivity);

    @Module
    class SongListModule {

        @Provides
        public SongListPresenter provideShoppingListPresenter() {
            return new SongListPresenter();
        }
    }
}
