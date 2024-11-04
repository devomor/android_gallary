package com.photo.photography.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.drew.lang.GeoLocation;
import com.photo.photography.R;
import com.photo.photography.data_helper.Media;
import com.photo.photography.data_helper.metadata_helper.MediaDetailMap;
import com.photo.photography.data_helper.metadata_helper.MetadataHelpers;
import com.photo.photography.callbacks.CallbackRenameClick;
import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Field;
import java.util.Locale;

public class AlertDialogHelper {

    public static AlertDialog getInsertTextDialog(AppCompatActivity activity, EditText editText, @StringRes int title, CallbackRenameClick listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        View dialogLayout = activity.getLayoutInflater().inflate(com.photo.photography.R.layout.dialog_inserts_text, null);
        TextView textViewTitle = dialogLayout.findViewById(R.id.rename_title);

        textViewTitle.setText(title);
        TextView text_ok = dialogLayout.findViewById(R.id.text_ok);
        TextView text_cancel = dialogLayout.findViewById(R.id.text_cancel);

        text_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOkClick(v);
            }
        });
        text_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClick(v);
            }
        });

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParams);
        editText.setSingleLine(true);
        editText.setTextColor(ContextCompat.getColor(activity, R.color.md_grey_600));
        editText.getBackground().mutate().setColorFilter(ContextCompat.getColor(activity, R.color.primaryColor), PorterDuff.Mode.SRC_IN);

        try {
            @SuppressLint("SoonBlockedPrivateApi") Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, null);
        } catch (Exception ignored) {
        }

        ((RelativeLayout) dialogLayout.findViewById(com.photo.photography.R.id.container_edit_text)).addView(editText);

        dialogBuilder.setView(dialogLayout);
        return dialogBuilder.create();
    }

    public static void getTextDialog(Activity activity, String title, String Message, int ok, int cancel, View.OnClickListener okListner, View.OnClickListener cancelListner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_texts, null);

        TextView dialogTitle = dialogLayout.findViewById(R.id.text_dialog_title);
        TextView dialogMessage = dialogLayout.findViewById(R.id.text_dialog_message);
        TextView okBtn = dialogLayout.findViewById(R.id.text_delete);
        okBtn.setText(ok);

        TextView cancelBtn = dialogLayout.findViewById(R.id.text_cancel);
        cancelBtn.setText(cancel);

        dialogTitle.setText(title);
        dialogMessage.setText(Message);
        builder.setView(dialogLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        cancelBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
            cancelListner.onClick(v);
        });
        okBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
            okListner.onClick(v);
        });
    }

    public static AlertDialog getDetailsDialog(final AppCompatActivity activity, final Media media) {
        AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(activity);

        final View dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_medias_details, null);
        ImageView imgMap = dialogLayout.findViewById(R.id.photo_map);
        final GeoLocation location;
        if ((location = media.getGeoLocation()) != null) {

            StaticMapProviders staticMapProvider = StaticMapProviders.fromValue(
                    Hawk.get(activity.getString(R.string.preference_map_provider), StaticMapProviders.GOOGLE_MAPS.getValue()));

            Glide.with(activity.getApplicationContext())
                    .load(staticMapProvider.getUrl(location))
                    .into(imgMap);

            imgMap.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format(Locale.ENGLISH, "geo:%f,%f?z=%d", location.getLatitude(), location.getLongitude(), 17))));
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(activity, R.string.no_app_to_perform, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            imgMap.setVisibility(View.VISIBLE);
            dialogLayout.findViewById(com.photo.photography.R.id.details_title).setVisibility(View.GONE);

        } else imgMap.setVisibility(View.GONE);

        final TextView showMoreText = dialogLayout.findViewById(R.id.details_showmore);
        showMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDetails(dialogLayout, activity);
                showMoreText.setVisibility(View.GONE);
            }
        });

        detailsDialogBuilder.setView(dialogLayout);

        AlertDialog alertDialog = detailsDialogBuilder.create();
        dialogLayout.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        MetadataHelpers mdhelper = new MetadataHelpers();
        MediaDetailMap<String, String> mainDetails = mdhelper.getMainDetails(activity, media);
        loadDetails(dialogLayout, activity, mainDetails);

        return alertDialog;
    }

    private static void loadDetails(View dialogLayout, AppCompatActivity activity, MediaDetailMap<String, String> metadata) {
        LinearLayout detailsTable = dialogLayout.findViewById(R.id.ll_list_details);

        int tenPxInDp = Measures.pxToDp(10, activity);
        int hundredPxInDp = Measures.pxToDp(125, activity);//more or less an hundred. Did not used weight for a strange bug

        for (int index : metadata.getKeySet()) {
            LinearLayout row = new LinearLayout(activity.getApplicationContext());
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView label = new TextView(activity.getApplicationContext());
            TextView value = new TextView(activity.getApplicationContext());
            label.setText(metadata.getLabel(index));
            label.setLayoutParams((new LinearLayout.LayoutParams(hundredPxInDp, LinearLayout.LayoutParams.WRAP_CONTENT)));
            value.setText(metadata.getValue(index));
            value.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
            label.setTypeface(null, Typeface.BOLD);
            label.setGravity(Gravity.END);
            label.setTextSize(16);
            value.setTextSize(16);
            value.setPaddingRelative(tenPxInDp, 0, tenPxInDp, 0);
            row.addView(label);
            row.addView(value);
            detailsTable.addView(row, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private static void showMoreDetails(View dialogLayout, AppCompatActivity activity) {
        MediaDetailMap<String, String> metadata = new MediaDetailMap<>();
        loadDetails(dialogLayout, activity, metadata);
    }

}
