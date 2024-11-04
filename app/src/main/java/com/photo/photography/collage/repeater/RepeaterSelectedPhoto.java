package com.photo.photography.collage.repeater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.collage.util.PhotoUtil;
import com.photo.photography.view.SquaresImageView;

import java.util.ArrayList;

/**
 * Created by Sira on 3/27/2018.
 */
public class RepeaterSelectedPhoto extends RecyclerView.Adapter<RepeaterSelectedPhoto.SelectedPhotoViewHolder> {
    private final ArrayList<String> mImages;
    private final OnDeleteButtonClickListener mListener;
    private boolean mImageFitCenter = false;

    public RepeaterSelectedPhoto(ArrayList<String> images, OnDeleteButtonClickListener listener) {
        mImages = images;
        mListener = listener;
    }

    public void setImageFitCenter(boolean imageFitCenter) {
        mImageFitCenter = imageFitCenter;
        notifyDataSetChanged();
    }

    @Override
    public SelectedPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selected_photo, parent, false);
        return new SelectedPhotoViewHolder(v, mImageFitCenter);
    }

    @Override
    public void onBindViewHolder(SelectedPhotoViewHolder holder, final int position) {
        PhotoUtil.loadImageWithGlide(holder.mImageView.getContext(), holder.mImageView, mImages.get(position));
        holder.mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteButtonClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(int position);
    }

    public static class SelectedPhotoViewHolder extends RecyclerView.ViewHolder {
        private final SquaresImageView mImageView;
        private final View mDeleteView;

        SelectedPhotoViewHolder(View itemView, boolean imageFitCenter) {
            super(itemView);
            mImageView = (SquaresImageView) itemView.findViewById(R.id.selectedImage);
            mDeleteView = itemView.findViewById(R.id.deleteView);
            if (imageFitCenter) {
                mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }
    }
}
