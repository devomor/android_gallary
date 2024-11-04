package com.photo.photography.frag;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.photo.photography.LinearLayoutManagerWrapp;
import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActMain;
import com.photo.photography.act.ActSettings;
import com.photo.photography.act.ActVideos;
import com.photo.photography.ads_notifier.AdsEventNotifier;
import com.photo.photography.ads_notifier.AdsEventState;
import com.photo.photography.ads_notifier.AdsEventTypes;
import com.photo.photography.ads_notifier.AdsIEventListener;
import com.photo.photography.ads_notifier.AdsListenerPriority;
import com.photo.photography.ads_notifier.AdsNotifierFactory;
import com.photo.photography.collage.screen.ActSelectPhoto;
import com.photo.photography.data_helper.Album;
import com.photo.photography.data_helper.Media;
import com.photo.photography.duplicatephotos.AppDataBaseHandler;
import com.photo.photography.duplicatephotos.act.ActDuplicateHomeMain;
import com.photo.photography.duplicatephotos.act.ActPhotoList;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.repeater.RepeaterLocationAlbums;
import com.photo.photography.secure_vault.ActSetupPinLock;
import com.photo.photography.util.utilsEdit.SupportClass;

import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class FragRvAllMediaExplore extends FragBaseMediaGrid implements AdsIEventListener {
    public static final String EXTRA_IS_FRAME_IMAGE = "frameImage";
    //    RelativeLayout rel_ads;
    SwipeRefreshLayout swipe_refresh;
    RecyclerView recyclerLocationWise;
    RepeaterLocationAlbums locationAlbumsAdapter;
    ArrayList<String> locationListTitle = new ArrayList<>();
    ArrayList<Album> locationListAlbum = new ArrayList<>();
    LinearLayout linProgress, linLocation;
    TextView tvTotalScanned, tvTotalPhotos;
    CardView cardVideo1, cardVideo2, cardVideo3, cardVideo4;
    ImageView ivVideo1, ivVideo2, ivVideo3, ivVideo4;
    ImageView ivPlaces1, ivPlaces2, ivPlaces3, ivPlaces4;
    boolean isScanningModeEnabled = false;

    public FragRvAllMediaExplore() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_rv_all_media_explore, container, false);
//        rel_ads = view.findViewById(R.id.rel_ads);
        recyclerLocationWise = view.findViewById(R.id.recyclerLocationWise);
        linProgress = view.findViewById(R.id.linProgress);
        linLocation = view.findViewById(R.id.linLocation);
        tvTotalScanned = view.findViewById(R.id.tvTotalScanned);
        tvTotalScanned.setText("0");
        tvTotalPhotos = view.findViewById(R.id.tvTotalPhotos);
        cardVideo1 = view.findViewById(R.id.cardVideo1);
        cardVideo2 = view.findViewById(R.id.cardVideo2);
        cardVideo3 = view.findViewById(R.id.cardVideo3);
        cardVideo4 = view.findViewById(R.id.cardVideo4);
        ivVideo1 = view.findViewById(R.id.ivVideo1);
        ivVideo2 = view.findViewById(R.id.ivVideo2);
        ivVideo3 = view.findViewById(R.id.ivVideo3);
        ivVideo4 = view.findViewById(R.id.ivVideo4);
        ivPlaces1 = view.findViewById(R.id.ivPlaces1);
        ivPlaces2 = view.findViewById(R.id.ivPlaces2);
        ivPlaces3 = view.findViewById(R.id.ivPlaces3);
        ivPlaces4 = view.findViewById(R.id.ivPlaces4);


        tvTotalPhotos.setText("0");
        recyclerLocationWise.setLayoutManager(new LinearLayoutManagerWrapp(mActivity,/* new GridLayoutManager(requireActivity(),*/ 3));
        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(() -> {
            if (isScanningModeEnabled) {
                Toast.makeText(mActivity, "Already scanning mode", Toast.LENGTH_SHORT).show();
                swipe_refresh.setRefreshing(false);
            } else {
                swipe_refresh.setRefreshing(false);
//                    fetchLocationWiseData();
            }
            add();
        });
        view.findViewById(R.id.linVideo).setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, ActVideos.class);
            startActivity(intent);
        });

        registerAdsListener();
        view.findViewById(R.id.linCreateCollage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(com.photo.photography.secure_vault.helper.Constants.CollageOpen, com.photo.photography.secure_vault.helper.Constants.CollageOpen);

                Intent intent = new Intent(mActivity, ActSelectPhoto.class);
                intent.putExtra(EXTRA_IS_FRAME_IMAGE, true);

                if (MyApp.getInstance().needToShowAd()) {
                    MyApp.getInstance().showInterstitial(mActivity, intent, false, -1, null);
                } else {
                    startActivity(intent);
                }
            }
        });

        view.findViewById(R.id.linSecureVault).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(mActivity, ActSetupPinLock.class), ActMain.REQUEST_CODE_SECURE_VAULT);
            }
        });
        view.findViewById(R.id.linSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActSettings.startActivity(mActivity);
            }
        });
        view.findViewById(R.id.linDuplicatePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SupportClass.isExternalStoragePermissionGranted(mActivity)) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(com.photo.photography.secure_vault.helper.Constants.DuplicatePhotosOpen, com.photo.photography.secure_vault.helper.Constants.DuplicatePhotosOpen);

                    AppDataBaseHandler dbHandler = new AppDataBaseHandler(mActivity);
                    List<IndividualGroups> sDataList = dbHandler.getAllItems(true);
                    List<IndividualGroups> eDataList = dbHandler.getAllItems(false);
                    if (eDataList.size() > 0 || sDataList.size() > 0) {

                        GlobalVarsAndFunction.setCancelFlag(mActivity, false);

                        long jjj = GlobalVarsAndFunction.getTotalDuplicateMemoryRegain(eDataList);
                        GlobalVarsAndFunction.setMemoryRegainedExact(GlobalVarsAndFunction.getStringSizeLengthFile(jjj));
                        GlobalVarsAndFunction.setMemoryRegainedExactInLong(jjj);
                        GlobalVarsAndFunction.setTotalDuplicatesExact(GlobalVarsAndFunction.getTotalDuplicate(eDataList));

                        long kkk = GlobalVarsAndFunction.getTotalDuplicateMemoryRegain(sDataList);
                        GlobalVarsAndFunction.setMemoryRegainedSimilar(GlobalVarsAndFunction.getStringSizeLengthFile(kkk));
                        GlobalVarsAndFunction.setMemoryRegainedSimilarInLong(kkk);
                        GlobalVarsAndFunction.setTotalDuplicatesSimilar(GlobalVarsAndFunction.getTotalDuplicate(sDataList));

                        GlobalVarsAndFunction.setGroupOfDuplicatesSimilar(mActivity, sDataList, false);
                        GlobalVarsAndFunction.setGroupOfDuplicatesExact(mActivity, eDataList, false);

                        Intent intent = new Intent(mActivity, ActPhotoList.class);
                        intent.putExtra("memoryPopUpAndRecoverPopUp", "showMemoryPopUp");
                        intent.putExtra("tS", "exact");
                        intent.putExtra("showSimilarRegainedPopUpExact", false);
                        intent.putExtra("lastFromScan", true);
                        intent.putExtra("showSimilarRegainedPopUpSimilar", false);

                        if (MyApp.getInstance().needToShowAd()) {
                            MyApp.getInstance().showInterstitial(mActivity, intent, false, -1, null);
                        } else {
                            startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(mActivity, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left).toBundle());
                        }
                    } else {
                        startActivityForResult(new Intent(mActivity, ActDuplicateHomeMain.class), ActMain.REQUEST_CODE_SECURE_VAULT);
                    }
                } else {
                    SupportClass.showTakeWritePermissionDialog(mActivity);
                }
            }
        });

        loadNativeAds();

//        fetchLocationWiseData();

        return view;
    }

    public void add() {

        if (isAdded()) {
//            ArrayList<Media> mPhotosList = new ArrayList<>();
//            for (int i = 0; i < FragRvAllMediaPhotos.mArrayList.size(); i++) {
//                if (!FragRvAllMediaPhotos.mArrayList.get(i).isVideo()) {
//                    mPhotosList.add(FragRvAllMediaPhotos.mArrayList.get(i));
//                }
//            }


            ArrayList<Media> mVideoList = new ArrayList<>();
            for (int i = 0; i < FragRvAllMediaPhotos.mArrayList.size(); i++) {
                if (FragRvAllMediaPhotos.mArrayList.get(i).isVideo()) {
                    mVideoList.add(FragRvAllMediaPhotos.mArrayList.get(i));
                }
            }
            if (mVideoList.size() > 0) {
                if (mVideoList.size() >= 1) {
                    if (mVideoList.get(0).getUri() != null) {
                        Glide.with(mActivity)
                                .load(mVideoList.get(0).getUri())
                                .thumbnail(0.5f)
                                .into(ivVideo1);
                    }
                }
                if (mVideoList.size() >= 2) {
                    if (mVideoList.get(1).getUri() != null) {
                        Glide.with(mActivity)
                                .load(mVideoList.get(1).getUri())
                                .thumbnail(0.5f)
                                .into(ivVideo2);
                    }
                }
                if (mVideoList.size() >= 3) {
                    if (mVideoList.get(2).getUri() != null) {
                        Glide.with(mActivity)
                                .load(mVideoList.get(2).getUri())
                                .thumbnail(0.5f)
                                .into(ivVideo3);
                    }
                }
                if (mVideoList.size() >= 4) {
                    if (mVideoList.get(3).getUri() != null) {
                        Glide.with(mActivity)
                                .load(mVideoList.get(3).getUri())
                                .thumbnail(0.5f)
                                .into(ivVideo4);
                    }
                }

            }
        }
    }

//    private void fetchLocationWiseData() {
//        // set Empty Adapter;
//        locationListTitle = new ArrayList<>();
//        locationListAlbum = new ArrayList<>();
//        locationAlbumsAdapter = new RepeaterLocationAlbums(mActivity, locationListAlbum, FragRvAllMediaExplore.this);
//        recyclerLocationWise.setAdapter(locationAlbumsAdapter);
//
//        swipe_refresh.setRefreshing(false);
//        linProgress.setVisibility(View.VISIBLE);
//        linLocation.setVisibility(View.GONE);
//
//        // Fetch location wise Photos
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new getLocationData().execute();
//            }
//        }, 2000);
//    }

    public double parseDigit(double floatNum) {
        DecimalFormat format = new DecimalFormat("0.##");
        return Double.parseDouble(format.format(floatNum));
    }

    private void registerAdsListener() {
        AdsEventNotifier notifier = AdsNotifierFactory.getInstance().getNotifier(AdsNotifierFactory.EVENT_NOTIFIER_AD_STATUS);
        notifier.registerListener(this, AdsListenerPriority.PRIORITY_HIGH);
    }

    public void loadNativeAds() {
//        if (rel_ads != null) {
//            if (isAdded() && mActivity != null && !mActivity.isFinishing()) {
//                if (MyApp.getInstance().getGNativeHome() != null && MyApp.getInstance().getGNativeHome().size() > 0 && MyApp.getInstance().getGNativeHome().get(0) != null) {
//                    NativeAdView adView = (NativeAdView) LayoutInflater.from(requireActivity()).inflate(R.layout.ads_unified, null);
//                    if (adView != null) {
//                        populateUnifiedNativeAdView(MyApp.getInstance().getGNativeHome().get(0), adView);
//                        rel_ads.removeAllViews();
//                        rel_ads.addView(adView);
//                        rel_ads.setVisibility(View.VISIBLE);
//                    } else {
//                        rel_ads.setVisibility(View.GONE);
//                    }
//
//                } else {
//                    rel_ads.setVisibility(View.GONE);
//                }
//            } else {
//                rel_ads.setVisibility(View.GONE);
//            }
//        }
    }

    @Override
    public int eventNotify(int eventType, final Object eventObject) {
        Log.e("Update: ", "eventNotify");
        int eventState = AdsEventState.EVENT_IGNORED;
        switch (eventType) {
            case AdsEventTypes.EVENT_AD_LOADED_NATIVE:
                Log.e("Update: ", "Case");
                eventState = AdsEventState.EVENT_PROCESSED;
                loadNativeAds();
        }
        return eventState;
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

    @Override
    public int getSelectedCount() {
        return 0;
    }

    @Override
    public int getTotalCount() {
        return 0;
    }

    @Nullable
    @Override
    public View.OnClickListener getToolbarButtonListener(boolean editMode) {
        return null;
    }

    @Nullable
    @Override
    public String getToolbarTitle() {
        return null;
    }

    @Override
    public boolean editMode() {
        return false;
    }

    @Override
    public boolean clearSelected() {
        return false;
    }

    @Override
    public void onItemSelected(int position, ImageView imageView) {
        if (position < locationAlbumsAdapter.getItemCount()) {
            ((ActMain) mActivity).displayLocationMedia(locationAlbumsAdapter.get(position));
        }
    }

    @Override
    public void onItemViewSelected(int position, ImageView imageView) {

    }

    @Override
    public void onSelectMode(boolean selectMode) {

    }

    @Override
    public void onSelectionCountChanged(int selectionCount, int totalCount) {

    }

    @Override
    public void onLocationClick() {

    }

    private class getLocationData extends AsyncTask<String, String, ArrayList<Album>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isScanningModeEnabled = true;
        }

        @Override
        protected ArrayList<Album> doInBackground(String... strings) {
            HashMap<String, String> latLongLocationFilterMap = new HashMap<>();
            double[] recentPicLatLong = new double[2];
//            Log.e("LOCATION_LOG_0", "=============");
            String[] sProjection = new String[]{
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.ORIENTATION,
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.LATITUDE, // return value on device below Android Q
                    MediaStore.Images.Media.LONGITUDE // return value on device below Android Q
            };

            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            //Stores all the images from the gallery in Cursor
            Cursor cursor = mActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, sProjection, null, null, orderBy);
            //Total number of images
            int count = cursor.getCount();

            //Create an array to store path to all the images
            String[] arrPath = new String[count];

            int CURSOR_POS__ID = Media.getIndex(sProjection, MediaStore.Images.Media._ID);
            int CURSOR_POS_DATA = Media.getIndex(sProjection, MediaStore.Images.Media.DATA);
            int CURSOR_POS_DATE_TAKEN = Media.getIndex(sProjection, MediaStore.Images.Media.DATE_TAKEN);
            int CURSOR_POS_MIME_TYPE = Media.getIndex(sProjection, MediaStore.Images.Media.MIME_TYPE);
            int CURSOR_POS_SIZE = Media.getIndex(sProjection, MediaStore.Images.Media.SIZE);
            int CURSOR_POS_ORIENTATION = Media.getIndex(sProjection, MediaStore.Images.Media.ORIENTATION);
            int CURSOR_POS_DATE_MODIFIED = Media.getIndex(sProjection, MediaStore.Images.Media.DATE_MODIFIED);
            int CURSOR_POS_DATE_ADDED = Media.getIndex(sProjection, MediaStore.Images.Media.DATE_ADDED);
            int CURSOR_POS_LATITUDE = Media.getIndex(sProjection, MediaStore.Images.Media.LATITUDE);
            int CURSOR_POS_LONGITUDE = Media.getIndex(sProjection, MediaStore.Images.Media.LONGITUDE);
            int locationImgCount = 0;
            int locationFromGeoCoder = 0;
            int locationFromRecentPic = 0;
            int locationFromSameDistance = 0;
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                //Store the path of the image
                arrPath[i] = cursor.getString(dataColumnIndex);

                Media media = new Media();
                media.setId(cursor.getLong(CURSOR_POS__ID));
                media.setPath(cursor.getString(CURSOR_POS_DATA));
                long dateModify = cursor.getLong(CURSOR_POS_DATE_MODIFIED) * 1000;
                String strDateModify = Media.parseDate(cursor.getLong(CURSOR_POS_DATE_MODIFIED) * 1000);
                long dateCreated = cursor.getLong(CURSOR_POS_DATE_TAKEN);
                String strDateCreated = Media.parseDate(cursor.getLong(CURSOR_POS_DATE_TAKEN));
                long dateAdded = cursor.getLong(CURSOR_POS_DATE_ADDED) * 1000;
                String strDateAdded = Media.parseDate(cursor.getLong(CURSOR_POS_DATE_ADDED) * 1000);

                long finalDateMS = Math.max(dateAdded, dateCreated);
                String strFinalDateMS = Media.parseDate(finalDateMS);
                long finalDateMS2 = Math.max(finalDateMS, dateModify);
                String strFinalDateMS2 = Media.parseDate(finalDateMS2);
                media.setDateModified(finalDateMS2);

                media.setMimeType(cursor.getString(CURSOR_POS_MIME_TYPE));
                media.setSize(cursor.getLong(CURSOR_POS_SIZE));
                media.setOrientation(cursor.getInt(CURSOR_POS_ORIENTATION));
                Uri contentUri = Uri.fromFile(new File(media.getPath()));
                if (contentUri == null)
                    contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, media.getId());
                media.setUri(contentUri.toString());

                double[] latLong = new double[2];

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    try {
                        Uri photoUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getString(CURSOR_POS__ID));

                        // Get location data using the Exifinterface library.
                        // Exception occurs if ACCESS_MEDIA_LOCATION permission isn't granted.
                        photoUri = MediaStore.setRequireOriginal(photoUri);

                        InputStream stream = mActivity.getContentResolver().openInputStream(photoUri);
                        if (stream != null) {
                            ExifInterface exifInterface = new ExifInterface(stream);
                            double[] returnedLatLong = exifInterface.getLatLong();

                            // If lat/long is null, fall back to the coordinates (0, 0).
                            latLong = returnedLatLong != null ? returnedLatLong : new double[2];

                            // Don't reuse the stream associated with
                            // the instance of "ExifInterface".
                            stream.close();
                        } else {
                            // Failed to load the stream, so return the coordinates (0, 0).
                            latLong = new double[2];
                        }
                    } catch (Exception exc) {
//                        Log.e("LOCATION_LOG_1", exc.getLocalizedMessage());
                    }
                } else {
                    latLong = new double[]{cursor.getDouble(CURSOR_POS_LATITUDE), cursor.getDouble(CURSOR_POS_LONGITUDE)};
//                    Log.e("LOCATION_LOG_2", cursor.getDouble(CURSOR_POS_LATITUDE) + " " + cursor.getDouble(CURSOR_POS_LONGITUDE));
                }

                try {
                    if (latLong[0] > 0) {
                        float[] results = new float[1];
                        Location.distanceBetween(latLong[0], latLong[1], recentPicLatLong[0], recentPicLatLong[1], results);
                        float distance = results[0] / 1000;

                        String finalLocation = "";
                        if (distance > 10) {
                            double lattitude = parseDigit(latLong[0]);
                            double longitude = parseDigit(latLong[1]);
                            if (latLongLocationFilterMap.containsKey(lattitude + "-" + longitude)) {
                                finalLocation = latLongLocationFilterMap.get(lattitude + "-" + longitude);
                                locationFromSameDistance++;
//                                Log.e("LOCATION_LOG_3", String.format("%-6s", parseDigit(distance)) + " POS : " + i + ", " + lattitude + "-" + longitude + " FinalLocation => " + String.format("%-12s", (TextUtils.isEmpty(finalLocation) ? "" : finalLocation)));

                            } else {
                                Geocoder gcd = new Geocoder(mActivity, Locale.getDefault());
                                List<Address> addresses = gcd.getFromLocation(lattitude, longitude, 1);
                                if (addresses.size() > 0) {
                                    Address add = addresses.get(0);
                                    String locality = add.getLocality();
                                    String subLocality = add.getSubLocality();
                                    String subAdminArea = add.getSubAdminArea();
                                    String adminArea = add.getAdminArea();
                                    String thoroughfare = add.getThoroughfare();
                                    String subThoroughfare = add.getSubThoroughfare();
                                    String featureName = add.getFeatureName();
                                    finalLocation = /*new Random().nextInt() + "";*/TextUtils.isEmpty(subLocality)
                                            ? (!TextUtils.isEmpty(locality) ? locality : subAdminArea)
                                            : subLocality;

                                    latLongLocationFilterMap.put(lattitude + "-" + longitude, finalLocation);
//                                Log.e("LOCATION_LOG_4", distance + " POS : " + i + ", " + lattitude + "-" + longitude + " FinalLocation => " + String.format("%-12s", (TextUtils.isEmpty(finalLocation) ? "" : finalLocation))
//                                        + ", Locality => " + String.format("%-12s", (TextUtils.isEmpty(locality) ? "" : locality))
//                                        + ", SubLocality => " + String.format("%-12s", (TextUtils.isEmpty(subLocality) ? "" : subLocality))
//                                        + ", SubAdminArea => " + String.format("%-12s", (TextUtils.isEmpty(subAdminArea) ? "" : subAdminArea))
//                                        + ", AdminArea => " + String.format("%-12s", (TextUtils.isEmpty(adminArea) ? "" : adminArea))
//                                        + ", Thoroughfare => " + String.format("%-12s", (TextUtils.isEmpty(thoroughfare) ? "" : thoroughfare))
//                                        + ", SubThoroughfare => " + String.format("%-12s", (TextUtils.isEmpty(subThoroughfare) ? "" : subThoroughfare))
//                                        + ", FeatureName => " + String.format("%-12s", (TextUtils.isEmpty(featureName) ? "" : featureName)));
                                }
                                locationFromGeoCoder++;
                            }
                            recentPicLatLong[0] = lattitude;
                            recentPicLatLong[1] = longitude;
                        } else {
                            finalLocation = latLongLocationFilterMap.get(recentPicLatLong[0] + "-" + recentPicLatLong[1]);
                            locationFromRecentPic++;
                        }
                        if (!TextUtils.isEmpty(finalLocation)) {
                            int locationPos = -1;
                            ArrayList<Media> mediaArrayList = new ArrayList<>();
                            if (locationListTitle.contains(finalLocation)) {
                                locationPos = locationListTitle.indexOf(finalLocation);
                                mediaArrayList = locationListAlbum.get(locationPos).getMediaArrayList();
                            }
                            mediaArrayList.add(media);
                            if (locationPos == -1) {
                                Album album = Album.getLocationMediaAlbum(finalLocation, mediaArrayList);
                                locationListTitle.add(finalLocation);
                                locationListAlbum.add(album);
                                publishProgress("Insert", String.valueOf(locationListTitle.size() - 1), String.valueOf(cursor.getCount()), String.valueOf(i));
//                                Log.e("LOCATION_LOG_4", "Location Inserted : " + finalLocation + " with Size : " + mediaArrayList.size());
                            } else {
                                Album album = locationListAlbum.get(locationPos);
                                album.setMediaArrayList(mediaArrayList);
                                locationListAlbum.set(locationPos, album);
                                publishProgress("Update", String.valueOf(locationPos), String.valueOf(cursor.getCount()), String.valueOf(i));
//                                Log.e("LOCATION_LOG_4", "Location Updated : " + finalLocation + " with Size : " + mediaArrayList.size());
                            }
                        }
                        locationImgCount++;
                    }
                } catch (Exception exc) {
//                    Log.e("LOCATION_LOG_5", "Exception : " + exc.getLocalizedMessage());
                }

//                if (i == 400) {
//                    break;
//                }
            }
            cursor.close();
//            Log.e("LOCATION_LOG_6", "Total Image : " + count + " and fetched location Count : " + locationListTitle.size() + " locationImgCount : " + locationImgCount +
//                    ", locationFromGeoCoder : " + locationFromGeoCoder + ", locationFromRecentPic : " + locationFromRecentPic + ", locationFromSameDistance : " + locationFromSameDistance);
//            Log.e("LOCATION_LOG_7", "=============>  " + locationListTitle.size());
            return locationListAlbum;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvTotalScanned.setText(values[3]);
            tvTotalPhotos.setText(values[2]);
            if (values[0].equalsIgnoreCase("Insert")) {
                linLocation.setVisibility(View.VISIBLE);
                locationAlbumsAdapter.notifyItemInserted(Integer.parseInt(values[1]));
            } else {
                locationAlbumsAdapter.notifyItemChanged(Integer.parseInt(values[1]));
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Album> locationListAlbum) {
            super.onPostExecute(locationListAlbum);
            isScanningModeEnabled = false;
            linProgress.setVisibility(View.GONE);
            swipe_refresh.setRefreshing(false);
        }
    }
}