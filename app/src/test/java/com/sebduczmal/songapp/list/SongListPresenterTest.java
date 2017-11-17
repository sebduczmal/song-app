package com.sebduczmal.songapp.list;

import com.sebduczmal.songapp.BuildConfig;
import com.sebduczmal.songapp.data.SongModel;
import com.sebduczmal.songapp.data.SongRepositoryType;
import com.sebduczmal.songapp.data.local.LocalSongsRepository;
import com.sebduczmal.songapp.data.remote.RemoteSongsRepository;
import com.sebduczmal.songapp.util.SortBy;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SongListPresenterTest {
    private static final int LOCAL_SONGS_NUM = 3;
    private static final int REMOTE_SONGS_NUM = 5;

    private SongListPresenter sut;

    @Mock private SongListView mockView;
    @Mock LocalSongsRepository mockLocalSongsRepository;
    @Mock RemoteSongsRepository mockRemoteSongsRepository;
    private List<SongModel> mockLocalSongs;
    private List<SongModel> mockRemoteSongs;

    @BeforeClass
    public static void setUpRxSchedulers() {
        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockLocalSongs = createMockSongsList(LOCAL_SONGS_NUM);
        mockRemoteSongs = createMockSongsList(REMOTE_SONGS_NUM);
        when(mockLocalSongsRepository.getSongsObservable(anyString())).thenReturn(Observable
                .fromIterable(mockLocalSongs).toList());
        when(mockRemoteSongsRepository.getSongsObservable(anyString())).thenReturn(Observable
                .fromIterable(mockRemoteSongs).toList());
        sut = new SongListPresenter(mockRemoteSongsRepository, mockLocalSongsRepository);
        sut.attachView(mockView);
    }

    @Test
    public void loadSongs_singleRepository_shouldUpdateView() {
        sut.loadSongs("query", SongRepositoryType.LOCAL, SortBy.TITLE);

        verify(mockView).showLoading();
        verify(mockView).updateSongs(mockLocalSongs);
        verify(mockView).hideLoading();
        verify(mockView).setSpinnersToHeaders();
    }

    @Test
    public void loadSongs_allRepositories_shouldUpdateView() {
        sut.loadSongs("query", SongRepositoryType.ALL, SortBy.TITLE);

        List<SongModel> songs = new ArrayList<>();
        songs.addAll(mockLocalSongs);
        songs.addAll(mockRemoteSongs);
        songs.sort(SortBy.TITLE.getComparator());

        verify(mockView).showLoading();
        verify(mockView).updateSongs(songs);
        verify(mockView).hideLoading();
        verify(mockView).setSpinnersToHeaders();
    }

    @Test
    public void sortSongs_shouldSortSongsByTitle() {
        testSortingLists(SortBy.TITLE);
    }

    @Test
    public void sortSongs_shouldSortSongsByArtist() {
        testSortingLists(SortBy.ARTIST);
    }

    @Test
    public void sortSongs_shouldSortSongsByYear() {
        testSortingLists(SortBy.YEAR);
    }

    @Test
    public void detachView_shouldNotCallAnyViewMethod() {
        sut.detachView();

        verifyNoMoreInteractions(mockView);
    }

    @After
    public void tearDown() {
        sut.detachView();
        RxAndroidPlugins.reset();
        RxJavaPlugins.reset();
    }

    private void testSortingLists(SortBy sortBy) {
        List<SongModel> expectedOrderSongs = createMockSongsList(10);
        List<SongModel> shuffledSongs = new ArrayList<>(expectedOrderSongs);
        Collections.reverse(shuffledSongs);

        assertNotEquals(expectedOrderSongs, shuffledSongs);

        when(mockLocalSongsRepository.getSongsObservable(anyString()))
                .thenReturn(Observable.fromIterable(shuffledSongs).toList());
        sut.loadSongs("query", SongRepositoryType.LOCAL, sortBy);

        verify(mockView).updateSongs(expectedOrderSongs);
    }

    private List<SongModel> createMockSongsList(int songsQuantity) {
        List<SongModel> songModels = new ArrayList<>();
        for (int i = 0; i < songsQuantity; i++) {
            SongModel mockSongModel = mock(SongModel.class);
            when(mockSongModel.getTitle()).thenReturn("title" + i);
            when(mockSongModel.getArtist()).thenReturn("artist" + i);
            when(mockSongModel.getAlbum()).thenReturn("album" + i);
            when(mockSongModel.hasYear()).thenReturn(true);
            when(mockSongModel.getYear()).thenReturn(String.valueOf(i));
            songModels.add(mockSongModel);
        }
        return songModels;
    }
}