package com.photo.photography.repeater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.models.TemplateItemModel;
import com.photo.photography.util.utils.PhotoUtil;
import com.photo.photography.view.SquaresImageView;

import java.util.ArrayList;


public class RepeaterHorizontalPreviewTemplate extends RecyclerView.Adapter<RepeaterHorizontalPreviewTemplate.PreviewTemplateViewHolder> {

    private final ArrayList<TemplateItemModel> mTemplateItems;
    private final OnPreviewTemplateClickListener mListener;
    int oldPos;

    public RepeaterHorizontalPreviewTemplate(ArrayList<TemplateItemModel> items, OnPreviewTemplateClickListener listener) {
        mTemplateItems = items;
        mListener = listener;
        for (int i = 0; i < mTemplateItems.size(); i++) {
            if (mTemplateItems.get(i).isSelected()) {
                oldPos = i;
                break;
            }
        }
    }

    @Override
    public PreviewTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_preview_template_hor, parent, false);
        return new PreviewTemplateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PreviewTemplateViewHolder holder, final int position) {
        PhotoUtil.loadImageWithGlide(holder.mImageView.getContext(), holder.mImageView, mTemplateItems.get(position).getPreview());
        if (mTemplateItems.get(position).isSelected()) {
            holder.mSelectedView.setVisibility(View.VISIBLE);
        } else {
            holder.mSelectedView.setVisibility(View.INVISIBLE);
        }

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mTemplateItems.get(oldPos).setSelected(false);
                    notifyItemChanged(oldPos);
                    mListener.onPreviewTemplateClick(position);
                    oldPos = position;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTemplateItems.size();
    }

    public interface OnPreviewTemplateClickListener {
        void onPreviewTemplateClick(int position);
    }

    public static class PreviewTemplateViewHolder extends RecyclerView.ViewHolder {
        private final SquaresImageView mImageView;
        private final SquaresImageView mSelectedView;

        PreviewTemplateViewHolder(View itemView) {
            super(itemView);
            mImageView = (SquaresImageView) itemView.findViewById(R.id.imageView);
            mSelectedView = itemView.findViewById(R.id.selectedView);
        }
    }
}
