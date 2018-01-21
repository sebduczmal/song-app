package com.sebduczmal.songapp.di;

import android.content.Context;

import com.sebduczmal.songapp.data.DataRepoQualifier;
import com.sebduczmal.songapp.data.DataRepository;
import com.sebduczmal.songapp.data.local.AssetsHelper;
import com.sebduczmal.songapp.data.local.LocalSongsRepository;
import com.sebduczmal.songapp.data.remote.RemoteSongsRepository;

import javax.inject.Qualifier;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Component(modules = {ApplicationComponent.ApplicationModule.class})
public interface ApplicationComponent {

    Context exposeContext();

    DataRepository exposeRemoteSongsRepository();

    DataRepository exposeLocalSongsRepository();

    @Module
    class ApplicationModule {

        private final Context context;

        public ApplicationModule(Context context) {
            this.context = context;
        }

        @Provides
        public Context provideContext() {
            return context;
        }

        @Provides
        @DataRepoQualifier("remote")
        public DataRepository providesRemoteSongsRepository() {
            return new RemoteSongsRepository();
        }

        @Provides
        @DataRepoQualifier("local")
        public DataRepository providesLocalSongsRepository() {
            return new LocalSongsRepository(new AssetsHelper(context));
        }
    }
}
