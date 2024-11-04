package com.photo.photography.repeater;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.callbacks.CallbackCustomItemClickGallery;
import com.photo.photography.models.StickerItemParentModelMan;

import java.util.ArrayList;

//public class StickerCategoryListAdapter extends RecyclerView.Adapter<StickerCategoryListAdapter.ViewHolder> {


public class RepeaterStickerCategoryList extends RecyclerView.Adapter<RepeaterStickerCategoryList.ViewHolder> {

    ArrayList<StickerItemParentModelMan> optionDataModelArrayList;
    CallbackCustomItemClickGallery listener;
    Activity context;
    // int progressViewVisible;

    public RepeaterStickerCategoryList(Activity activity, ArrayList<StickerItemParentModelMan> optionDataModelArrayList, CallbackCustomItemClickGallery listener) {
        super();
        this.context = activity;
        this.optionDataModelArrayList = optionDataModelArrayList;
        this.listener = listener;
        // this.progressViewVisible = progressView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_stickers_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.tvSpecies.setText(optionDataModelArrayList.get(i).getParentText());
        viewHolder.imgThumbnail.setImageDrawable(optionDataModelArrayList.get(i).getParentIcon());

        switch (i) {
            case 0:
                //beard


                break;
            case 1:
                //hair


                break;

            case 2:
                //Mustache

                break;

        }

    }


    public void notifyProgress(ArrayList<StickerItemParentModelMan> stickerParentList) {
        this.optionDataModelArrayList = stickerParentList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return optionDataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView imgThumbnail;
        public TextView tvSpecies;
        RelativeLayout rel_main;


        public ViewHolder(final View itemView) {
            super(itemView);
            imgThumbnail = (AppCompatImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
            rel_main = (RelativeLayout) itemView.findViewById(R.id.rel_main);

            tvSpecies.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "clicked position:" + getLayoutPosition());
                    listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}

//}

 /*   ArrayList<StickerItemParentModel> optionDataModelArrayList;
    CustomItemClickListener listener;
    Activity context;

    public StickerCategoryListAdapter(Activity activity, ArrayList<StickerItemParentModel> optionDataModelArrayList, CustomItemClickListener listener) {
        super();
        this.context = activity;
        this.optionDataModelArrayList = optionDataModelArrayList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_sticker_category, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.tvSpecies.setText(optionDataModelArrayList.get(i).getParentText());
        viewHolder.imgThumbnail.setImageDrawable(optionDataModelArrayList.get(i).getParentIcon());
    }

    @Override
    public int getItemCount() {
        return optionDataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvSpecies;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
            tvSpecies.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "clicked position:" + getLayoutPosition());
                    listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}*/
