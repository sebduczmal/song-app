package com.sebduczmal.songapp.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.sebduczmal.songapp.BaseActivity;
import com.sebduczmal.songapp.R;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.data.SongRepositoryType;
import com.sebduczmal.songapp.databinding.ActivitySongListBinding;
import com.sebduczmal.songapp.details.SongDetailsFragment;
import com.sebduczmal.songapp.list.di.DaggerSongListComponent;
import com.sebduczmal.songapp.list.di.SongListComponent;
import com.sebduczmal.songapp.util.SortBy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class SongListActivity extends BaseActivity implements SongListView, NavigationView
        .OnNavigationItemSelectedListener, OnSongListClickListener {

    @Inject protected SongListPresenter presenter;
    private ActivitySongListBinding binding;
    private SongListAdapter songListAdapter;
    private CompositeDisposable viewsDisposables = new CompositeDisposable();
    private SongRepositoryType currentRepository;
    private SortBy sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_list);
        setupSongsList();
        setupDrawer();
        currentRepository = SongRepositoryType.ALL;
        sortBy = SortBy.TITLE;
    }

    private void injectDependencies() {
        DaggerSongListComponent.builder().songListModule(new SongListComponent.SongListModule())
                .applicationComponent(getApplicationComponent()).build().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupSearchBar();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        viewsDisposables.clear();
        presenter.detachView();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showLoading() {
        binding.loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void updateSongs(List<SongModel> songModels) {
        songListAdapter.updateSongs(songModels);
    }

    @Override
    public void onSongsLoadingError() {
        binding.loadingBar.setVisibility(View.GONE);
        Toast.makeText(this, R.string.songs_loading_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSongItemClick(SongModel songModel) {
        SongDetailsFragment songDetailsFragment = SongDetailsFragment.newInstance(songModel);
        songDetailsFragment.show(getSupportFragmentManager(), "details-dialog-fragment");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_all:
                changeRepositoryType(SongRepositoryType.ALL);
                break;
            case R.id.nav_local:
                changeRepositoryType(SongRepositoryType.LOCAL);
                break;
            case R.id.nav_remote:
                changeRepositoryType(SongRepositoryType.REMOTE);
                break;
            case R.id.sort_title:
                sortBy = SortBy.TITLE;
                presenter.sortSongs(songListAdapter.getSongs(), sortBy);
                break;
            case R.id.sort_artist:
                sortBy = SortBy.ARTIST;
                presenter.sortSongs(songListAdapter.getSongs(), sortBy);
                break;
            case R.id.sort_year:
                sortBy = SortBy.YEAR;
                presenter.sortSongs(songListAdapter.getSongs(), sortBy);
                break;
            default:
                Timber.d("No action handled");
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupSongsList() {
        songListAdapter = new SongListAdapter();
        songListAdapter.setOnSongListClickListener(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        binding.recyclerSongs.setLayoutManager(linearLayoutManager);
        binding.recyclerSongs.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        binding.recyclerSongs.setAdapter(songListAdapter);
    }

    private void setupSearchBar() {
        viewsDisposables.add(RxTextView.afterTextChangeEvents(binding.inputSearch)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewAfterTextChangeEvent -> presenter
                        .loadSongs(binding.inputSearch.getText().toString(), currentRepository,
                                sortBy)));
    }

    private void setupDrawer() {
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R
                .string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    private void changeRepositoryType(SongRepositoryType type) {
        currentRepository = type;
        final String inputSearchText = binding.inputSearch.getText().toString();
        binding.inputSearch.setText(inputSearchText);
        binding.inputSearch.setSelection(inputSearchText.length());
    }
}
