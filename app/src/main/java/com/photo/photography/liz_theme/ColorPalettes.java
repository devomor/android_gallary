package com.photo.photography.liz_theme;

import android.graphics.Color;

import androidx.core.graphics.ColorUtils;

public class ColorPalettes {

    public static int getObscuredColor(int c) {
        float[] hsv = new float[3];
        int color = c;
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.85f; // value component
        color = Color.HSVToColor(hsv);
        return color;
    }

    public static int getTransparentColor(int color, int alpha) {
        return ColorUtils.setAlphaComponent(color, alpha);
    }

    public static String getHexColor(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.72f; // value component
        return Color.HSVToColor(hsv);
    }

    public static int[] getTransparencyShadows(int color) {
        int[] shadows = new int[10];
        for (int i = 0; i < 10; i++)
            shadows[i] = (ColorPalettes.getTransparentColor(color, ((100 - (i * 10)) * 255) / 100));
        return shadows;
    }
}