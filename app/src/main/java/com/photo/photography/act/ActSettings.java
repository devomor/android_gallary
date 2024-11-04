package com.photo.photography.act;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.util.preferences.Prefs;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.ActChangeLockStyle;
import com.photo.photography.secure_vault.ActChangePasswordLock;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActSettings extends ActBase {

    public static final int CHANGE_LOCK_STYLE_REQ_CODE = 111;
    private static final int CAMERA_PERMISSIONS = 406;
    String currentLockStyle;
    @BindView(R.id.ll_change_lock_style_caption)
    TextView ll_change_lock_style_caption;
    @BindView(R.id.ll_theme_caption)
    TextView ll_theme_caption;
//    FirebaseAnalytics mFirebaseAnalytics;
    private Toolbar toolbar;
    private Unbinder unbinder;

    public static void startActivity(@NonNull Context context) {
        Intent intent = new Intent(context, ActSettings.class);
        if (MyApp.getInstance().needToShowAd()) {
            MyApp.getInstance().showInterstitial((Activity) context, intent, false, -1, null);
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        unbinder = ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Init and Load Ads
        loadBannerAds(findViewById(R.id.adContainerView));

        setSupportActionBar(toolbar);
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        boolean isPinlock = SupportClass.getSettingsInfo(SupportClass.IS_PIN_LOCK);
        if (isPinlock) {
            currentLockStyle = getResources().getString(R.string.txt_pin_lock);
            ll_change_lock_style_caption.setText(getString(R.string.txt_pin_lock));
        } else {
            currentLockStyle = getResources().getString(R.string.txt_pattern_lock);
            ll_change_lock_style_caption.setText(getString(R.string.txt_pattern_lock));
        }

        if (AppCompatDelegate.MODE_NIGHT_NO == Prefs.getTheme()) {
            ll_theme_caption.setText(getString(R.string.light));
        } else if (AppCompatDelegate.MODE_NIGHT_YES == Prefs.getTheme()) {
            ll_theme_caption.setText(getString(R.string.dark));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSIONS:
                boolean gotPermission = grantResults.length > 0;

                for (int result : grantResults) {
                    gotPermission &= result == PackageManager.PERMISSION_GRANTED;
                }

                if (gotPermission) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, true);
                    editor.commit();

                } else {
                    Toast.makeText(ActSettings.this, getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.ll_theme)
    public void onThemeClicked(View view) {
        dialogTheme();
    }

    @OnClick(R.id.ll_change_password)
    public void onChangePasswordClicked(View view) {
        Intent intent = new Intent(ActSettings.this, ActChangePasswordLock.class);
        if (MyApp.getInstance().needToShowAd()) {
            MyApp.getInstance().showInterstitial(ActSettings.this, intent, false, -1, null);
        } else {
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_change_lock_style)
    public void onChangeLockStyleClicked(View view) {
        final String[] items = {getString(R.string.txt_pin_lock), getString(R.string.txt_pattern_lock), getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(ActSettings.this);
        builder.setTitle("Change Lock Style!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getResources().getString(R.string.txt_pin_lock))) {
                    if (!currentLockStyle.equals(items[item])) {
                        Log.e("TAG", "You Change " + currentLockStyle + " to " + items[item] + " lock");
                        currentLockStyle = items[item];
                        Intent intent = new Intent(ActSettings.this, ActChangeLockStyle.class);
                        startActivityForResult(intent, CHANGE_LOCK_STYLE_REQ_CODE);
                    }

                } else if (items[item].equals(getResources().getString(R.string.txt_pattern_lock))) {
                    if (!currentLockStyle.equals(items[item])) {
                        Log.e("TAG", "You Change " + currentLockStyle + " to " + items[item] + " lock");
                        currentLockStyle = items[item];
                        Intent intent = new Intent(ActSettings.this, ActChangeLockStyle.class);
                        startActivityForResult(intent, CHANGE_LOCK_STYLE_REQ_CODE);
                    }

                } else if (items[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_LOCK_STYLE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                ll_change_lock_style_caption.setText(currentLockStyle);
                Intent broadCastIntent = new Intent("SHOW_LOCK");
                broadCastIntent.putExtra("COMMAND", "update");
                Log.d("in Service setting", SupportClass.getBoolean(SupportClass.IS_PIN_LOCK) + "");
                sendBroadcast(broadCastIntent);
                Log.e("TAG", "Setting menu activityForResult OK.");
            } else {
                if (currentLockStyle.equals(getResources().getString(R.string.txt_pin_lock))) {
                    currentLockStyle = getResources().getString(R.string.txt_pattern_lock);
                } else {
                    currentLockStyle = getResources().getString(R.string.txt_pin_lock);
                }
                Log.e("TAG", "Setting menu activityForResult CANCEL.");
            }
        }
    }

    public void dialogTheme() {
        final Dialog dialog = new Dialog(ActSettings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_theme_change);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_apply = dialog.findViewById(R.id.tv_apply);
        RadioGroup radioGroup = dialog.findViewById(R.id.radio_group);
        RadioButton radio_light = dialog.findViewById(R.id.radio_light);
        RadioButton radio_dark = dialog.findViewById(R.id.radio_dark);
        if (AppCompatDelegate.MODE_NIGHT_YES == Prefs.getTheme()) {
            radio_dark.setChecked(true);
        } else if (AppCompatDelegate.MODE_NIGHT_NO == Prefs.getTheme()) {
            radio_light.setChecked(true);
        }

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_light && AppCompatDelegate.MODE_NIGHT_YES == Prefs.getTheme()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(com.photo.photography.secure_vault.helper.Constants.theme, com.photo.photography.secure_vault.helper.Constants.white);

                    Prefs.setTheme(AppCompatDelegate.MODE_NIGHT_NO);
                    GlobalVarsAndFunction.setAppTheme(ActSettings.this, AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_dark && AppCompatDelegate.MODE_NIGHT_NO == Prefs.getTheme()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(com.photo.photography.secure_vault.helper.Constants.theme, com.photo.photography.secure_vault.helper.Constants.black);

                    Prefs.setTheme(AppCompatDelegate.MODE_NIGHT_YES);
                    GlobalVarsAndFunction.setAppTheme(ActSettings.this, AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    Log.e("TAG", "Same theme selected, not changed");
                }
                restartScreenApp();
            }
        });

        dialog.show();
    }
}
