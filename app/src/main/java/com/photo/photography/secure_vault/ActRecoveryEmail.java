package com.photo.photography.secure_vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.photo.photography.MyApp;
import com.photo.photography.R;
import com.photo.photography.act.ActBase;
import com.photo.photography.util.utilsEdit.SupportClass;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 30-07-2016.
 */
public class ActRecoveryEmail extends ActBase {

    @BindView(R.id.spin_security_question)
    Spinner mSecurityQuestion;

    @BindView(R.id.et_input_answer)
    EditText et_input_answer;

    @BindView(R.id.bannerCustomSize)
    LinearLayout mbannerlayout;

    String[] arr_security_question;

    @OnClick(R.id.close_snap_activity)
    public void closeActivity() {
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recovery_email);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        ButterKnife.bind(this);

        arr_security_question = getResources().getStringArray(R.array.security_question);
        ArrayAdapter aa = new ArrayAdapter(ActRecoveryEmail.this, R.layout.spinner_items2, arr_security_question);
        aa.setDropDownViewResource(R.layout.spinner_dropdown_items);
        mSecurityQuestion.setAdapter(aa);

    }

    @OnClick(R.id.btn_submit)
    public void submitClick() {
        CharSequence inputAnswer = et_input_answer.getText().toString().trim();

        if (mSecurityQuestion.getSelectedItemPosition() == 0) {
            Toast.makeText(this, getString(R.string.plese_select_security_question), Toast.LENGTH_SHORT).show();
            mSecurityQuestion.requestFocus();

        } else if (TextUtils.isEmpty(inputAnswer)) {
            et_input_answer.setError(getString(R.string.txt_field_not_empty));
            et_input_answer.requestFocus();

        } else {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.mContext);
            String recoveryQuestion = preferences.getString(SupportClass.SELECTED_RECOVERY_SECURITY_QUESTION, "");
            String recoveryAnswer = preferences.getString(SupportClass.SELECTED_RECOVERY_SECURITY_ANSWER, "");
            Log.e("TAG", "Your Recovery Question : " + recoveryQuestion + ", Answer : " + recoveryAnswer);

            if (recoveryQuestion.equalsIgnoreCase(arr_security_question[mSecurityQuestion.getSelectedItemPosition()]) && inputAnswer.equals(recoveryAnswer)) {
                startActivityForResult(new Intent(getApplicationContext(), ActRecoverSetupLock.class), 11);
            } else {
                Toast.makeText(ActRecoveryEmail.this, getString(R.string.txt_please_select_valid_question_and_answer), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "Recovery Email Activity onActivityResult Called.");
        if (requestCode == 11) {
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }
}
