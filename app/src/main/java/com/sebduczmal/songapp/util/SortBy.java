package com.sebduczmal.songapp.util;


import com.sebduczmal.songapp.data.SongModel;

import java.util.Comparator;

public enum SortBy {
    TITLE((rhs, lhs) -> rhs.getTitle().compareTo(lhs.getTitle())),
    ARTIST((rhs, lhs) -> rhs.getArtist().compareTo(lhs.getArtist())),
    YEAR((rhs, lhs) -> {
        final Integer rhsYear = rhs.hasYear() ? Integer.valueOf(rhs.getYear()) : Integer.MAX_VALUE;
        final Integer lhsYear = lhs.hasYear() ? Integer.valueOf(lhs.getYear()) : Integer.MAX_VALUE;
        return rhsYear.compareTo(lhsYear);
    });

    private Comparator<SongModel> comparator;

    SortBy(Comparator<SongModel> comparator) {
        this.comparator = comparator;
    }

    public Comparator<SongModel> getComparator() {
        return comparator;
    }
}
