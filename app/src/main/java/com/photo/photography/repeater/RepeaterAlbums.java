package com.photo.photography.repeater;

import static com.photo.photography.util.configs.Constants.FOLDER_WHATSAPP_STATUS;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActMain;
import com.photo.photography.act.ActWAStatus;
import com.photo.photography.callbacks.CallbackActions;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.AlbumsHelper;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.sorting.AlbumsComparator;
import com.photo.photography.data_helper.sorting.SortingModes;
import com.photo.photography.data_helper.sorting.SortingOrders;
import com.photo.photography.liz_theme.ui_theme.ThemedIcons;
import com.photo.photography.util.StringUtil;
import com.photo.photography.util.preferences.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RepeaterAlbums extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final CallbackActions actionsListener;
    private final Activity mActivity;
    private List<Album> albums;
    private int selectedCount = 0;
    private SortingOrders sortingOrder;
    private SortingModes sortingMode;
    private boolean isSelecting;

    public RepeaterAlbums(Activity mActivity, CallbackActions actionsListener) {
        this.mActivity = mActivity;
        albums = new ArrayList<>();
        this.sortingMode = AlbumsHelper.getSortingMode();
        this.sortingOrder = AlbumsHelper.getSortingOrder();
        this.actionsListener = actionsListener;
    }

    public void sort() {
        Album album = albums.get(0);
        albums.remove(0);
        Collections.sort(albums, AlbumsComparator.getComparator(sortingMode, sortingOrder));
        albums.add(0, album);
        notifyDataSetChanged();
    }

    public List<String> getAlbumsPaths() {
        ArrayList<String> list = new ArrayList<>();

        for (Album album : albums) {
            list.add(album.getPath());
        }

        return list;
    }

    public Album get(int pos) {
        return albums.get(pos);
    }

    public void notifyItemChanaged(Album album) {
        notifyItemChanged(albums.indexOf(album));
    }

    public SortingOrders sortingOrder() {
        return sortingOrder;
    }

    public void changeSortingOrder(SortingOrders sortingOrder) {
        this.sortingOrder = sortingOrder;
        reverseOrder();
        notifyDataSetChanged();
    }

    public SortingModes sortingMode() {
        return sortingMode;
    }

    public void changeSortingMode(SortingModes sortingMode) {
        this.sortingMode = sortingMode;
        sort();
    }

    public List<Album> getSelectedAlbums() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return albums.stream().filter(Album::isSelected).collect(Collectors.toList());
        } else {
            ArrayList<Album> arrayList = new ArrayList<>(selectedCount);
            for (Album album : albums)
                if (album.isSelected())
                    arrayList.add(album);
            return arrayList;
        }
    }

    public Album getFirstSelectedAlbum() {
        if (selectedCount > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                return albums.stream().filter(Album::isSelected).findFirst().orElse(null);
            else
                for (Album album : albums)
                    if (album.isSelected())
                        return album;
        }
        return null;
    }

    private void startSelection() {
        isSelecting = true;
        actionsListener.onSelectMode(true);
    }

    private void stopSelection() {
        isSelecting = false;
        actionsListener.onSelectMode(false);
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void selectAll() {
        for (int i = 0; i < albums.size(); i++)
            if (albums.get(i).setSelected(true))
                notifyItemChanged(i);
        selectedCount = albums.size();
        startSelection();
    }

    public void removeAlbum(Album album) {
        int i = albums.indexOf(album);
        albums.remove(i);
        notifyItemRemoved(i);
    }

    public void invalidateSelectedCount() {
        int c = 0;
        for (Album m : this.albums) {
            c += m.isSelected() ? 1 : 0;
        }

        this.selectedCount = c;

        if (this.selectedCount == 0) stopSelection();
        else {
            this.actionsListener.onSelectionCountChanged(selectedCount, albums.size());
        }
    }

    public boolean clearSelected() {

        boolean changed = true;
        for (int i = 0; i < albums.size(); i++) {
            boolean b = albums.get(i).setSelected(false);
            if (b)
                notifyItemChanged(i);
            changed &= b;
        }

        selectedCount = 0;
        stopSelection();
        return changed;
    }

    @Override
    public int getItemViewType(int position) {
        if (albums.get(position) != null) {
            if (albums.get(position).isNativeAd()) {
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 2) {
            return new AdsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ad, parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_albums_material, parent, false);
            return new AlbumsHolder(v);
        }
    }

    private void notifySelected(boolean increase) {
        selectedCount += increase ? 1 : -1;
        actionsListener.onSelectionCountChanged(selectedCount, getItemCount());

        if (selectedCount == 0 && isSelecting) stopSelection();
        else if (selectedCount > 0 && !isSelecting) startSelection();
    }

    public boolean selecting() {
        return isSelecting;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof AlbumsHolder) {
            AlbumsHolder holder = (AlbumsHolder) viewHolder;
            Album a = albums.get(position);
            if (a.getName().equalsIgnoreCase(FOLDER_WHATSAPP_STATUS)) {
                holder.name.setText("Whatsapp Status");
                holder.lottieAnim.setImageAssetsFolder("images");
                holder.lottieAnim.playAnimation();
                holder.lottieAnim.setVisibility(View.VISIBLE);
                holder.lottieAnimLocation.setVisibility(View.GONE);
                holder.picture.setVisibility(View.GONE);
                holder.nMedia.setText(a.getCount() + " items");
            } else {
                Media f = a.getCover();
                holder.name.setText(StringUtil.htmlFormat(a.getName(), ContextCompat.getColor(mActivity, R.color.accent_black), false, false));
                holder.lottieAnim.setVisibility(View.GONE);
                holder.lottieAnimLocation.setVisibility(View.GONE);
                holder.picture.setVisibility(View.VISIBLE);
                RequestOptions options = new RequestOptions()
                        .signature(f.getSignature())
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .centerCrop()
                        .error(R.drawable.icon_error)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(holder.picture.getContext())
                        .load(f.getPath())
                        .placeholder(R.drawable.e_placeholder)
                        .apply(options)
                        .into(holder.picture);
                holder.nMedia.setText(a.getCount() + " items");
            }

            if (a.isSelected()) {
                holder.selectedIcon.setVisibility(View.VISIBLE);
            } else {
                holder.selectedIcon.setVisibility(View.GONE);
            }

            holder.llCount.setVisibility(Prefs.showMediaCount() ? View.VISIBLE : View.GONE);

//            holder.nMedia.setText(a.getCount() + " items");

            holder.card.setOnClickListener(v -> {
                if (selecting()) {
                    notifySelected(a.toggleSelected());
                    notifyItemChanged(position);

                } else if (albums.size() > position && !TextUtils.isEmpty(albums.get(position).getName()) && albums.get(position).getName().equalsIgnoreCase(FOLDER_WHATSAPP_STATUS)) {
                    Intent intent = new Intent(mActivity, ActWAStatus.class);
                    intent.putExtra(ActWAStatus.BUNDLE_ALBUM, albums.get(position));
                    mActivity.startActivityForResult(intent, ActMain.REQUEST_CODE_WA_STATUS);

                }/* else if (albums.size() > position && !TextUtils.isEmpty(albums.get(position).getName()) && albums.get(position).getName().equalsIgnoreCase(FOLDER_LOCATION)) {
                    actionsListener.onLocationClick();
                }*/ else
                    actionsListener.onItemSelected(position, holder.picture);
            });

//            holder.card.setOnLongClickListener(v -> {
//                notifySelected(a.toggleSelected());
//                notifyItemChanged(position);
//                return true;
//            });
        } else if (viewHolder instanceof AdsHolder) {
            AdsHolder holder = (AdsHolder) viewHolder;
            if (albums.get(position).isNativeAd()) {
                NativeAd nativeAd = albums.get(position).getNativeAdG();
                if (nativeAd == null) {
                    holder.fl_adplaceholder.setVisibility(View.GONE);
                } else {
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(mActivity).inflate(R.layout.ads_unified, null);
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
        }
    }

    public void clear() {
        albums.clear();
        notifyDataSetChanged();
    }

    public int add(Album album) {
        albums.add(album);
        notifyItemInserted(albums.size() - 1);
        return albums.size() - 1;
    }

    private void reverseOrder() {
        Album album = albums.get(0);
        albums.remove(0);
        int z = 0, size = getItemCount();
        while (z < size && albums.get(z).isPinned())
            z++;

        for (int i = Math.max(0, z), mid = (i + size) >> 1, j = size - 1; i < mid; i++, j--)
            Collections.swap(albums, i, j);

        albums.add(0, album);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public NativeAd getGoogleNativeAd() {
        if (MyApp.getInstance().checkForNativeAdMain()) {
            NativeAd nativeAd = MyApp.getInstance().getGNativeHome().get(0);
            return nativeAd;
        }
        return null;
    }

    public void notifyAds() {
        if (albums != null && albums.size() > 0 && albums.get(0).isNativeAd()) {
            albums.remove(0);
            notifyItemRemoved(0);
        }
        Album local = new Album("", "", 0, 0);
        local.setNativeAd(true);
        local.setNativeAdG(getGoogleNativeAd());
        if (albums != null) {
            albums.add(0, local);
        } else {
            albums = new ArrayList<>();
            albums.add(local);
        }
        notifyItemInserted(0);
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

    static class AlbumsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.album_card)
        CardView card;
        @BindView(R.id.lottie_anim)
        LottieAnimationView lottieAnim;
        @BindView(R.id.lottie_anim_location)
        LottieAnimationView lottieAnimLocation;
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

    public static class AdsHolder extends RecyclerView.ViewHolder {

        FrameLayout fl_adplaceholder;


        AdsHolder(View itemView) {
            super(itemView);

            fl_adplaceholder = itemView.findViewById(R.id.fl_adplaceholder);
        }
    }
}