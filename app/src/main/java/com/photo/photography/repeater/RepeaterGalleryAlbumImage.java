package com.photo.photography.repeater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.photo.photography.R;
import com.photo.photography.util.utils.PhotoUtil;

import java.util.List;

public class RepeaterGalleryAlbumImage extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private boolean mImageFitCenter = false;

    public RepeaterGalleryAlbumImage(Context context, List<String> objects) {
        super(context, R.layout.row_gallery_photo, objects);
        mInflater = LayoutInflater.from(context);
    }

    public void setImageFitCenter(boolean imageFitCenter) {
        mImageFitCenter = imageFitCenter;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.row_gallery_photo, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            if (mImageFitCenter) {
                holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoUtil.loadImageWithGlide(getContext(), holder.imageView, getItem(position));
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
