package com.photo.photography.collage.screen.frag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.photo.photography.R;
import com.photo.photography.collage.helper.HelperALog;
import com.photo.photography.collage.view.AlphaColorSelectView;
import com.photo.photography.collage.view.ColorChoosView;
import com.photo.photography.collage.callback.CallbackOnChooseAlphaColor;
import com.photo.photography.collage.callback.CallbackOnChooseColor;
import com.photo.photography.collage.callback.CallbackOnChooseRGB;


public class FragColorChooser extends FragBase implements
        CallbackOnChooseAlphaColor, CallbackOnChooseRGB {
    private static final String TAG = FragColorChooser.class
            .getSimpleName();
    private View mColorView;
    private TextView mColorTextView;
    private AlphaColorSelectView mAlphaColorSelectorView;
    private ColorChoosView mColorChooserView;
    private CallbackOnChooseColor mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof CallbackOnChooseColor) {
            mListener = (CallbackOnChooseColor) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HelperALog.d(TAG, "onCreateView");
        setTitle(getString(R.string.select_color));
        View rootView = inflater.inflate(R.layout.frag_color_chooser,
                container, false);
        mColorView = rootView.findViewById(R.id.colorView);
        mColorTextView = (TextView) rootView.findViewById(R.id.colorTextView);
        mAlphaColorSelectorView = (AlphaColorSelectView) rootView
                .findViewById(R.id.alphaColorView);
        mAlphaColorSelectorView.setListener(this);
        mColorChooserView = (ColorChoosView) rootView
                .findViewById(R.id.colorChooserView);
        mColorChooserView.setListener(this);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        HelperALog.d(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        HelperALog.d(TAG, "onPause");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_color_chooser, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            if (mListener != null) {
                mListener.setSelectedColor(mAlphaColorSelectorView
                        .getSelectedColor());
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChooseRGB(int rgb) {
        mAlphaColorSelectorView.setOriginalColor(rgb);
    }

    @Override
    public void onChooseColor(int color) {
        mColorView.setBackgroundColor(color);
        mColorTextView.setText("#" + Integer.toHexString(color));
    }

    @Override
    public void onDestroyView() {
        mColorChooserView.recyleImages();
        super.onDestroyView();
        HelperALog.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HelperALog.d(TAG, "onDestroy");
    }
}
