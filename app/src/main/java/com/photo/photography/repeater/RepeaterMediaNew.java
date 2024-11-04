package com.photo.photography.repeater;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.sorting.MediaComparator;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;
import com.photo.photography.callbacks.CallbackActions;
import com.photo.photography.liz_theme.ui_theme.ThemedIcons;
import com.photo.photography.view.SquareRelativeLayout;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter used to display Media Items.
 * TODO: This class needs a major cleanup. Remove code from onBindViewHolder!
 */
public class RepeaterMediaNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Media> media;
    private final CallbackActions actionsListener;
    Context mContext;
    int counter = 0;
    private ArrayList<Media> mediaNew;
    private int selectedCount = 0;
    private SortingOrders sortingOrder;
    private SortingModes sortingMode;
    private boolean isSelecting = false;

    public RepeaterMediaNew(Context context, SortingModes sortingMode, SortingOrders sortingOrder, CallbackActions actionsListener) {
        mContext = context;
        media = new ArrayList<>();
        mediaNew = new ArrayList<>();
        this.sortingMode = sortingMode;
        this.sortingOrder = sortingOrder;
        this.actionsListener = actionsListener;
    }

    public static String getDate(long milliSeconds) {
        String dateFormat = "dd MMM, yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(new Date(milliSeconds));
    }

    public static String getDateMonth(long milliSeconds) {
        String dateFormat = "MMM, yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(new Date(milliSeconds));
    }

    public static String getDateYear(long milliSeconds) {
        String dateFormat = "yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(new Date(milliSeconds));
    }

    public void changeSortingOrder(SortingOrders sortingOrder) {
        this.sortingOrder = sortingOrder;
        Collections.reverse(media);
        buildMediaNew();
    }

    public void changeSortingMode(SortingModes sortingMode) {
        this.sortingMode = sortingMode;
        sort();
    }

    private void sort() {
        Collections.sort(media, MediaComparator.getComparator(sortingMode, sortingOrder));
        buildMediaNew();
    }

    public NativeAd getGoogleNativeAd() {
        if (MyApp.getInstance().checkForNativeAdMain()) {
            NativeAd nativeAd = MyApp.getInstance().getGNativeHome().get(0);
            return nativeAd;
        }
        return null;
    }

    public void sortWithDate() {
        try {
            Collections.sort(media, new Comparator<Media>() {
                public int compare(Media obj1, Media obj2) {
                    // ## Ascending order
                    return sortingOrder == SortingOrders.ASCENDING
                            ? obj1.getDateModified().compareTo(obj2.getDateModified())
                            : obj2.getDateModified().compareTo(obj1.getDateModified()); // To compare string values
                    // return Integer.valueOf(obj1.empId).compareTo(Integer.valueOf(obj2.empId)); // To compare integer values

                    // ## Descending order
                    // return obj2.firstName.compareToIgnoreCase(obj1.firstName); // To compare string values
                    // return Integer.valueOf(obj2.empId).compareTo(Integer.valueOf(obj1.empId)); // To compare integer values
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
        }
    }

    public void buildMediaNew() {
        mediaNew = new ArrayList<>();
        Media local = new Media();
        local.setNativeAd(true);
        local.setNativeAdG(getGoogleNativeAd());
        mediaNew.add(local);

        if (sortingMode == SortingModes.DATE_DAY) {
            sortWithDate();
            for (int i = 0; i < media.size(); i++) {
                try {
                    String date1 = mediaNew.size() == 0 ? "" : getDate(mediaNew.get(mediaNew.size() - 1).getDateModified() != null ? mediaNew.get(mediaNew.size() - 1).getDateModified() : System.currentTimeMillis());
                    String date2 = getDate(media.get(i).getDateModified());
                    if (i == 0) {
                        mediaNew.add(null);
                    } else if (!date1.equals(date2)) {
                        mediaNew.add(null);
                    }
                    mediaNew.add(media.get(i));
                } catch (Exception exc) {
                    Log.e("JKLFd", "fakdjf");
                }
            }
        } else if (sortingMode == SortingModes.DATE_MONTH) {
            sortWithDate();
            for (int i = 0; i < media.size(); i++) {
                try {
                    String date1 = mediaNew.size() == 0 ? "" : getDateMonth(mediaNew.get(mediaNew.size() - 1).getDateModified() != null ? mediaNew.get(mediaNew.size() - 1).getDateModified() : System.currentTimeMillis());
                    String date2 = getDateMonth(media.get(i).getDateModified());
                    if (i == 0) {
                        mediaNew.add(null);
                    } else if (!date1.equals(date2)) {
                        mediaNew.add(null);
                    }
                    mediaNew.add(media.get(i));
                } catch (Exception exc) {
                    Log.e("JKLFd", "fakdjf");
                }
            }
        } else if (sortingMode == SortingModes.DATE_YEAR) {
            sortWithDate();
            for (int i = 0; i < media.size(); i++) {
                try {
                    String date1 = mediaNew.size() == 0 ? "" : getDateYear(mediaNew.get(mediaNew.size() - 1).getDateModified() != null ? mediaNew.get(mediaNew.size() - 1).getDateModified() : System.currentTimeMillis());
                    String date2 = getDateYear(media.get(i).getDateModified());
                    if (i == 0) {
                        mediaNew.add(null);
                    } else if (!date1.equals(date2)) {
                        mediaNew.add(null);
                    }

                    mediaNew.add(media.get(i));
                } catch (Exception exc) {
                    Log.e("JKLFd", "fakdjf");
                }
            }
        } else if (sortingMode == SortingModes.SIZE) {
            ArrayList<Media> first = new ArrayList<>();
            ArrayList<Media> second = new ArrayList<>();
            for (int i = 0; i < media.size(); i++) {
                try {
                    if (i == 0) {
                        first = new ArrayList<>();
                        second = new ArrayList<>();
                    }
                    if (media.get(i).getSize() < (1024 * 1024 * 5)) {
                        if (sortingOrder == SortingOrders.ASCENDING)
                            first.add(media.get(i));
                        else
                            second.add(media.get(i));

                    } else {
                        if (sortingOrder == SortingOrders.ASCENDING)
                            second.add(media.get(i));
                        else
                            first.add(media.get(i));
                    }
                } catch (Exception exc) {
                    Log.e("JKLFd", "fakdjf");
                }
            }

            if (first.size() > 0) {
                mediaNew.add(null);
                mediaNew.addAll(first);
            }
            if (second.size() > 0) {
                mediaNew.add(null);
                mediaNew.addAll(second);
            }
        } else {
            mediaNew.addAll(media);
        }
        notifyDataSetChanged();
    }

    public void setSelection(int pos) {
        if (media.get(pos).setSelected(true))
            notifyItemChanged(mediaNew.indexOf(media.get(pos)));
        selectedCount = media.size();
        startSelection();
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
                notifyItemChanged(mediaNew.indexOf(media.get(i)));
        selectedCount = media.size();
        startSelection();
    }

    public boolean clearSelected() {
        boolean changed = true;
        for (int i = 0; i < media.size(); i++) {
            boolean b = media.get(i).setSelected(false);
            if (b)
                notifyItemChanged(mediaNew.indexOf(media.get(i)));
            changed &= b;
        }

        selectedCount = 0;
        stopSelection();
        return changed;
    }

    private void notifySelected(boolean increase) {
        selectedCount += increase ? 1 : -1;
        actionsListener.onSelectionCountChanged(selectedCount, getOriginalItemCount());

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
    public long getItemId(int position) {
        return media.get(position).getUri().hashCode() ^ 1312;
    }

    @Override
    public int getItemViewType(int position) {
        if (mediaNew.get(position) != null) {
            if (mediaNew.get(position).getNativeAd()) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new MyHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sorting_header, parent, false));
        } else if (viewType == 2) {
            return new AdsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_photos, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder myholder, int position) {
        if (getItemViewType(position) == 0) {
            MyHeaderViewHolder holder = (MyHeaderViewHolder) myholder;
            String strTitle = "Media";
            if (sortingMode == SortingModes.DATE_DAY && mediaNew.size() > position + 1) {
                strTitle = mediaNew.size() == 0 || position == mediaNew.size() - 1 || mediaNew.get(position + 1) == null || mediaNew.get(position + 1).getDateModified() == null
                        ? "Day" : getDate(mediaNew.get(position + 1).getDateModified());

            } else if (sortingMode == SortingModes.DATE_MONTH && mediaNew.size() > position + 1) {
                strTitle = mediaNew.size() == 0 || position == mediaNew.size() - 1 || mediaNew.get(position + 1) == null || mediaNew.get(position + 1).getDateModified() == null
                        ? "Month" : getDateMonth(mediaNew.get(position + 1).getDateModified());

            } else if (sortingMode == SortingModes.DATE_YEAR && mediaNew.size() > position + 1) {
                strTitle = mediaNew.size() == 0 || position == mediaNew.size() - 1 || mediaNew.get(position + 1) == null || mediaNew.get(position + 1).getDateModified() == null
                        ? "Year" : getDateYear(mediaNew.get(position + 1).getDateModified());

            } else if (sortingMode == SortingModes.SIZE && mediaNew.size() > position + 1) {
                strTitle = mediaNew.size() == 0 || mediaNew.get(position + 1) == null
                        ? "Size" : mediaNew.get(position + 1).getSize() < (1024 * 1024 * 5) ? "Below 5 MB" : "Above 5 MB";
            }
            holder.sortingTitle.setText(strTitle);

        } else if (getItemViewType(position) == 2) {
            AdsHolder holder = (AdsHolder) myholder;
            if (mediaNew.get(position).getNativeAd()) {
                NativeAd nativeAd = mediaNew.get(position).getNativeAdG();
                if (nativeAd == null) {
                    holder.fl_adplaceholder.setVisibility(View.GONE);
                } else {
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(mContext).inflate(R.layout.ads_unified, null);
                    if (adView != null) {
                        populateUnifiedNativeAdView(nativeAd, adView);
                        holder.fl_adplaceholder.removeAllViews();
                        holder.fl_adplaceholder.addView(adView);
                        holder.fl_adplaceholder.setVisibility(View.VISIBLE);
                    } else {
                        holder.fl_adplaceholder.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.fl_adplaceholder.setVisibility(View.GONE);
            }

        } else {
            ViewHolder holder = (ViewHolder) myholder;
            Media f = mediaNew.get(position);
            holder.icon.setVisibility(View.GONE);
            holder.icon2.setVisibility(View.GONE);
            holder.gifIcon.setVisibility(f.isGif() ? View.VISIBLE : View.GONE);

            RequestOptions options = new RequestOptions()
                    .signature(f.getSignature())
                    .format(DecodeFormat.PREFER_RGB_565)
                    .centerCrop()
                    .placeholder(R.drawable.e_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//            holder.imageView.setTransitionName(String.valueOf(f.getId()));

            Glide.with(holder.imageView.getContext())
                    .load(f.getUri())
                    .apply(options)
                    .thumbnail(0.5f)
                    .into(holder.imageView);

            if (f.isVideo()) {
                holder.icon.setImageResource(R.drawable.e_play_video_white);
                holder.icon.setVisibility(View.VISIBLE);
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
                if (mediaNew.get(holder.getLayoutPosition()) != null) {
                    if (selecting()) {
                        notifySelected(f.toggleSelected());
                        notifyItemChanged(holder.getLayoutPosition());
                    } else
                        actionsListener.onItemSelected(media.indexOf(mediaNew.get(holder.getLayoutPosition())), holder.imageView);
                }
            });

            holder.icon2.setOnClickListener(v -> {
                if (media!=null && mediaNew.get(holder.getLayoutPosition()) != null) {
                    actionsListener.onItemViewSelected(media.indexOf(mediaNew.get(holder.getLayoutPosition())), holder.imageView);
                }
            });

            holder.layout.setOnLongClickListener(v -> {
                if (mediaNew.get(holder.getLayoutPosition()) != null) {
                    if (!selecting()) {
                        // If it is the first long press
                        notifySelected(f.toggleSelected());
                        notifyItemChanged(holder.getLayoutPosition());
                    } else {
                        selectAllUpTo(f);
                    }
                }
                return true;
            });
        }
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        try {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.getStoreView().setVisibility(View.GONE);
        adView.getPriceView().setVisibility(View.GONE);

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
//			videoStatus.setText(String.format(Locale.getDefault(),
//					"Video status: Ad contains a %.2f:1 video asset.",
//					vc.getAspectRatio()));

            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
//					refresh.setEnabled(true);
//					videoStatus.setText("Video status: Video playback has ended.");
                    super.onVideoEnd();
                }
            });
        } else {
//			videoStatus.setText("Video status: Ad does not contain a video asset.");
//			refresh.setEnabled(true);
        }
    }

    public void removeSelectedMedia(Media mediaRemove) {
        try {
            media.remove(mediaRemove);
            int i = mediaNew.indexOf(mediaRemove);
            // check for last element of the group and second last is the header of the group
            boolean p1 = i == mediaNew.size() - 1;
            boolean p2 = mediaNew.get(i - 1) == null;


            boolean p3 = mediaNew.size() - 1 != i && mediaNew.get(i - 1) == null && mediaNew.get(i + 1) == null;
            if ((p1 && p2) || p3) {
                mediaNew.remove(i - 1);
                notifyItemRemoved(i - 1);
                mediaNew.remove(i - 1);
                notifyItemRemoved(i - 1);
                return;
            }
            mediaNew.remove(i);
            notifyItemRemoved(i);
        } catch (Exception ex) {
            Log.e("ERROR", "Error in removeSelectedMedia method : " + ex.toString());
        }
    }

    public void invalidateSelectedCount() {
        int c = 0;
        for (Media m : this.media) {
            c += m.isSelected() ? 1 : 0;
        }

        this.selectedCount = c;

        if (this.selectedCount == 0)
            stopSelection();
        else {
            this.actionsListener.onSelectionCountChanged(selectedCount, getOriginalItemCount());
        }
    }

    /**
     * On longpress, it finds the last or the first selected image before or after the targetIndex
     * and selects them all.
     *
     * @param
     */
    public void selectAllUpTo(Media m) {
        int targetIndex = mediaNew.indexOf(m);

        int indexRightBeforeOrAfter = -1;
        int indexNow;

        for (Media sm : getSelected()) {
            indexNow = mediaNew.indexOf(sm);
            if (indexRightBeforeOrAfter == -1) indexRightBeforeOrAfter = indexNow;

            if (indexNow > targetIndex) break;
            indexRightBeforeOrAfter = indexNow;
        }

        if (indexRightBeforeOrAfter != -1) {
            for (int index = Math.min(targetIndex, indexRightBeforeOrAfter); index <= Math.max(targetIndex, indexRightBeforeOrAfter); index++) {
                if (mediaNew.get(index) != null) {
                    if (mediaNew.get(index).setSelected(true)) {
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
        return media.size() - 1;
    }

    @Override
    public int getItemCount() {
        return mediaNew.size();
    }

    public int getOriginalItemCount() {
        return media.size();
    }

    public static class AdsHolder extends RecyclerView.ViewHolder {
        FrameLayout fl_adplaceholder;

        AdsHolder(View itemView) {
            super(itemView);
            fl_adplaceholder = (FrameLayout) itemView.findViewById(R.id.fl_adplaceholder);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
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

    public class MyHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView sortingTitle;
        FrameLayout fl_adplaceholder;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            sortingTitle = (TextView) itemView.findViewById(R.id.sortingTitle);
            fl_adplaceholder = (FrameLayout) itemView.findViewById(R.id.fl_adplaceholder);
        }
    }
}
