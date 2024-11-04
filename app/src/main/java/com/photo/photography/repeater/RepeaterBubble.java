package com.photo.photography.repeater;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.edit_views.SquareImagesView;
import com.photo.photography.models.TextBubblesModel;
import com.photo.photography.util.utilsEdit.SupportClass;

import java.util.ArrayList;

public class RepeaterBubble extends RecyclerView.Adapter<RepeaterBubble.ViewHolder> {

    private final Context mContext;
    private final ArrayList<TextBubblesModel> textBubbleList;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public RepeaterBubble(Context mContext, ArrayList<TextBubblesModel> textBubbleList) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.textBubbleList = textBubbleList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_stickers_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bitmap imutableBitmap = SupportClass.getBitmapFromAsset(mContext, SupportClass.filePath + textBubbleList.get(position).getKEY_BUBBLE_IMAGE());
        Bitmap bitmap = imutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
        holder.ivBubble.setImageBitmap(bitmap);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return textBubbleList.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SquareImagesView ivBubble;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBubble = (SquareImagesView) itemView.findViewById(R.id.ivSticker);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
