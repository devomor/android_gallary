package com.photo.photography.liz_theme.ui_theme;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.iconics.view.IconicsImageView;


public class ThemedIcons extends IconicsImageView {
    public ThemedIcons(Context context) {
        this(context, null);
    }

    public ThemedIcons(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThemedIcons(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setColor(int color) {
        setIcon(getIcon().color(color));
    }

    public void setIcon(IIcon icon) {
        setIcon(getIcon().icon(icon));
    }


    public void setIconSrc(int icon) {
        setImageResource(icon);
    }
}
