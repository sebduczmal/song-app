package com.sebduczmal.songapp.di;

import android.content.Context;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Component(modules = {ApplicationComponent.ApplicationModule.class})
public interface ApplicationComponent {

    Context exposeContext();

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
    }
}
