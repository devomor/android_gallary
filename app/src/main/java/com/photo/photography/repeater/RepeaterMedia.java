package com.photo.photography.repeater;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photo.photography.R;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.sorting.MediaComparator;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;
import com.photo.photography.callbacks.CallbackActions;
import com.photo.photography.liz_theme.ui_theme.ThemedIcons;
import com.photo.photography.view.SquareRelativeLayout;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter used to display Media Items.
 * <p>
 * TODO: This class needs a major cleanup. Remove code from onBindViewHolder!
 */
public class RepeaterMedia extends RecyclerView.Adapter<RepeaterMedia.ViewHolder> {

    private final ArrayList<Media> media;
    private final CallbackActions actionsListener;
    private int selectedCount = 0;
    private SortingOrders sortingOrder;
    private SortingModes sortingMode;
    private boolean isSelecting = false;

    public RepeaterMedia(Context context, SortingModes sortingMode, SortingOrders sortingOrder, CallbackActions actionsListener) {
        media = new ArrayList<>();
        this.sortingMode = sortingMode;
        this.sortingOrder = sortingOrder;
        this.actionsListener = actionsListener;
    }

    private void sort() {
        Collections.sort(media, MediaComparator.getComparator(sortingMode, sortingOrder));
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return media.get(position).getUri().hashCode() ^ 1312;
    }

    public void changeSortingOrder(SortingOrders sortingOrder) {
        this.sortingOrder = sortingOrder;
        Collections.reverse(media);
        notifyDataSetChanged();
    }

    public void changeSortingMode(SortingModes sortingMode) {
        this.sortingMode = sortingMode;
        sort();
    }

    public ArrayList<Media> getSelected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new ArrayList<>(media.stream().filter(Media::isSelected).collect(Collectors.toList()));
        } else {
            ArrayList<Media> arrayList = new ArrayList<>(selectedCount);
            for (Media m : media)
                if (m.isSelected())
                    arrayList.add(m);
            return arrayList;
        }
    }

    public Media getFirstSelected() {
        if (selectedCount > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return media.stream().filter(Media::isSelected).findFirst().orElse(null);
            else
                for (Media m : media)
                    if (m.isSelected())
                        return m;
        }
        return null;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(@NonNull List<Media> mediaList) {
        media.clear();
        media.addAll(mediaList);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void selectAll() {
        for (int i = 0; i < media.size(); i++)
            if (media.get(i).setSelected(true))
                notifyItemChanged(i);
        selectedCount = media.size();
        startSelection();
    }

    public boolean clearSelected() {
        boolean changed = true;
        for (int i = 0; i < media.size(); i++) {
            boolean b = media.get(i).setSelected(false);
            if (b)
                notifyItemChanged(i);
            changed &= b;
        }

        selectedCount = 0;
        stopSelection();
        return changed;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photos, parent, false));
    }

    private void notifySelected(boolean increase) {
        selectedCount += increase ? 1 : -1;
        actionsListener.onSelectionCountChanged(selectedCount, getItemCount());

        if (selectedCount == 0 && isSelecting) stopSelection();
        else if (selectedCount > 0 && !isSelecting) startSelection();
    }

    private void startSelection() {
        isSelecting = true;
        actionsListener.onSelectMode(true);
    }

    private void stopSelection() {
        isSelecting = false;
        actionsListener.onSelectMode(false);
    }

    public boolean selecting() {
        return isSelecting;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Media f = media.get(position);
        if (f != null && getPathFromURI(f.getPath())) {
            holder.icon.setVisibility(View.GONE);
            holder.icon2.setVisibility(View.GONE);

            holder.gifIcon.setVisibility(f.isGif() ? View.VISIBLE : View.GONE);

            RequestOptions options = new RequestOptions()
                    .signature(f.getSignature())
                    .format(DecodeFormat.PREFER_RGB_565)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

            Glide.with(holder.imageView.getContext())
                    .load(f.getUri())
                    .placeholder(R.drawable.e_placeholder)
                    .apply(options)
                    .thumbnail(0.5f)
                    .into(holder.imageView);

            if (f.isVideo()) {
                holder.icon.setImageResource(R.drawable.e_play_video_white);
                holder.icon.setVisibility(View.VISIBLE);
                //ANIMS
                holder.icon.animate().alpha(1).setDuration(250);
            } else {
                holder.icon.setVisibility(View.GONE);
                holder.icon2.setVisibility(View.GONE);

                holder.icon.animate().alpha(0).setDuration(250);
                holder.icon2.animate().alpha(0).setDuration(250);
            }

            if (f.isSelected()) {
                holder.icon.setImageResource(R.drawable.e_check_blue);
                holder.icon2.setIcon(CommunityMaterial.Icon.cmd_arrow_expand_all);
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon2.setVisibility(View.VISIBLE);
                holder.imageView.setColorFilter(0x88000000, PorterDuff.Mode.SRC_ATOP);
                holder.icon.animate().alpha(1).setDuration(250);
                holder.icon2.animate().alpha(1).setDuration(250);
            } else {
                holder.imageView.clearColorFilter();
            }

            holder.layout.setOnClickListener(v -> {
                if (selecting()) {
                    notifySelected(f.toggleSelected());
                    notifyItemChanged(holder.getAdapterPosition());
                } else
                    actionsListener.onItemSelected(holder.getAdapterPosition(), holder.imageView);
            });

            holder.icon2.setOnClickListener(v -> {
                actionsListener.onItemViewSelected(holder.getAdapterPosition(), holder.imageView);
            });

            holder.layout.setOnLongClickListener(v -> {
                if (!selecting()) {
                    // If it is the first long press
                    notifySelected(f.toggleSelected());
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    selectAllUpTo(f);
                }
                return true;
            });
        }
    }

    public boolean getPathFromURI(String path) {
        File yourFile = new File(path);
        if (yourFile.exists()) {
            return true;
        }
        return false;

    }

    public void remove(Media media) {
        int i = this.media.indexOf(media);
        this.media.remove(i);
        notifyItemRemoved(i);
    }

    public void removeSelectedMedia(Media media) {
        try {
            int i = this.media.indexOf(media);
            this.media.remove(i);
            notifyItemRemoved(i);
        } catch (Exception ignored) {
            Log.e("ReoveSelection :",""+ignored.getMessage());
        }
    }

    public void invalidateSelectedCount() {
        int c = 0;
        for (Media m : this.media) {
            c += m.isSelected() ? 1 : 0;
        }

        this.selectedCount = c;

        if (this.selectedCount == 0) stopSelection();
        else {
            this.actionsListener.onSelectionCountChanged(selectedCount, media.size());
        }
    }

    /**
     * On longpress, it finds the last or the first selected image before or after the targetIndex
     * and selects them all.
     *
     * @param
     */
    public void selectAllUpTo(Media m) {
        int targetIndex = media.indexOf(m);

        int indexRightBeforeOrAfter = -1;
        int indexNow;

        // TODO: 4/5/17 rewrite?
        for (Media sm : getSelected()) {
            indexNow = media.indexOf(sm);
            if (indexRightBeforeOrAfter == -1) indexRightBeforeOrAfter = indexNow;

            if (indexNow > targetIndex) break;
            indexRightBeforeOrAfter = indexNow;
        }

        if (indexRightBeforeOrAfter != -1) {
            for (int index = Math.min(targetIndex, indexRightBeforeOrAfter); index <= Math.max(targetIndex, indexRightBeforeOrAfter); index++) {
                if (media.get(index) != null) {
                    if (media.get(index).setSelected(true)) {
                        notifySelected(true);
                        notifyItemChanged(index);
                    }
                }
            }
        }
    }

    public void setupFor(Album album) {
        media.clear();
        changeSortingMode(album.settings.getSortingMode());
        changeSortingOrder(album.settings.getSortingOrder());
        notifyDataSetChanged();
    }

    public void clear() {
        media.clear();
        notifyDataSetChanged();
    }

    public int add(Media album) {
        media.add(album);
        notifyItemInserted(media.size() - 1);
        return media.size() - 1;
    }

    public void addAllMediaList(ArrayList<Media> mediaArrayList) {
        media.addAll(mediaArrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return media.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo_preview)
        ImageView imageView;
        @BindView(R.id.gif_icon)
        ThemedIcons gifIcon;
        @BindView(R.id.icon)
        ImageView icon;
        @BindView(R.id.icon2)
        ThemedIcons icon2;
        @BindView(R.id.media_card_layout)
        SquareRelativeLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
