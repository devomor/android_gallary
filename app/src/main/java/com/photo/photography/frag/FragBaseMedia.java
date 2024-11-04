package com.photo.photography.frag;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.photo.photography.data_helper.Media;

/**
 * A Base Fragment for showing Media.
 */
public abstract class FragBaseMedia extends FragBase {

    private static final String ARGS_MEDIA = "args_media";
    private static final String ARGS_MEDIA_POSITION = "args_media_position";

    protected int position;
    protected Media media;
    private MediaTapListener mediaTapListener;

    @NonNull
    protected static <T extends FragBaseMedia> T newInstance(@NonNull T mediaFragment, @NonNull Media media, int position) {
        Bundle args = new Bundle();
        args.putParcelable(ARGS_MEDIA, media);
        args.putInt(ARGS_MEDIA_POSITION, position);
        mediaFragment.setArguments(args);
        return mediaFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MediaTapListener) mediaTapListener = (MediaTapListener) context;
    }

    private void fetchArgs() {
        Bundle args = getArguments();
        if (args == null) throw new RuntimeException("Must pass arguments to Media Fragments!");
        media = getArguments().getParcelable(ARGS_MEDIA);
        position = getArguments().getInt(ARGS_MEDIA_POSITION);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchArgs();
    }

    protected void setTapListener(@NonNull View view) {
        view.setOnClickListener(v -> onTapped());
    }

    public void onTapped() {
        mediaTapListener.onViewTapped();
    }

    /**
     * Interface for listeners to react on Media Clicks.
     */
    public interface MediaTapListener {

        /**
         * Called when user taps on the Media view.
         */
        void onViewTapped();
    }
}
