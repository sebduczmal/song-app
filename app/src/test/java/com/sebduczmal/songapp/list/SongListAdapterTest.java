package com.sebduczmal.songapp.list;

import com.sebduczmal.songapp.BuildConfig;
import com.sebduczmal.songapp.data.SongModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SongListAdapterTest {

    private SongListAdapter sut;

    @Mock SongModel songModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sut = new SongListAdapter();
    }

    @Test
    public void updateSongsToDisplay_shouldNotEqualBaseSongs() {
        sut.updateSongsBase(Arrays.asList(songModel, songModel, songModel));
        sut.updateSongsToDisplay(Arrays.asList(songModel, songModel));

        assertEquals(2, sut.getItemCount());
        assertEquals(sut.getSongsToDisplay().size(), sut.getItemCount());
        assertNotEquals(sut.getSongsBase().size(), sut.getItemCount());
    }

    @Test
    public void updateSongsToDisplay_shouldUpdateListOfDisplayedItems() {
        sut.updateSongsToDisplay(Arrays.asList(songModel, songModel, songModel));

        assertEquals(3, sut.getSongsToDisplay().size());
    }

    @Test
    public void updateSongsBase_shouldUpdateListOfOriginalSongs() {
        sut.updateSongsBase(Arrays.asList(songModel, songModel, songModel));

        assertEquals(3, sut.getSongsBase().size());
    }

    @Test
    public void updateSongsBase_shouldUpdateSongsToDisplay() {
        sut.updateSongsToDisplay(Arrays.asList(songModel, songModel));
        assertEquals(2, sut.getSongsToDisplay().size());

        sut.updateSongsBase(Arrays.asList(songModel, songModel, songModel));
        assertEquals(3, sut.getSongsBase().size());
        assertEquals(sut.getSongsBase().size(), sut.getSongsToDisplay().size());
        assertEquals(sut.getSongsBase(), sut.getSongsToDisplay());
    }
}