package com.photo.photography.callbacks;

import android.widget.ImageView;

import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.Media;

import java.util.ArrayList;

public interface CallbackMediaClick {

    void onMediaClick(Album album, ArrayList<Media> media, int position, ImageView imageView);

    void onMediaViewClick(Album album, ArrayList<Media> media, int position, ImageView imageView);
}
