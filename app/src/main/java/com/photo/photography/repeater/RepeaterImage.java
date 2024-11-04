package com.photo.photography.repeater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.photo.photography.R;
import com.photo.photography.models.StatusModel;
import com.photo.photography.util.Common;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeaterImage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<StatusModel> imagesList;
    private final RelativeLayout container;
    private Context context;

    public RepeaterImage(List<StatusModel> imagesList, RelativeLayout container) {
        this.imagesList = imagesList;
        this.container = container;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context =parent.getContext();
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_status, parent, false));


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivVideo)
        ImageView ivVideo;
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;
        @BindView(R.id.save)
        ImageButton save;
        @BindView(R.id.share)
        ImageButton share;


        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder1, int position) {
        ItemViewHolder holder = (ItemViewHolder) holder1;
        final StatusModel status = imagesList.get(position);
        if (status.isVideo()) {
            holder.ivVideo.setVisibility(View.VISIBLE);
        } else {
            holder.ivVideo.setVisibility(View.GONE);
        }
        if (status.isApi30()) {
//            holder.save.setVisibility(View.GONE);
            Glide.with(context).load(status.getDocumentFile().getUri())
                    .placeholder(R.drawable.e_placeholder).into(holder.ivThumbnail);
        } else {
            holder.save.setVisibility(View.VISIBLE);
            Glide.with(context).load(status.getFile())
                    .placeholder(R.drawable.e_placeholder).into(holder.ivThumbnail);
        }

        holder.save.setOnClickListener(v -> Common.copyFile(status, context, container));

        holder.share.setOnClickListener(v -> {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("image/jpg");

            if (status.isApi30()) {
                shareIntent.putExtra(Intent.EXTRA_STREAM, status.getDocumentFile().getUri());
            } else {
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + status.getFile().getAbsolutePath()));
            }

            context.startActivity(Intent.createChooser(shareIntent, "Share image"));

        });

        holder.ivThumbnail.setOnClickListener(v -> {

            final AlertDialog.Builder alertD = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.row_image_full_screen, null);
            alertD.setView(view);

            ImageView imageView = view.findViewById(R.id.img);
            if (status.isApi30()) {
                Glide.with(context).load(status.getDocumentFile().getUri())
                        .placeholder(R.drawable.e_placeholder)
                        .into(imageView);
            } else {
                Glide.with(context).load(status.getFile())
                        .placeholder(R.drawable.e_placeholder)
                        .into(imageView);
            }

            AlertDialog alert = alertD.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();

        });

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

}
