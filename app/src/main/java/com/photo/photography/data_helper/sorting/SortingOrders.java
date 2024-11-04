package com.photo.photography.data_helper.sorting;


public enum SortingOrders {
    ASCENDING(1), DESCENDING(0);

    int value;

    SortingOrders(int value) {
        this.value = value;
    }

    public static SortingOrders fromValue(boolean value) {
        return value ? ASCENDING : DESCENDING;
    }

    public static SortingOrders fromValue(int value) {
        return value == 0 ? DESCENDING : ASCENDING;
    }

    public int getValue() {
        return value;
    }

    public boolean isAscending() {
        return value == ASCENDING.getValue();
    }
}
