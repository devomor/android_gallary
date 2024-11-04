package com.photo.photography.frag;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.photo.photography.BuildConfig;
import com.photo.photography.R;
import com.photo.photography.data_helper.Media;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A Media Fragment for showing an Image (static)
 */
public class FragImage extends FragBaseMedia {

    @BindView(R.id.imageView)
    PhotoView imageView;

    @NonNull
    public static FragImage newInstance(@NonNull Media media, int position) {
        return FragBaseMedia.newInstance(new FragImage(), media, position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_image, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            imageView.setTransitionName(String.valueOf(media.getId()));
            Uri mediaUri;
            if (Build.VERSION.SDK_INT >= 24) {
                File imgFile = media.getFile();
                mediaUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", imgFile);
//                mediaUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", imgFile);
            } else {
                mediaUri = Uri.fromFile(media.getFile());
            }

            Glide.with(imageView.getContext())
                    .load(mediaUri)
                    .into(imageView);
//            imageView.setImage(ImageSource.uri(mediaUri));

            setTapListener(imageView);
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            Log.e("TAG_ERR", e.toString());
        }
    }

    /**
     * Rotate the currently displaying media image.
     *
     * @param rotationInDegrees The rotation in degrees
     */
    public void rotatePicture(int rotationInDegrees) {
        if (rotationInDegrees == -90 && imageView.getRotation() == 0)
            imageView.setRotation(270);
        else
            imageView.setRotation(Math.abs(imageView.getRotation() + rotationInDegrees) % 360);
    }
}
