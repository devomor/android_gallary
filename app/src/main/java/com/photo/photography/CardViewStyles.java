package com.photo.photography;


public enum CardViewStyles {

    MATERIAL(0, R.layout.card_albums_material),
    FLAT(1, R.layout.card_albums_flat),
    COMPACT(2, R.layout.card_album_compacts);

    private static final int size = CardViewStyles.values().length;

    int value;
    int layout;

    CardViewStyles(int value, int layout) {
        this.value = value;
        this.layout = layout;
    }

    public static int getSize() {
        return size;
    }

    public static CardViewStyles fromValue(int value) {
        switch (value) {
            case 0:
            default:
                return MATERIAL;
            case 1:
                return FLAT;
            case 2:
                return COMPACT;
        }
    }

    public int getLayout() {
        return layout;
    }

    public int getValue() {
        return value;
    }
}
