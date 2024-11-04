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
import android.widget.Toast;

import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.util.utilsEdit.SupportClass;
import com.photo.photography.secure_vault.views.Lock9Views;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActChangePasswordLock extends ActBase {

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
    @BindView(R.id.patternLockMsg)
    TextView patternLockMsg;
    @BindView(R.id.pad_x)
    ImageView mDeleteButtonPinLock;
    @BindView(R.id.lock_9_view)
    Lock9Views lock9View;
    @BindView(R.id.patternLockView)
    RelativeLayout patternLockView;
    @BindView(R.id.pinLockView)
    RelativeLayout pinLockView;

    String oldPatternValue = "";
    String pinCode = "";
    boolean isFirstTime, isSecondTime = false;
    boolean isRepeatScreen;
    boolean isVibrator = false, isSound = false;
    Intent intent;
    boolean isServiceEnable = false;
    @BindView(R.id.enter_pincode)
    TextView mTextEnterPin;
    Bitmap selectDrawable, unSelectDrawable;
    private Vibrator vibrator;
    private SoundPool soundPool;
    private int musicId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lock_pads);
       // hideSystemBars();

        ButterKnife.bind(this);
        mForgotPasscode.setVisibility(View.GONE);
        mForgotPasscodePattern.setVisibility(View.GONE);


        boolean currentLockStyle = SupportClass.getSettingsInfo(SupportClass.IS_PIN_LOCK);
        if (currentLockStyle) {
            initPinLockData();
        } else {
            initPatternData();
            patternLockMsg.setText(getString(R.string.txt_confirm_old_pattern));
        }

        lock9View.setCallBack(new Lock9Views.CallBack() {
            @Override
            public void onFinish(String password) {
                String passss = getPinCode();
                Log.e("TAG", "Your pattern Pass : " + password);
                if (isFirstTime) {
                    if (passss.equals(password)) {
                        isFirstTime = false;
                        isSecondTime = true;
                        patternLockMsg.setText(getString(R.string.txt_draw_new_pattern));
                    } else {
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                        patternLockMsg.startAnimation(shake);
                        patternLockMsg.setText(getString(R.string.txt_wrong_pattern));
                    }
                } else if (isSecondTime) {
                    isSecondTime = false;
                    isRepeatScreen = true;
                    oldPatternValue = password;
                    patternLockMsg.setText(getString(R.string.txt_confirm_new_pattern));
                } else if (isRepeatScreen) {
                    if (oldPatternValue.equals(password)) {
                        setPinCode(password);
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_change_pattern_successfully), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_shake);
                        patternLockMsg.startAnimation(shake);
                        patternLockMsg.setText(getString(R.string.txt_pattern_confirmation_error));
                    }
                }
            }
        });

        mTextEnterPin.setText(R.string.enter_old_code);
        mPasscode1.setText("");
        mPasscode2.setText("");
        mPasscode3.setText("");
        mPasscode4.setText("");

        isFirstTime = true;
        isSecondTime = false;
        isRepeatScreen = false;

        Log.e("TAG", "ChangePasswordLockActivity Vibrate Enabled " + SupportClass.getSettingsInfo(SupportClass.IS_VIBRATE_ENABLE));
        if (SupportClass.getSettingsInfo(SupportClass.IS_VIBRATE_ENABLE)) {
            isVibrator = true;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }
        Log.e("TAG", "ChangePasswordLockActivity Sound Enabled " + SupportClass.getSettingsInfo(SupportClass.IS_SOUND_ENABLE));
        if (SupportClass.getSettingsInfo(SupportClass.IS_SOUND_ENABLE)) {
            isSound = true;
            soundPool = SupportClass.buildSoundPool();
            musicId = soundPool.load(ActChangePasswordLock.this, R.raw.beep_tune, 1);
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

        selectDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.icon_o);
        unSelectDrawable = BitmapFactory.decodeResource(getResources(), R.drawable.icon_o1);
        mPasscode1.setText("");
        mPasscode2.setText("");
        mPasscode3.setText("");
        mPasscode4.setText("");
    }

    public String getPinCode() {
        return SupportClass.getString(SupportClass.PIN_LOCK);
    }

    public void setPinCode(String value) {
        SupportClass.saveString(value, SupportClass.PIN_LOCK);
    }

    @OnClick({R.id.pad_0, R.id.pad_1, R.id.pad_2, R.id.pad_3, R.id.pad_4, R.id.pad_5, R.id.pad_6, R.id.pad_7, R.id.pad_8, R.id.pad_9, R.id.pad_x})
    public void dialPadClicked(View v) {
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
                    mTextEnterPin.setText(R.string.enter_old_code);
                    isRepeatScreen = false;
                    isSecondTime = false;
                } else if (isSecondTime && mDialedText.getText().toString().equals("")) {
                    mTextEnterPin.setText(R.string.enter_new_code);
                    isRepeatScreen = false;
                    isFirstTime = false;
                } else if (isSecondTime && isRepeatScreen) {
                    mTextEnterPin.setText(R.string.enter_new_code);
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
                    performFirstTime();
                } else if (isSecondTime == true) {
                    performSecondTime();
                } else if (isRepeatScreen == true) {
                    if (pinCode.equals(mDialedText.getText().toString())) {
                        setPinCode(pinCode);
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_change_password_successfully), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        clearPinLock();
                        Toast.makeText(getApplicationContext(), getString(R.string.txt_pin_confirmation_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.txt_try_again), Toast.LENGTH_SHORT).show();
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

    private void performFirstTime() {

        String storedpass = getPinCode();
        String inputedPass = mDialedText.getText().toString();

        if (storedpass.equals(inputedPass)) {
            Log.e("TAG", "Password Match");
            mTextEnterPin.setText(R.string.enter_new_code);

            clearPinLock();
            isFirstTime = false;
            isSecondTime = true;
        } else {
            clearPinLock();
            Log.e("TAG", "Does not match old Password");
            Toast.makeText(ActChangePasswordLock.this, getString(R.string.code_not_match), Toast.LENGTH_SHORT).show();
        }

    }

    public void clearPinLock() {
        mPasscode1.setText("");
        mPasscode2.setText("");
        mPasscode3.setText("");
        mPasscode4.setText("");
        mDialedText.setText("");

    }

    private void performSecondTime() {

        pinCode = mDialedText.getText().toString();
        clearPinLock();
        mTextEnterPin.setText(R.string.reenter_new_code);
        isSecondTime = false;
        isRepeatScreen = true;
    }
}
