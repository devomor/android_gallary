package com.photo.photography.secure_vault;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.views.Lock9Views;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActChangeLockStyle extends ActBase {

    @BindView(R.id.dial_text)
    TextView mDialedText;

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

    @BindView(R.id.enter_pincode)
    TextView mTextEnterPin;

    @BindView(R.id.patternLockMsg)
    TextView patternLockMsg;

    @BindView(R.id.lock_9_view)
    Lock9Views lock9View;

    @BindView(R.id.pad_x)
    ImageView mDeleteButtonPinLock;

    @BindView(R.id.patternLockView)
    RelativeLayout patternLockView;

    @BindView(R.id.pinLockView)
    RelativeLayout pinLockView;

    boolean isRepeatScreen;
    Intent intent;
    Bitmap selectDrawable, unSelectDrawable;
    boolean isVibrator = false, isSound = false;
    boolean confirmPin = false,
            confirmPattern = false,
            setupFirstTime = true,
            setupPatternLock = false,
            setupPinLock = false;
    String firstTimeInputedPin = "";
    private Vibrator vibrator;
    private SoundPool soundPool;
    private int musicId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_change_lock_style);

       // hideSystemBars();

        ButterKnife.bind(this);
        pinLockView.setVisibility(View.GONE);
        mDeleteButtonPinLock.setVisibility(View.GONE);
        patternLockView.setVisibility(View.GONE);
        mForgotPasscode.setVisibility(View.GONE);
        mForgotPasscodePattern.setVisibility(View.GONE);

        //SET PIN LOCK THEME
//        changeTheme(SupportedClass.getBoolean(SupportedClass.THEME_CHANGE));
        selectDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.icon_o);
        unSelectDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.icon_o1);

        boolean isPinlock = SupportClass.getSettingsInfo(SupportClass.IS_PIN_LOCK);
        if (isPinlock) {
            setupPinToPatternLock();
        } else {
            setupPatternToPinLock();
        }

        lock9View.setCallBack(new Lock9Views.CallBack() {
            @Override
            public void onFinish(String password) {

                Log.e("TAG", "Your pattern Pass : " + password);

                if (password.length() < 4) {
                    patternLockMsg.setText(getResources().getString(R.string.txt_at_least_four_dot));
                } else {
                    Log.e("TAG", "old pass : " + firstTimeInputedPin + " new pass : " + password);
                    if (confirmPattern) {
                        String passss = getPinCode();
                        if (passss.equals(password)) {
                            mTextEnterPin.setText(R.string.enter_new_code);
                            setupPinLock();

                        } else {
                            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                            patternLockMsg.startAnimation(shake);
                            patternLockMsg.setText(getResources().getString(R.string.txt_wrong_pattern));
                        }

                    } else if (setupPatternLock && setupFirstTime) {
                        firstTimeInputedPin = password;
                        setupFirstTime = false;
                        patternLockMsg.setText(getResources().getString(R.string.txt_confirm_pattern));

                    } else if (setupPatternLock) {
                        if (firstTimeInputedPin.equals(password)) {
                            setResult(RESULT_OK);
                            setPinCode(password);
                            SupportClass.setSettingsInfo(SupportClass.IS_PIN_LOCK, false);
                            finish();
                        } else {
                            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                            patternLockMsg.startAnimation(shake);
                            patternLockMsg.setText(getResources().getString(R.string.txt_pattern_cant_match));
                        }
                    } else {
                        Log.e("TAG", "Blank Else Call");
                    }
                }
            }
        });

        patternLockMsg.setText(R.string.txt_confirm_old_pattern);
        mTextEnterPin.setText(R.string.enter_old_code);
        mPasscode1.setText("");
        mPasscode2.setText("");
        mPasscode3.setText("");
        mPasscode4.setText("");

        saveSelectedArrayList(SupportClass.getArraylistSavedApps(SupportClass.SAVE_APPLICATION_PREF));
        Log.e("TAG", "ChangeLockStyleActivity Vibrate Enabled " + SupportClass.getSettingsInfo(SupportClass.IS_VIBRATE_ENABLE));
        if (SupportClass.getSettingsInfo(SupportClass.IS_VIBRATE_ENABLE)) {
            isVibrator = true;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }
        Log.e("TAG", "ChangeLockStyleActivity Sound Enabled " + SupportClass.getSettingsInfo(SupportClass.IS_SOUND_ENABLE));
        if (SupportClass.getSettingsInfo(SupportClass.IS_SOUND_ENABLE)) {
            isSound = true;
            soundPool = SupportClass.buildSoundPool();
            musicId = soundPool.load(ActChangeLockStyle.this, R.raw.beep_tune, 1);
        }
    }

    private void confirmPinLock() {
        confirmPin = true;
        pinLockView.setVisibility(View.VISIBLE);
        mDeleteButtonPinLock.setVisibility(View.VISIBLE);
        patternLockView.setVisibility(View.GONE);
    }

    private void setupPatternLock() {
        firstTimeInputedPin = "";
        setupPatternLock = true;
        pinLockView.setVisibility(View.GONE);
        mDeleteButtonPinLock.setVisibility(View.GONE);
        patternLockView.setVisibility(View.VISIBLE);
    }

    private void setupPinToPatternLock() {
        confirmPinLock();
    }

    private void confirmPatternLock() {
        confirmPattern = true;
        pinLockView.setVisibility(View.GONE);
        mDeleteButtonPinLock.setVisibility(View.GONE);
        patternLockView.setVisibility(View.VISIBLE);
    }

    private void setupPinLock() {
        firstTimeInputedPin = "";
        setupPinLock = true;
        setupFirstTime = true;
        pinLockView.setVisibility(View.VISIBLE);
        mDeleteButtonPinLock.setVisibility(View.VISIBLE);
        patternLockView.setVisibility(View.GONE);
    }

    private void setupPatternToPinLock() {
        confirmPatternLock();
    }

    public void saveSelectedArrayList(ArrayList<String> savedArrayList) {
        SupportClass.saveArraylistSavedApps(savedArrayList, SupportClass.SAVE_APPLICATION_PREF);
        Log.d("savedArrayList", savedArrayList.size() + "");
    }

    public String getPinCode() {
        return SupportClass.getString(SupportClass.PIN_LOCK);
    }

    public void setPinCode(String value) {
        SupportClass.saveString(value, SupportClass.PIN_LOCK);
    }

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

                    if (setupFirstTime && mDialedText.getText().toString().equals("")) {
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
                    if (confirmPin) {
                        String oldPassword = getPinCode();
                        if (oldPassword.equals(mDialedText.getText().toString())) {
                            Log.e("TAG", "SetupPatternLock() called");
                            patternLockMsg.setText(R.string.txt_draw_new_pattern);
                            setupPatternLock();
                        } else {
                            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                            mTextEnterPin.startAnimation(shake);
                            mTextEnterPin.setText(getResources().getString(R.string.txt_wrong_pass));
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
                    } else if (setupPinLock) {
                        Log.e("TAG", "old pass : " + firstTimeInputedPin + " new pass : " + mDialedText.getText());
                        if (setupFirstTime) {
                            firstTimeInputedPin = mDialedText.getText().toString();
                            mDialedText.setText("");
                            mPasscode1.setText("");
                            mPasscode2.setText("");
                            mPasscode3.setText("");
                            mPasscode4.setText("");

                            mTextEnterPin.setText(R.string.reenter_code);
                            setupFirstTime = false;
                        } else {
                            if (firstTimeInputedPin.equals(mDialedText.getText().toString())) {
                                setResult(RESULT_OK);
                                setPinCode(firstTimeInputedPin);
                                SupportClass.setSettingsInfo(SupportClass.IS_PIN_LOCK, true);
                                finish();
                            } else {
                                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                                mTextEnterPin.startAnimation(shake);
                                mTextEnterPin.setText(getResources().getString(R.string.txt_wrong_pass));
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
                        }
                    }
                }
            }
        }
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
}
