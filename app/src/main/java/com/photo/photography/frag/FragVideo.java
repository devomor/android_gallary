package com.photo.photography.frag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.photo.photography.R;
import com.photo.photography.act.ActSingleMedia;
import com.photo.photography.data_helper.Media;
import com.photo.photography.view.videoplayers.CustomExoPlayersView;
import com.photo.photography.view.videoplayers.CustomPlayBackControllers;
import com.photo.photography.BuildConfig;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragVideo extends FragBaseMedia implements CustomPlayBackControllers.VisibilityListener {

    public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String DRM_MULTI_SESSION = "drm_multi_session";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.player_view)
    CustomExoPlayersView simpleExoPlayerView;
    Uri uriLocal;
    VideoViewBroadCastReceiver receiver;
    private Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private MappingTrackSelector trackSelector;
    private TrackGroupArray lastSeenTrackGroupArray;
    private int resumeWindow;
    private long resumePosition;
    private boolean shouldAutoPlay;
    private boolean inErrorState;

    @NonNull
    public static FragVideo newInstance(@NonNull Media media, int position) {
        return FragBaseMedia.newInstance(new FragVideo(), media, position);
    }

    public boolean isBuildVersionValidForPlay() {
        Log.e("isBuildVersion", "ValidForPlay : " + (Build.VERSION.SDK_INT >= 26));
        return false/*Build.VERSION.SDK_INT >= 26*/;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_video, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            if (media.getFile() != null && media.getFile().exists()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    uriLocal = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", media.getFile());
//                    uriLocal = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", media.getFile());
                } else {
                    uriLocal = Uri.fromFile(media.getFile());
                }
            }
            imageView.setTransitionName(String.valueOf(media.getId()));

            Glide.with(mActivity)
                    .load(uriLocal)
                    .into(imageView);

            setTapListener(imageView);
        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.getMessage());
            Log.e("TAG_ERR", e.toString());
        }

        if (isBuildVersionValidForPlay()) {
            shouldAutoPlay = true;
            clearResumePosition();
            mediaDataSourceFactory = buildDataSourceFactory(true);
            mainHandler = new Handler();

            if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
                CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
            }

            simpleExoPlayerView.setUseController(false);
            simpleExoPlayerView.requestFocus();
            simpleExoPlayerView.setVisibility(View.VISIBLE);
        } else {
            simpleExoPlayerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isBuildVersionValidForPlay()) {
            initializePlayer();
            if (player != null)
                Log.e("PAUSE_TRACE", "onStart video : " + player.getDuration());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isBuildVersionValidForPlay()) {
            try {
                Log.e("VOLUME_TRACE", "registerReceiver : " + "fragment-" + position);
                receiver = new VideoViewBroadCastReceiver();
                mActivity.registerReceiver(receiver, new IntentFilter("fragment-" + position));
            } catch (Exception e) {
                Log.e("VOLUME_TRACE", "Exception : " + e.getLocalizedMessage());
            }
            initializePlayer();
            if (player != null)
                Log.e("PAUSE_TRACE", "onResume video : " + player.getDuration());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isBuildVersionValidForPlay()) {
            try {
                if (receiver != null) {
                    Log.e("VOLUME_TRACE", "unregisterReceiver");
                    mActivity.unregisterReceiver(receiver);
                }
            } catch (Exception e) {
                Log.e("VOLUME_TRACE", "Exception : " + e.getLocalizedMessage());
            }
            if (player != null)
                Log.e("PAUSE_TRACE", "onPause video : " + player.getDuration());
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isBuildVersionValidForPlay()) {
            if (player != null)
                Log.e("PAUSE_TRACE", "onStop video : " + player.getDuration());
            releasePlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isBuildVersionValidForPlay()) {
                initializePlayer();
            }
        } else {
            showToast(R.string.storage_permission_denied);
            mActivity.finish();
        }
    }

    /**
     * Internal methods
     */
    private void initializePlayer() {
        Intent intent = mActivity.getIntent();

        boolean needNewPlayer = player == null;
        if (needNewPlayer) {

            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            lastSeenTrackGroupArray = null;

            UUID drmSchemeUuid = intent.hasExtra(DRM_SCHEME_UUID_EXTRA) ? UUID.fromString(intent.getStringExtra(DRM_SCHEME_UUID_EXTRA)) : null;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (drmSchemeUuid != null) {
                String drmLicenseUrl = intent.getStringExtra(DRM_LICENSE_URL);
                String[] keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
                boolean multiSession = intent.getBooleanExtra(DRM_MULTI_SESSION, false);
                int errorStringId = R.string.error_drm_unknown;

                try {
                    drmSessionManager = buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl, keyRequestPropertiesArray, multiSession);
                } catch (UnsupportedDrmException e) {
                    errorStringId = e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
                }
                if (drmSessionManager == null) {
                    showToast(errorStringId);
                    return;
                }
            }

            DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(mActivity, drmSessionManager, DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER);
            player = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector);
            player.addListener(new PlayerEventListener());
            simpleExoPlayerView.setPlayer(player);
            player.setPlayWhenReady(shouldAutoPlay);
        }

        Uri[] uris;
        String[] extensions;
        uris = new Uri[]{uriLocal};
        extensions = new String[]{"video/*"};

        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i < uris.length; i++) {
            mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
        }
        MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);

        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
        if (haveResumePosition) {
            player.seekTo(resumeWindow, resumePosition);
        }
        player.prepare(mediaSource, !haveResumePosition, false);
        inErrorState = false;

        setVolumeActivated(false);
    }

    public void setVolumeActivated(boolean isActivated) {
        if (player != null)
            player.setVolume(isActivated ? 1f : 0f);
        ((ActSingleMedia) requireActivity()).setVolumeControlView(isActivated);
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession) throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, null, multiSession);
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false), new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false), new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, null);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(), mainHandler, null);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = Math.max(0, player.getContentPosition());
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return new DefaultDataSourceFactory(mActivity, useBandwidthMeter ? BANDWIDTH_METER : null,
                buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null));
    }

    HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(mActivity, "LeafPic"), bandwidthMeter);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return new DefaultHttpDataSourceFactory(Util.getUserAgent(mActivity, "LeafPic"), useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mActivity.getMenuInflater().inflate(R.menu.menu_video_player, menu);
        return true;
    }

    //User controls
    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVisibilityChange(int visibility) {
        if (visibility == View.GONE)
            ((ActSingleMedia) mActivity).hideSystemUI();
//            hideControls();
        else if (visibility == View.VISIBLE)
            ((ActSingleMedia) mActivity).showSystemUI();
//            showControls();
    }

    public class VideoViewBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("VOLUME_TRACE", "onReceive broadcast");
            if (player != null) {
                boolean isActivated = intent.getBooleanExtra("volumeOn", false);
                Log.e("VOLUME_TRACE", "onReceive with isActivated : " + isActivated);
                setVolumeActivated(isActivated);
            }
        }
    }

    private class PlayerEventListener extends Player.DefaultEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
                ((ActSingleMedia) mActivity).showSystemUI();
//                showControls();
                initializePlayer();
            }
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
            if (inErrorState) {
                // This will only occur if the user has performed a seek whilst in the error state. Update
                // the resume position so that if the user then retries, playback will resume from the
                // position to which they seeked.
                updateResumePosition();
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;
            if (e.type == ExoPlaybackException.TYPE_RENDERER) {
                Exception cause = e.getRendererException();
                if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                    // Special case for decoder initialization failures.
                    MediaCodecRenderer.DecoderInitializationException decoderInitializationException = (MediaCodecRenderer.DecoderInitializationException) cause;
                    if (decoderInitializationException.decoderName == null)
                        if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException)
                            errorString = getString(R.string.error_querying_decoders);
                        else if (decoderInitializationException.secureDecoderRequired)
                            errorString = getString(R.string.error_no_secure_decoder, decoderInitializationException.mimeType);
                        else
                            errorString = getString(R.string.error_no_decoder, decoderInitializationException.mimeType);
                    else
                        errorString = getString(R.string.error_instantiating_decoder, decoderInitializationException.decoderName);
                }
            }
            if (errorString != null)
                showToast(errorString);
            inErrorState = true;
            ((ActSingleMedia) mActivity).showSystemUI();
//            showControls();
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            if (trackGroups != lastSeenTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        showToast(R.string.error_unsupported_audio);
                    }
                }
                lastSeenTrackGroupArray = trackGroups;
            }
        }
    }
}