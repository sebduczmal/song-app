package com.sebduczmal.songapp.list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.sebduczmal.songapp.util.Constants;
import com.sebduczmal.songapp.util.SortBy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class SongListActivity extends BaseActivity implements SongListView,
        OnSongListClickListener, AdapterView.OnItemSelectedListener {

    @Inject protected SongListPresenter presenter;
    private ActivitySongListBinding binding;
    private SongListAdapter songListAdapter;
    private CompositeDisposable viewsDisposables = new CompositeDisposable();
    private SongRepositoryType currentRepository;
    private SortBy sortBy;
    private ArrayAdapter<String> titleFilterAdapter;
    private ArrayAdapter<String> artistFilterAdapter;
    private ArrayAdapter<String> yearFilterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_list);
        setupSongsList();
        setupToolbar();
        setupSpinnerAdapters();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_all:
                changeRepositoryType(SongRepositoryType.ALL);
                item.setChecked(true);
                break;
            case R.id.nav_local:
                changeRepositoryType(SongRepositoryType.LOCAL);
                item.setChecked(true);
                break;
            case R.id.nav_remote:
                changeRepositoryType(SongRepositoryType.REMOTE);
                item.setChecked(true);
                break;
            case R.id.sort_title:
                sortBy = SortBy.TITLE;
                presenter.sortSongs(songListAdapter.getSongsToDisplay(), sortBy);
                item.setChecked(true);
                break;
            case R.id.sort_artist:
                sortBy = SortBy.ARTIST;
                presenter.sortSongs(songListAdapter.getSongsToDisplay(), sortBy);
                item.setChecked(true);
                break;
            case R.id.sort_year:
                sortBy = SortBy.YEAR;
                presenter.sortSongs(songListAdapter.getSongsToDisplay(), sortBy);
                item.setChecked(true);
                break;
            case R.id.clear_filters:
                clearFilters();
                break;
            default:
                Timber.d("No action handled");
        }
        return true;
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
        songListAdapter.updateSongsBase(songModels);
    }

    @Override
    public void onSongsLoadingError() {
        binding.loadingBar.setVisibility(View.GONE);
        Toast.makeText(this, R.string.songs_loading_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateTitleFilter(List<String> filters) {
        titleFilterAdapter.clear();
        titleFilterAdapter.addAll(filters);
    }

    @Override
    public void updateArtistFilter(List<String> artists) {
        artistFilterAdapter.clear();
        artistFilterAdapter.addAll(artists);
    }

    @Override
    public void updateYearFilter(List<String> years) {
        yearFilterAdapter.clear();
        yearFilterAdapter.addAll(years);
    }

    @Override
    public void setSpinnersToHeaders() {
        binding.spinnerArtistFilter.setSelection(Constants.BASE_INDEX);
        binding.spinnerTitleFilter.setSelection(Constants.BASE_INDEX);
        binding.spinnerYearFilter.setSelection(Constants.BASE_INDEX);
    }

    @Override
    public void applyFilter(List<SongModel> songs) {
        songListAdapter.updateSongsToDisplay(songs);
    }

    @Override
    public void onSongItemClick(SongModel songModel) {
        SongDetailsFragment songDetailsFragment = SongDetailsFragment.newInstance(songModel);
        songDetailsFragment.show(getSupportFragmentManager(), "details-dialog-fragment");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        List<SongModel> listToFilter = songListAdapter.getSongsToDisplay();
        switch (parent.getId()) {
            case R.id.spinner_title_filter:
                presenter.filterSongsByTitle(listToFilter, titleFilterAdapter.getItem(position));
                break;
            case R.id.spinner_artist_filter:
                presenter.filterSongsByArtist(listToFilter, artistFilterAdapter.getItem(position));
                break;
            case R.id.spinner_year_filter:
                presenter.filterSongsByYear(listToFilter, yearFilterAdapter.getItem(position));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }

    private void changeRepositoryType(SongRepositoryType type) {
        currentRepository = type;
        final String inputSearchText = binding.inputSearch.getText().toString();
        binding.inputSearch.setText(inputSearchText);
        binding.inputSearch.setSelection(inputSearchText.length());
    }

    private void setupSpinnerAdapters() {
        titleFilterAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        titleFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTitleFilter.setAdapter(titleFilterAdapter);
        binding.spinnerTitleFilter.setOnItemSelectedListener(this);

        artistFilterAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        artistFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerArtistFilter.setAdapter(artistFilterAdapter);
        binding.spinnerArtistFilter.setOnItemSelectedListener(this);

        yearFilterAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        yearFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerYearFilter.setAdapter(yearFilterAdapter);
        binding.spinnerYearFilter.setOnItemSelectedListener(this);
    }

    private void clearFilters() {
        songListAdapter.updateSongsToDisplay(songListAdapter.getSongsBase());
        setSpinnersToHeaders();
        presenter.getAllFilters(songListAdapter.getSongsBase());
    }
}
