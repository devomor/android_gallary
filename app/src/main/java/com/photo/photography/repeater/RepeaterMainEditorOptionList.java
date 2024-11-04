package com.photo.photography.repeater;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.callbacks.CallbackMainOptionItemClick;
import com.photo.photography.models.OptionDatasModel;

import java.util.ArrayList;

public class RepeaterMainEditorOptionList extends RecyclerView.Adapter<RepeaterMainEditorOptionList.ViewHolder> {

    ArrayList<OptionDatasModel> optionDataModelArrayList;
    CallbackMainOptionItemClick listener;
    Activity context;
    int currentSelectedOption;

    public RepeaterMainEditorOptionList(Activity activity, ArrayList<OptionDatasModel> optionDataModelArrayList, CallbackMainOptionItemClick listener) {
        super();
        this.context = activity;
        this.optionDataModelArrayList = optionDataModelArrayList;
        this.listener = listener;
    }

    public int getCurrentSelectedOption() {
        return currentSelectedOption;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_main_options, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        OptionDatasModel dataModel = optionDataModelArrayList.get(position);
        if (dataModel.isSelected()) {
            currentSelectedOption = position;

            viewHolder.imgThumbnail.setImageResource(dataModel.getOptionIconSelected());
            viewHolder.imgThumbnail.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
        } else {
            viewHolder.imgThumbnail.setImageResource(dataModel.getOptionIcon());
            viewHolder.imgThumbnail.setColorFilter(ContextCompat.getColor(context, R.color.light_gray), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public int getItemCount() {
        return optionDataModelArrayList.size();
    }

    public void notifySelection(int newPosition) {
        for (int i = 0; i < optionDataModelArrayList.size(); i++) {
            optionDataModelArrayList.get(i).setSelected(i == newPosition);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView imgThumbnail;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgThumbnail = (AppCompatImageView) itemView.findViewById(R.id.img_thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TAG", "clicked position:" + getLayoutPosition());
                    listener.onItemClick(getLayoutPosition()); // (CurrentPos, newSelectedPos)
                }
            });
        }
    }
}
