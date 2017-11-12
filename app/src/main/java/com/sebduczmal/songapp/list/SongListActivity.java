package com.sebduczmal.songapp.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.sebduczmal.songapp.BaseActivity;
import com.sebduczmal.songapp.R;
import com.sebduczmal.songapp.databinding.ActivitySongListBinding;
import com.sebduczmal.songapp.list.di.DaggerSongListComponent;
import com.sebduczmal.songapp.list.di.SongListComponent;

import javax.inject.Inject;

public class SongListActivity extends BaseActivity implements SongListView {

    @Inject protected SongListPresenter presenter;
    private ActivitySongListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_list);
    }

    private void injectDependencies() {
        DaggerSongListComponent.builder().songListModule(new SongListComponent.SongListModule())
                .applicationComponent(getApplicationComponent()).build().inject(this);
    }
}
