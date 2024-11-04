package com.photo.photography.secure_vault;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.photo.photography.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by  on 10/4/2016.
 */
public class ActSecretSnapView extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.delete_file)
    ImageView delete_file;

    @BindView(R.id.iv_itruder_img)
    ImageView iv_itruder_img;

    String imgPath, imgCapMilli;
    private Toolbar toolbar;

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_secret_snap_view);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable nav = ContextCompat.getDrawable(this, R.drawable.e_back);
        if (nav != null) {
            nav.setTint(ContextCompat.getColor(this, R.color.black));
            toolbar.setNavigationIcon(nav);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                supportFinishAfterTransition();
//                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(getString(R.string.txt_view_secret_snaps));

        imgPath = getIntent().getStringExtra("imgPath");
        imgCapMilli = getIntent().getStringExtra("imgCapMilli");

        delete_file.setVisibility(View.VISIBLE);
        delete_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(imgPath);
                if (file.exists())
                    file.delete();
                setResult(RESULT_OK);
                finish();
                Toast.makeText(ActSecretSnapView.this, "Delete File Successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        String imgDate = getDate(Long.parseLong(imgCapMilli), "dd MMM yyyy hh:mm:ss aa");
        tv_date.setText("on " + imgDate);
        File file = new File(imgPath);
        Bitmap bmp = null;
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bmp = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            iv_itruder_img.setImageBitmap(bmp);
        }
    }
}
