package com.photo.photography.data_helper.sorting;

import androidx.annotation.NonNull;

import com.photo.photography.data_helper.AlbumSetting;
import com.photo.photography.data_helper.Media;
import com.photo.photography.time_line.datas.TimelineHeader;
import com.photo.photography.util.NumericComparators;

import java.util.Comparator;


public class MediaComparator {

    public static Comparator<Media> getComparator(AlbumSetting settings) {
        return getComparator(settings.getSortingMode(), settings.getSortingOrder());
    }

    public static Comparator<Media> getComparator(SortingModes sortingMode, SortingOrders sortingOrder) {
        return sortingOrder == SortingOrders.ASCENDING
                ? getComparator(sortingMode) : reverse(getComparator(sortingMode));
    }

    public static Comparator<TimelineHeader> getTimelineComparator(@NonNull SortingOrders sortingOrder) {
        return sortingOrder.isAscending() ? getTimelineComparator() : reverse(getTimelineComparator());
    }

    public static Comparator<Media> getComparator(SortingModes sortingMode) {
        switch (sortingMode) {
            case NAME:
                return getNameComparator();
            case DATE_DAY:
            default:
                return getDateComparator();
            case SIZE:
                return getSizeComparator();
            case TYPE:
                return getTypeComparator();
            case NUMERIC:
                return getNumericComparator();
        }
    }

    private static <T> Comparator<T> reverse(Comparator<T> comparator) {
        return (o1, o2) -> comparator.compare(o2, o1);
    }

    private static Comparator<Media> getDateComparator() {
        return (f1, f2) -> f1.getDateModified().compareTo(f2.getDateModified());
    }

    private static Comparator<Media> getNameComparator() {
        return (f1, f2) -> f1.getPath().compareTo(f2.getPath());
    }

    private static Comparator<Media> getSizeComparator() {
        return (f1, f2) -> Long.compare(f1.getSize(), f2.getSize());
    }

    private static Comparator<Media> getTypeComparator() {
        return (f1, f2) -> f1.getMimeType().compareTo(f2.getMimeType());
    }

    private static Comparator<Media> getNumericComparator() {
        return (f1, f2) -> NumericComparators.filevercmp(f1.getPath(), f2.getPath());
    }

    private static Comparator<TimelineHeader> getTimelineComparator() {
        return (t1, t2) -> t1.getDate().compareTo(t2.getDate());
    }
}
