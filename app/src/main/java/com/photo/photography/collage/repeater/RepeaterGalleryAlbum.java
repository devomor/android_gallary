package com.photo.photography.collage.repeater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.photo.photography.R;
import com.photo.photography.collage.model.DataGalleryAlbum;
import com.photo.photography.collage.util.PhotoUtil;
import com.photo.photography.view.SquaresImageView;

import java.util.List;

/**
 * Created by Sira on 3/26/2018.
 */
public class RepeaterGalleryAlbum extends ArrayAdapter<DataGalleryAlbum> {
    private final LayoutInflater mInflater;
    private final OnGalleryAlbumClickListener mListener;

    public RepeaterGalleryAlbum(Context context, List<DataGalleryAlbum> objects, OnGalleryAlbumClickListener listener) {
        super(context, R.layout.row_gallery_album, objects);
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_gallery_album, parent, false);
            holder = new ViewHolder();
            holder.thumbnailView = (SquaresImageView) convertView.findViewById(R.id.thumbnailView);
            holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
            holder.itemCountView = (TextView) convertView.findViewById(R.id.itemCountView);
//            holder.descriptionView = (TextView) convertView.findViewById(R.id.descriptionView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Show data
        final DataGalleryAlbum item = getItem(position);
        if (item != null) {
            if (holder != null) {
                if (item.getImageList().size() > 0) {
                    PhotoUtil.loadImageWithGlide(getContext(), holder.thumbnailView, item.getImageList().get(0));
                } else {
                    holder.thumbnailView.setImageBitmap(null);
                }

                holder.titleView.setText(item.getAlbumName());
                holder.itemCountView.setText(item.getImageList().size() + " items");
//                holder.descriptionView.setText(item.getTakenDate());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onGalleryAlbumClick(item);
                        }
                    }
                });
            } else {
//                FirebaseCrash.report(new Exception("Holder is null, position=" + position));
            }
        } else {
//            FirebaseCrash.report(new Exception("Get Item at position " + position + " is null"));
        }

        return convertView;
    }

    public interface OnGalleryAlbumClickListener {
        void onGalleryAlbumClick(DataGalleryAlbum album);
    }

    private class ViewHolder {
        SquaresImageView thumbnailView;
        TextView titleView;
        TextView itemCountView;
//        TextView descriptionView;
    }
}
