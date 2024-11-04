package com.photo.photography.repeater;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.callbacks.CallbackCustomItemClick;
import com.photo.photography.models.OptionDatasModel;

import java.util.ArrayList;

public class RepeaterEditorOptionList extends RecyclerView.Adapter<RepeaterEditorOptionList.ViewHolder> {

    ArrayList<OptionDatasModel> optionDataModelArrayList;
    CallbackCustomItemClick listener;
    Activity context;

    public RepeaterEditorOptionList(Activity activity, ArrayList<OptionDatasModel> optionDataModelArrayList, CallbackCustomItemClick listener) {
        super();
        this.context = activity;
        this.optionDataModelArrayList = optionDataModelArrayList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_items, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.tvSpecies.setText(optionDataModelArrayList.get(i).getOptionName());
        viewHolder.imgThumbnail.setImageResource(optionDataModelArrayList.get(i).getOptionIcon());
        if (optionDataModelArrayList.get(i).isSelected()) {
            viewHolder.tvSpecies.setTextColor(ContextCompat.getColor(context, R.color.option_text_selected_color));
            viewHolder.imgThumbnail.setColorFilter(ContextCompat.getColor(context, R.color.option_icon_selected_color));
        } else {
            viewHolder.tvSpecies.setTextColor(ContextCompat.getColor(context, R.color.option_text_color));
            viewHolder.imgThumbnail.setColorFilter(ContextCompat.getColor(context, R.color.option_icon_color));
        }
        viewHolder.tvDevider.setVisibility(optionDataModelArrayList.get(i).withDevider() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return optionDataModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgThumbnail;
        public TextView tvSpecies, tvDevider;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            tvSpecies = (TextView) itemView.findViewById(R.id.tv_species);
            tvDevider = (TextView) itemView.findViewById(R.id.tvDevider);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (optionDataModelArrayList.get(getAdapterPosition()).isOptionSelectable()) {
                        for (int i = 0; i < optionDataModelArrayList.size(); i++) {
                            optionDataModelArrayList.get(i).setSelected(i == getAdapterPosition());
                        }
                        notifyDataSetChanged();
                    }
                    Log.d("TAG", "clicked position:" + getLayoutPosition());
                    listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}
