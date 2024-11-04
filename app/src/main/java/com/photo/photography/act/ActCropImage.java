package com.photo.photography.act;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.photo.photography.R;
import com.photo.photography.frag.FragCropImage;
import com.photo.photography.util.utilsEdit.FontUtil;


public class ActCropImage extends AppCompatActivity {

    private static final String TAG = ActCropImage.class.getSimpleName();
    public Uri currentImgUri;

    public static Intent createIntent(Activity activity, Uri crpUri) {
        Intent intent = new Intent(activity, ActCropImage.class);
        intent.putExtra("cropUri", crpUri.toString());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_crop_image);

        /*((TextView) findViewById(R.id.toolbar_title)).setText(getString(R.string.txt_crop));
        findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            toolbar.setTitle(getString(R.string.txt_crop));
//            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        }

        Intent intent = getIntent();
        currentImgUri = Uri.parse(intent.getStringExtra("cropUri"));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, FragCropImage.newInstance()).commit();
        }

        // apply custom font
        FontUtil.setFont(findViewById(R.id.root_layout));
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void cancelCropping() {
        if (isFinishing()) return;
        setResult(RESULT_CANCELED);
        finish();
    }

    public void startResultActivity(byte[] byteArr) {
        if (isFinishing()) return;
        // Start ResultActivity
        Log.e("TECHTAG", "Set Back with Bitmap");
        Intent intent = new Intent();
        intent.putExtra("croppedBitmapByteArr", byteArr);
        setResult(RESULT_OK, intent);
        finish();
    }
}
