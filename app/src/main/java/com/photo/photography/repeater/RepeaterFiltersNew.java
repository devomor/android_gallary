package com.photo.photography.repeater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.callbacks.CallbackFilterItemClick;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class RepeaterFiltersNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ThumbnailItem> filterModelList;
    private final CallbackFilterItemClick filterItemClickListener;
    private final int rotateImage;
    public int selectedPos = 0;

    public RepeaterFiltersNew(List<ThumbnailItem> filterModelList, int rotateImage, CallbackFilterItemClick filterItemClickListener) {
        this.filterModelList = filterModelList;
        this.filterItemClickListener = filterItemClickListener;
        this.rotateImage = rotateImage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_items, parent, false);
        return new CommonHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CommonHolder holder = (CommonHolder) viewHolder;
        ThumbnailItem thumbnailItem = filterModelList.get(position);
        holder.selectedBorder.setVisibility(position == selectedPos ? View.VISIBLE : View.GONE);
        holder.ivImage.setRotation(rotateImage);
        holder.ivImage.setImageBitmap(thumbnailItem.image);
        holder.tvFilterName.setText(thumbnailItem.filterName);
    }

    public void setSelectedFirstPos() {
        int p = selectedPos;
        this.selectedPos = 0;
        notifyItemChanged(p);
        notifyItemChanged(selectedPos);
    }

    @Override
    public int getItemCount() {
        return filterModelList.size();
    }

    class CommonHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvFilterName;
        TextView selectedBorder;

        CommonHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.img_filter);
            tvFilterName = (TextView) view.findViewById(R.id.tv_filter);
            selectedBorder = (TextView) view.findViewById(R.id.selectedBorder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = selectedPos;
                    selectedPos = getAdapterPosition();
                    notifyItemChanged(p);
                    notifyItemChanged(selectedPos);
                    filterItemClickListener.onFilterClicked(filterModelList.get(getAdapterPosition()).filter);
                }
            });
        }
    }
}
