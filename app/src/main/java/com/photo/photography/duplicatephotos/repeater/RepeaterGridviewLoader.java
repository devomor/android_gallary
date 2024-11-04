package com.photo.photography.duplicatephotos.repeater;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;

public class RepeaterGridviewLoader extends AsyncTask<ImagesItem, Void, String> {
    private final WeakReference<CheckBox> checkBoxReference;
    private final WeakReference<ImageView> imageViewReference;
    Boolean checkboxstatus;
    ImageLoader imageLoader;
    Context imageloadercontext;
    DisplayImageOptions options;

    public RepeaterGridviewLoader(Context context, ImageView imageView, CheckBox checkBox, ImageLoader imageLoader2, DisplayImageOptions displayImageOptions) {
        this.imageViewReference = new WeakReference<>(imageView);
        this.checkBoxReference = new WeakReference<>(checkBox);
        this.imageloadercontext = context;
        this.imageLoader = imageLoader2;
        this.options = displayImageOptions;
    }

    public String doInBackground(ImagesItem... imageItemArr) {
        String image = imageItemArr[0].getImage();
        this.checkboxstatus = imageItemArr[0].isImageCheckBox();
        return image;
    }

    public void onPostExecute(String str) {
        super.onPostExecute(str);
        WeakReference<ImageView> weakReference = this.imageViewReference;
        if (weakReference != null) {
            ImageView imageView = weakReference.get();
            CheckBox checkBox = this.checkBoxReference.get();
            if (imageView == null) {
                return;
            }
            if (str != null) {
                ImageLoader imageLoader2 = this.imageLoader;
                imageLoader2.displayImage("file:///" + Uri.decode(str), imageView, this.options);
                checkBox.setChecked(this.checkboxstatus.booleanValue());
                return;
            }
            imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(R.drawable.icon_rsz_empty_photo));
        }
    }
}
