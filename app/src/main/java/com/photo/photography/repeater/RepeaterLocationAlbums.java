package com.photo.photography.repeater;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photo.photography.R;
import com.photo.photography.data_helper.Album;
import com.photo.photography.callbacks.CallbackActions;
import com.photo.photography.liz_theme.ui_theme.ThemedIcons;
import com.photo.photography.util.preferences.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeaterLocationAlbums extends RecyclerView.Adapter<RepeaterLocationAlbums.AlbumsHolder> {

    private Activity mActivity;
    private ArrayList<Album> albumArrayList;
    private CallbackActions actionsListener;

    public RepeaterLocationAlbums(Activity mActivity, ArrayList<Album> locationListAlbum, CallbackActions actionsListener) {
        this.mActivity = mActivity;
        this.actionsListener = actionsListener;
        this.albumArrayList = locationListAlbum;
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }

    @NonNull
    @Override
    public RepeaterLocationAlbums.AlbumsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_albums_material, parent, false);
        return new AlbumsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RepeaterLocationAlbums.AlbumsHolder viewHolder, int position) {
        if (viewHolder instanceof AlbumsHolder) {
            AlbumsHolder holder = (AlbumsHolder) viewHolder;
            holder.name.setText(albumArrayList.get(position).getName());
            holder.lottieAnim.setVisibility(View.GONE);
            holder.picture.setVisibility(View.VISIBLE);
            RequestOptions options = new RequestOptions()
                    .format(DecodeFormat.PREFER_ARGB_8888)
                    .centerCrop()
                    .error(R.drawable.icon_error)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(holder.picture.getContext())
                    .load(albumArrayList.get(position).getMediaArrayList().get(0).getUri())
                    .placeholder(R.drawable.e_placeholder)
                    .apply(options)
                    .into(holder.picture);

            holder.llCount.setVisibility(Prefs.showMediaCount() ? View.VISIBLE : View.GONE);

            holder.nMedia.setText(albumArrayList.get(position).getMediaArrayList().size() + " items");

            holder.card.setOnClickListener(v -> {
                actionsListener.onItemSelected(position, holder.picture);
            });
        }
    }

    public Album get(int pos) {
        return albumArrayList.get(pos);
    }

    public void notifyLocationList(ArrayList<Album> albumArrayList) {
        this.albumArrayList.clear();
        this.albumArrayList = albumArrayList;
        notifyDataSetChanged();
    }

    static class AlbumsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.album_card)
        CardView card;
        @BindView(R.id.lottie_anim)
        LottieAnimationView lottieAnim;
        @BindView(R.id.album_preview)
        ImageView picture;
        @BindView(R.id.selected_icon)
        ThemedIcons selectedIcon;
        @BindView(R.id.ll_album_info)
        View footer;
        @BindView(R.id.ll_media_count)
        View llCount;
        @BindView(R.id.album_name)
        TextView name;
        @BindView(R.id.album_media_count)
        TextView nMedia;
        @BindView(R.id.album_media_label)
        TextView mediaLabel;
        @BindView(R.id.album_path)
        TextView path;

        AlbumsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}