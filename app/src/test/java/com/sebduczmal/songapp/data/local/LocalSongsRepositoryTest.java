package com.sebduczmal.songapp.data.local;

import com.sebduczmal.songapp.BuildConfig;
import com.sebduczmal.songapp.data.SongModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LocalSongsRepositoryTest {

    private LocalSongsRepository sut;
    private TestObserver testObserver;
    private TestScheduler testScheduler;
    private List<SongModel> mockSongs;
    @Mock private AssetsHelper mockAssetsHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockSongs = createMockSongsList(5);
        when(mockAssetsHelper.getLocalSongsList()).thenReturn(mockSongs);
        sut = new LocalSongsRepository(mockAssetsHelper);
        testScheduler = new TestScheduler();
    }

    @Test
    public void getSongsObservable_emptyQuery_shouldReturnEmptyList() {
        testObserver = sut.getSongsObservable("").test();
        testScheduler.triggerActions();

        List<SongModel> results = (List<SongModel>) testObserver.values().get(0);

        assertTrue(results.isEmpty());
    }

    @Test
    public void getSongsObservable_nonEmptyQuery_shouldReturnItemsMatchingQuery() throws Exception {
        testObserver = sut.getSongsObservable("combined").test();
        testObserver.await();

        testScheduler = new TestScheduler();
        testScheduler.triggerActions();

        List<SongModel> results = (List<SongModel>) testObserver.values().get(0);

        assertEquals(5, results.size());
    }

    private List<SongModel> createMockSongsList(int songsQuantity) {
        List<SongModel> songModels = new ArrayList<>();
        for (int i = 0; i < songsQuantity; i++) {
            SongModel mockSongModel = mock(SongModel.class);
            when(mockSongModel.getAlbum()).thenReturn("combined" + i);
            songModels.add(mockSongModel);
        }
        return songModels;
    }
}