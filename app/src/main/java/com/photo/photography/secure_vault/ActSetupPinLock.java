package com.photo.photography.secure_vault;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.util.helper.UserHelpers;
import com.photo.photography.util.PermissionUtil;
import com.photo.photography.util.utils.CameraUtil;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.ImageLoader.ImageLoaders;
import com.photo.photography.secure_vault.views.Lock9Views;
import com.photo.photography.secure_vault.helper.Constants;


import java.util.ArrayList;
import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActSetupPinLock extends ActBase {

    private static final int CAMERA_PERMISSIONS = 406;
    private static final String TAG = "Bio";
    public int tryCount = 0;
    @BindView(R.id.spin_security_question)
    Spinner mSecurityQuestion;
    @BindView(R.id.dial_text)
    TextView mDialedText;
    @BindView(R.id.switchEnableSecretSnap)
    SwitchCompat mSwitchEnableSecretSnap;
    @BindView(R.id.tv_passcode1)
    TextView mPasscode1;
    @BindView(R.id.tv_passcode2)
    TextView mPasscode2;
    @BindView(R.id.tv_passcode3)
    TextView mPasscode3;
    @BindView(R.id.tv_passcode4)
    TextView mPasscode4;
    @BindView(R.id.forgot_passcode)
    TextView mForgotPasscode;
    @BindView(R.id.forgot_passcode_pattern)
    TextView mForgotPasscodePattern;
    @BindView(R.id.patternLockMsg)
    TextView patternLockMsg;
    @BindView(R.id.lock_9_view)
    Lock9Views lock9View;
    @BindView(R.id.pad_x)
    ImageView mDeleteButtonPinLock;
    @BindView(R.id.pad_finger_print)
    ImageView mfingerPrint;
    @BindView(R.id.pad_finger_print_pattern)
    ImageView mfingerPrintPattern;
    @BindView(R.id.patternLockView)
    RelativeLayout patternLockView;
    @BindView(R.id.pinLockView)
    RelativeLayout pinLockView;
    @BindView(R.id.lin_security_question_answer)
    LinearLayout lin_security_question_answer;
    @BindView(R.id.et_input_answer)
    EditText et_input_answer;
    String userAnswer = "";
    @BindView(R.id.stepIndicator)
    LinearLayout stepIndicator;
    @BindView(R.id.step1)
    ImageView step1;
    @BindView(R.id.step12)
    ImageView step12;
    @BindView(R.id.step2)
    ImageView step2;
    @BindView(R.id.step23)
    ImageView step23;
    @BindView(R.id.step3)
    ImageView step3;
    String pinCode = "";
    boolean isFirstTime;
    boolean isRepeatScreen;
    @BindView(R.id.enter_pincode)
    TextView mTextEnterPin;
    @BindView(R.id.viewFinder)
    PreviewView viewFinder;
    Bitmap selectDrawable, unSelectDrawable;
    boolean isVibrator = false, isSound = false;
    AlertDialog alert;
    ImageLoaders imageLoader;
    @BindView(R.id.layout_passcode)
    LinearLayout mlayoutDots;
    String[] arr_security_question;
//    FirebaseAnalytics mFirebaseAnalytics;
    private Vibrator vibrator;
    private SoundPool soundPool;
    private int musicId;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @OnClick(R.id.btn_submit)
    public void submitClick() {
        userAnswer = et_input_answer.getText().toString().trim();
        if (mSecurityQuestion.getSelectedItemPosition() == 0) {
            Toast.makeText(this, getString(R.string.plese_select_security_question), Toast.LENGTH_SHORT).show();
            mSecurityQuestion.requestFocus();

        } else if (TextUtils.isEmpty(userAnswer)) {
            et_input_answer.setError(getString(R.string.txt_field_not_empty));
            et_input_answer.requestFocus();

        } else {
            if (mSwitchEnableSecretSnap.isChecked()) {
                if (PermissionUtil.isCameraPermissionsGranted(ActSetupPinLock.this)) {
                    doSetupPinlockNext();

                } else {
                    PermissionUtil.requestPermissions(this, CAMERA_PERMISSIONS, Manifest.permission.CAMERA);
                }

            } else {
                doSetupPinlockNext();
            }
        }
    }

    private void doSetupPinlockNext() {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.vaultRegister, Constants.vaultRegister);
//        mFirebaseAnalytics.logEvent(Constants.vault, bundle);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SupportClass.SELECTED_RECOVERY_SECURITY_QUESTION, arr_security_question[mSecurityQuestion.getSelectedItemPosition()]);
        editor.putString(SupportClass.SELECTED_RECOVERY_SECURITY_ANSWER, userAnswer);
        editor.putBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, mSwitchEnableSecretSnap.isChecked());
        editor.commit();

        startMainDrawerActivity();
        finish();
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
                    doSetupPinlockNext();

                } else {
                    Toast.makeText(ActSetupPinLock.this, getString(R.string.camera_permission_denied), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void startMainDrawerActivity() {
        Intent intent = new Intent(getApplicationContext(), ActVaultOption.class);
        MyApp.getInstance().showInterstitial(this, intent, false, -1, null);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lock_pads);

//        hideSystemBars();

//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        ButterKnife.bind(this);


        //set lock preferences
        SupportClass.saveBoolean(false, Constants.IS_PHONE_LOCK_APPLIED);
        SupportClass.saveBoolean(false, Constants.IS_PHONE_LOCK_CANCEL_BY_CALL);

        patternLockView.setVisibility(View.GONE);
        pinLockView.setVisibility(View.VISIBLE);
        mDeleteButtonPinLock.setVisibility(View.GONE);
        lin_security_question_answer.setVisibility(View.GONE);

        arr_security_question = getResources().getStringArray(R.array.security_question);
        ArrayAdapter aa = new ArrayAdapter(ActSetupPinLock.this, R.layout.spinner_items, arr_security_question);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_items);
        mSecurityQuestion.setAdapter(aa);

        //set font
        SupportClass.setFontText(mTextEnterPin);
        SupportClass.setFontText(patternLockMsg);

        imageLoader = new ImageLoaders(getApplicationContext());

        if (!SupportClass.getSettingsInfo(SupportClass.IS_DEFAULT_SET)) {
            SupportClass.createSettingsPrefrences();
            SupportClass.setSettingsInfo(SupportClass.IS_DEFAULT_SET, true);
        }

        pinCode = getPinCode();
        if (pinCode.isEmpty()) {
            stepIndicator.setVisibility(View.VISIBLE);
            mfingerPrint.setVisibility(View.INVISIBLE);
            mfingerPrintPattern.setVisibility(View.INVISIBLE);
            mForgotPasscode.setVisibility(View.GONE);
            mForgotPasscodePattern.setVisibility(View.GONE);
            isFirstTime = true;
            isRepeatScreen = false;
            mTextEnterPin.setText(getString(R.string.txt_set_passcode));
            initPinLockData();

        } else {
            mfingerPrint.setVisibility(View.VISIBLE);
            mfingerPrintPattern.setVisibility(View.VISIBLE);
            mForgotPasscode.setVisibility(View.VISIBLE);
            mForgotPasscodePattern.setVisibility(View.VISIBLE);
            isFirstTime = false;

            setupLockData();
        }

        // ---------- forgot pin menu -------------
        mForgotPasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForgotPassword();
            }
        });
        // ---------- forgot pin menu -------------
        mForgotPasscodePattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForgotPassword();
            }
        });

        saveSelectedArrayList(SupportClass.getArraylistSavedApps(SupportClass.SAVE_APPLICATION_PREF));
        Log.e("TAG", "SetupPinLockActivity Vibrate Enabled " + SupportClass.getSettingsInfo(SupportClass.IS_VIBRATE_ENABLE));
        if (SupportClass.getSettingsInfo(SupportClass.IS_VIBRATE_ENABLE)) {
            isVibrator = true;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }
        Log.e("TAG", "SetupPinLockActivity Sound Enabled " + SupportClass.getSettingsInfo(SupportClass.IS_SOUND_ENABLE));
        if (SupportClass.getSettingsInfo(SupportClass.IS_SOUND_ENABLE)) {
            isSound = true;
            soundPool = SupportClass.buildSoundPool();
            musicId = soundPool.load(ActSetupPinLock.this, R.raw.beep_tune, 1);
        }
        if (UserHelpers.getVaultSet()) {
            checkForBioMetrics();
        }

        mfingerPrint.setOnClickListener(view -> {
            UserHelpers.setFailCount(0);
            FigureAuthenticity();
        });
        mfingerPrintPattern.setOnClickListener(view -> {
            UserHelpers.setFailCount(0);
            FigureAuthenticity();
        });
    }

    private Executor getMainThreadExecutor() {
        return new MainThreadExecutor();
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        // Callback for biometric authentication result
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == 7) {
                    Toast.makeText(ActSetupPinLock.this, getString(R.string.error_maximum_attempt_30_sec_wait), Toast.LENGTH_SHORT).show();
                } else if (errorCode == 5 || errorCode == 13) {
                } else {
                    Toast.makeText(ActSetupPinLock.this, errString, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Log.i(TAG, "onAuthenticationSucceeded");
                super.onAuthenticationSucceeded(result);
//                Toast.makeText(App_Make_Pswd_Lock_Act.this, "onAuthenticationSucceeded", Toast.LENGTH_SHORT).show();
                startMainDrawerActivity();
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                int fail = UserHelpers.getFailCount();
                UserHelpers.setFailCount(fail + 1);

                if (UserHelpers.getFailCount() == 4) {
                    // linBioLogin.setVisibility(View.GONE);
                    Toast.makeText(ActSetupPinLock.this, getString(R.string.innvalid_biometrics), Toast.LENGTH_SHORT).show();
                    biometricPrompt.cancelAuthentication();
                }
            }
        };
    }

    private void FigureAuthenticity() {
        try {
            BiometricPrompt.AuthenticationCallback authenticationCallback = getAuthenticationCallback();
            biometricPrompt = new BiometricPrompt(this, getMainThreadExecutor(), authenticationCallback);

            // Set prompt info
            promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.biometric_login_for_mykase))
                    .setSubtitle(getString(R.string.log_in_using_your_biometric_credential))
                    .setNegativeButtonText(getString(R.string.close))
                    .build();

            biometricPrompt.authenticate(promptInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void checkForBioMetrics() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            BiometricManager biometricManager = BiometricManager.from(this);
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                    mfingerPrint.setVisibility(View.VISIBLE);
                    mfingerPrintPattern.setVisibility(View.VISIBLE);
//                    iv_biometric_login_pattern.setVisibility(View.VISIBLE);
//                    iv_biometric_login_pattern.playAnimation();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Log.e("MY_APP_TAG", "No biometric features available on this device.");
                    mfingerPrint.setVisibility(View.INVISIBLE);
                    mfingerPrintPattern.setVisibility(View.INVISIBLE);
//                    iv_biometric_login_pattern.setVisibility(View.GONE);
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                    mfingerPrint.setVisibility(View.INVISIBLE);
                    mfingerPrintPattern.setVisibility(View.INVISIBLE);
//                    iv_biometric_login_pattern.setVisibility(View.GONE);
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Log.e("MY_APP_TAG", "The user hasn't associated " +
                            "any biometric credentials with their account.");
                    mfingerPrint.setVisibility(View.INVISIBLE);
                    mfingerPrintPattern.setVisibility(View.INVISIBLE);
//                    iv_biometric_login_pattern.setVisibility(View.GONE);
                    break;
            }
        } else {
            mfingerPrint.setVisibility(View.INVISIBLE);
            mfingerPrintPattern.setVisibility(View.INVISIBLE);
//            iv_biometric_login_pattern.setVisibility(View.GONE);
        }
    }

    private void setupLockData() {
        boolean currentLockStyle = SupportClass.getSettingsInfo(SupportClass.IS_PIN_LOCK);
        if (currentLockStyle) {
            initPinLockData();
        } else {
            initPatternData();
            lock9View.setCallBack(new Lock9Views.CallBack() {
                @Override
                public void onFinish(String password) {
                    String passss = getPinCode();
                    Log.e("TAG", "Your pattern Pass : " + password);
                    if (passss.equals(password)) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                        String recoveryQuestion = preferences.getString(SupportClass.SELECTED_RECOVERY_SECURITY_QUESTION, "");
                        String recoveryAnswer = preferences.getString(SupportClass.SELECTED_RECOVERY_SECURITY_ANSWER, "");
                        Log.e("TAG", "Your Recovery Question : " + recoveryQuestion + ", Answer : " + recoveryAnswer);
                        tryCount = 0;
                        if (recoveryQuestion.equals("") && recoveryAnswer.equals("")) {
                            if (userAnswer.equals("")) {
                                patternLockView.setVisibility(View.GONE);
                                pinLockView.setVisibility(View.GONE);
                                mDeleteButtonPinLock.setVisibility(View.GONE);
                                et_input_answer.setVisibility(View.VISIBLE);
                                lin_security_question_answer.setVisibility(View.VISIBLE);

                            } else {
                                patternLockView.setVisibility(View.GONE);
                                pinLockView.setVisibility(View.GONE);
                                mDeleteButtonPinLock.setVisibility(View.GONE);
                                lin_security_question_answer.setVisibility(View.VISIBLE);
                            }

                        } else {
                            startMainDrawerActivity();
                            finish();
                        }
                    } else {
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                        patternLockMsg.startAnimation(shake);
                        patternLockMsg.setText(getString(R.string.txt_wrong_pattern));
                        tryCount++;
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                        if (tryCount % 3 == 0 && preferences.getBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, false))
                            CameraUtil.takeSnapShot(ActSetupPinLock.this, viewFinder);
                    }
                }
            });
        }
    }

    private void initPatternData() {
        pinLockView.setVisibility(View.GONE);
        mDeleteButtonPinLock.setVisibility(View.GONE);
        patternLockView.setVisibility(View.VISIBLE);
    }

    private void initPinLockData() {
        pinLockView.setVisibility(View.VISIBLE);
        mDeleteButtonPinLock.setVisibility(View.VISIBLE);
        patternLockView.setVisibility(View.GONE);

        //SET PIN LOCK THEME
//        changeTheme(SupportedClass.getBoolean(SupportedClass.THEME_CHANGE));
//        selectDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.ic_o);
//        unSelectDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.ic_o1);
        setDefault();
    }

    private void DialogForgotPassword() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActSetupPinLock.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_forgot_pass, null);

        TextView btnNo = (TextView) dialogView.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        TextView btnYes = (TextView) dialogView.findViewById(R.id.btnYes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActSetupPinLock.this, ActRecoveryEmail.class);
                alert.dismiss();
                startActivityForResult(intent, 1);
            }
        });
        builder.setView(dialogView);

        alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "On Activity For Result Called in SetupPinLockActivity" + requestCode);
        if (requestCode == 1) {
            try {
                setDefault();
            } catch (Exception e) {
            }
            pinCode = getPinCode();

        }

        if (requestCode == 2) {
            try {
                setupLockData();
            } catch (Exception e) {
            }
            pinCode = getPinCode();

        }
    }

    public void saveSelectedArrayList(ArrayList<String> savedArrayList) {
        SupportClass.saveArraylistSavedApps(savedArrayList, SupportClass.SAVE_APPLICATION_PREF);
        Log.d("savedArrayList", savedArrayList.size() + "");
    }

    public String getPinCode() {
        return SupportClass.getString(SupportClass.PIN_LOCK);
    }

    public void setPinCode() {
        SupportClass.saveString(pinCode, SupportClass.PIN_LOCK);
    }

    @SuppressLint("MissingPermission")
    @OnClick({R.id.pad_0, R.id.pad_1, R.id.pad_2, R.id.pad_3, R.id.pad_4, R.id.pad_5, R.id.pad_6, R.id.pad_7, R.id.pad_8, R.id.pad_9, R.id.pad_x})
    public void dialPadClicked(View v) {
        if (mDialedText.getText().toString().length() < 5) {
            if (isVibrator) {
                vibrator.vibrate(60);
            }
            if (isSound) {
                soundPool.play(musicId, 1, 1, 0, 0, 1);
            }
            String value = "";
            switch (v.getId()) {
                case R.id.pad_0:
                    value = "0";
                    break;
                case R.id.pad_1:
                    value = "1";
                    break;
                case R.id.pad_2:
                    value = "2";
                    break;
                case R.id.pad_3:
                    value = "3";
                    break;
                case R.id.pad_4:
                    value = "4";
                    break;
                case R.id.pad_5:
                    value = "5";
                    break;
                case R.id.pad_6:
                    value = "6";
                    break;
                case R.id.pad_7:
                    value = "7";
                    break;
                case R.id.pad_8:
                    value = "8";
                    break;
                case R.id.pad_9:
                    value = "9";
                    break;
                case R.id.pad_x:
                    String text = mDialedText.getText().toString();
                    if (text.length() >= 1) {
                        mDialedText.setText(text.substring(0, text.length() - 1));
                        changePasscodeImage();
                    }

                    if (isFirstTime && mDialedText.getText().toString().equals("")) {
                        mTextEnterPin.setText(R.string.enter_code);
                        isRepeatScreen = false;
                    }

                    value = "";
                    break;
            }

            if (!value.isEmpty()) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alphas));
                String text = mDialedText.getText().toString();
                mDialedText.setText(text + value);
                changePasscodeImage();
                if (mDialedText.getText().toString().length() == 4) {
                    if (isFirstTime == true) {
                        mfingerPrint.setVisibility(View.INVISIBLE);
                        mfingerPrintPattern.setVisibility(View.INVISIBLE);
                        performFirstTime();
                    } else {
                        mfingerPrint.setVisibility(View.VISIBLE);
                        mfingerPrintPattern.setVisibility(View.VISIBLE);
                        if (pinCode.equals(mDialedText.getText().toString())) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                            String recoveryQuestion = preferences.getString(SupportClass.SELECTED_RECOVERY_SECURITY_QUESTION, "");
                            String recoveryAnswer = preferences.getString(SupportClass.SELECTED_RECOVERY_SECURITY_ANSWER, "");
                            Log.e("TAG", "Your Recovery Question : " + recoveryQuestion + ", Answer : " + recoveryAnswer);
                            tryCount = 0;
                            if (recoveryQuestion.equals("") && recoveryAnswer.equals("")) {
                                if (userAnswer.equals("")) {
                                    patternLockView.setVisibility(View.GONE);
                                    pinLockView.setVisibility(View.GONE);
                                    mDeleteButtonPinLock.setVisibility(View.GONE);
                                    et_input_answer.setVisibility(View.VISIBLE);
                                    lin_security_question_answer.setVisibility(View.VISIBLE);

                                } else {
                                    patternLockView.setVisibility(View.GONE);
                                    pinLockView.setVisibility(View.GONE);
                                    mDeleteButtonPinLock.setVisibility(View.GONE);
                                    lin_security_question_answer.setVisibility(View.VISIBLE);
                                }

                            } else {
                                startMainDrawerActivity();
                                finish();
                            }
                        } else {
                            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                            mlayoutDots.startAnimation(shake);
                            mTextEnterPin.setText(getString(R.string.txt_wrong_pass));
                            setDefault();
                            tryCount++;
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
                            if (tryCount % 3 == 0 && preferences.getBoolean(SupportClass.IS_ENABLE_SECRET_SNAP, false))
                                CameraUtil.takeSnapShot(this, viewFinder);
                        }
                    }
                }
            }
        }
    }

    public void setDefault() {
        mDialedText.setText("");
        mPasscode1.setText("");
        mPasscode2.setText("");
        mPasscode3.setText("");
        mPasscode4.setText("");
    }

    public void changePasscodeImage() {
        switch (mDialedText.getText().toString().length()) {
            case 0:
                mPasscode1.setText("");
                break;
            case 1:
                mPasscode1.setText("*");
                mPasscode2.setText("");
                break;

            case 2:
                mPasscode2.setText("*");
                mPasscode3.setText("");
                break;

            case 3:
                mPasscode3.setText("*");
                mPasscode4.setText("");
                break;

            case 4:
                mPasscode4.setText("*");
                break;

        }
    }

    private void performFirstTime() {

        if (isRepeatScreen) {
            if (pinCode.equals(mDialedText.getText().toString())) {
                SupportClass.saveString(pinCode, SupportClass.PIN_LOCK);
                patternLockView.setVisibility(View.GONE);
                pinLockView.setVisibility(View.GONE);
                mDeleteButtonPinLock.setVisibility(View.GONE);
                lin_security_question_answer.setVisibility(View.VISIBLE);

                if (userAnswer.equals("")) {
                    et_input_answer.setVisibility(View.VISIBLE);
                }

                Drawable unwrappedDrawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.icon_step_completed);
                unwrappedDrawable.setColorFilter(new
                        PorterDuffColorFilter(getResources().getColor(R.color.option_text_selected_color), PorterDuff.Mode.SRC_IN));
                step2.setImageDrawable(unwrappedDrawable);
//                step2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_step_completed));
                step23.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_step_nav_completed));

            } else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                mTextEnterPin.startAnimation(shake);
                mTextEnterPin.setText(getString(R.string.txt_wrong_pass));
                shake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mDialedText.setText("");
                        mPasscode1.setText("");
                        mPasscode2.setText("");
                        mPasscode3.setText("");
                        mPasscode4.setText("");
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

        } else {
            Drawable unwrappedDrawable = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.icon_step_completed);
            unwrappedDrawable.setColorFilter(new
                    PorterDuffColorFilter(getResources().getColor(R.color.option_text_selected_color), PorterDuff.Mode.SRC_IN));
            step1.setImageDrawable(unwrappedDrawable);
//            step1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_step_completed));

            step12.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_step_nav_completed));

            pinCode = mDialedText.getText().toString();
            mDialedText.setText("");
            mPasscode1.setText("");
            mPasscode2.setText("");
            mPasscode3.setText("");
            mPasscode4.setText("");

            mTextEnterPin.setText(R.string.reenter_code);
            isRepeatScreen = true;
        }

    }

    private static class MainThreadExecutor implements Executor {
        private final Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable r) {
            handler.post(r);
        }
    }
}
