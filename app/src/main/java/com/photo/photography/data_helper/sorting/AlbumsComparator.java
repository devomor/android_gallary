package com.photo.photography.data_helper.sorting;

import com.photo.photography.data_helper.Album;
import com.photo.photography.util.NumericComparators;

import java.util.Comparator;


public class AlbumsComparator {


    private static Comparator<Album> getComparator(SortingModes sortingMode, Comparator<Album> base) {
        switch (sortingMode) {
            case NAME:
                return getNameComparator(base);
            case SIZE:
                return getSizeComparator(base);
            case DATE_DAY:
            default:
                return getDateComparator(base);
            case NUMERIC:
                return getNumericComparator(base);
        }
    }

    public static Comparator<Album> getComparator(SortingModes sortingMode, SortingOrders sortingOrder) {

        Comparator<Album> comparator = getComparator(sortingMode, getBaseComparator(sortingOrder));

        return sortingOrder == SortingOrders.ASCENDING
                ? comparator : reverse(comparator);
    }

    private static Comparator<Album> reverse(Comparator<Album> comparator) {
        return (o1, o2) -> comparator.compare(o2, o1);
    }

    private static Comparator<Album> getBaseComparator(SortingOrders sortingOrder) {
        return sortingOrder == SortingOrders.ASCENDING
                ? getPinned() : getReversedPinned();
    }

    private static Comparator<Album> getPinned() {
        return (o1, o2) -> {
            if (o1.isPinned() == o2.isPinned()) return 0;
            return o1.isPinned() ? -1 : 1;
        };
    }

    private static Comparator<Album> getReversedPinned() {
        return (o1, o2) -> getPinned().compare(o2, o1);
    }

    private static Comparator<Album> getDateComparator(Comparator<Album> base) {
        return (a1, a2) -> {
            int res = base.compare(a1, a2);
            if (res == 0)
                return a1.getDateModified().compareTo(a2.getDateModified());
            return res;
        };
    }

    private static Comparator<Album> getNameComparator(Comparator<Album> base) {
        return (a1, a2) -> {
            int res = base.compare(a1, a2);
            if (res == 0)
                return a1.getName().toLowerCase().compareTo(a2.getName().toLowerCase());
            return res;
        };
    }

    private static Comparator<Album> getSizeComparator(Comparator<Album> base) {
        return (a1, a2) -> {
            int res = base.compare(a1, a2);
            if (res == 0)
                return a1.getCount() - a2.getCount();
            return res;
        };
    }

    private static Comparator<Album> getNumericComparator(Comparator<Album> base) {
        return (a1, a2) -> {
            int res = base.compare(a1, a2);
            if (res == 0)
                return NumericComparators.filevercmp(a1.getName().toLowerCase(), a2.getName().toLowerCase());
            return res;
        };
    }
}
