package com.sebduczmal.songapp.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.sebduczmal.songapp.BaseActivity;
import com.sebduczmal.songapp.R;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.databinding.ActivitySongListBinding;
import com.sebduczmal.songapp.list.di.DaggerSongListComponent;
import com.sebduczmal.songapp.list.di.SongListComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SongListActivity extends BaseActivity implements SongListView {

    @Inject protected SongListPresenter presenter;
    private ActivitySongListBinding binding;
    private SongListAdapter songListAdapter;
    private CompositeDisposable viewsDisposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_list);
        setupSongsList();
        setupViews();
    }

    private void injectDependencies() {
        DaggerSongListComponent.builder().songListModule(new SongListComponent.SongListModule())
                .applicationComponent(getApplicationComponent()).build().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        viewsDisposables.clear();
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void showLoading() {
        showProgressDialog(R.string.loading_songs);
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    @Override
    public void updateSongs(List<SongModel> songModels) {
        songListAdapter.updateSongs(songModels);
    }

    private void setupSongsList() {
        songListAdapter = new SongListAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerSongs.setLayoutManager(linearLayoutManager);
        binding.recyclerSongs.setAdapter(songListAdapter);
    }

    private void setupViews() {
        viewsDisposables.add(RxTextView.afterTextChangeEvents(binding.inputSearch)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewAfterTextChangeEvent -> presenter
                        .loadSongs(binding.inputSearch.getText().toString())));
    }
}
