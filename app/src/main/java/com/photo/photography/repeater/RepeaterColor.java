package com.photo.photography.repeater;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;

/**
 * Created on 13-01-2018, 05:09:19 PM.
 */

public class RepeaterColor extends RecyclerView.Adapter<RepeaterColor.ViewHolder> {

    private final String[] colorArr;
    private final LayoutInflater mInflater;
    Context mContext;
    private ItemClickListener mClickListener;

    public RepeaterColor(Context mContext, String[] colorArr) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.colorArr = colorArr;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_color_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int color = Color.parseColor(colorArr[position]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(holder.ivColorPreview.getDrawable(), Color.parseColor(colorArr[position]));
        } else {
            holder.ivColorPreview.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return colorArr.length;
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivColorPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            ivColorPreview = (ImageView) itemView.findViewById(R.id.ivColorPreview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(getAdapterPosition());
        }
    }
}
