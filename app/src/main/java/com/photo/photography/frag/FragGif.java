package com.photo.photography.frag;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.photo.photography.data_helper.Media;
import com.photo.photography.BuildConfig;

import pl.droidsonroids.gif.GifImageView;

public class FragGif extends FragBaseMedia {

    @NonNull
    public static FragGif newInstance(@NonNull Media media, int position) {
        return FragBaseMedia.newInstance(new FragGif(), media, position);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        GifImageView photoView = new GifImageView(getContext());
        try{
            if (media != null && media.getFile() != null && media.getFile().exists()) {
                Uri mediaUri = null;
                if (Build.VERSION.SDK_INT >= 24) {
                    mediaUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", media.getFile());
//                mediaUri = FileProvider.getUriForFile(requireActivity(), requireActivity().getPackageName() + ".provider", media.getFile());
                } else {
                    mediaUri = Uri.fromFile(media.getFile());
                }
                if (mediaUri != null)
                    photoView.setImageURI(mediaUri);

                photoView.setTransitionName(String.valueOf(media.getId()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setTapListener(photoView);
        return photoView;
    }
}
