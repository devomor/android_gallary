package com.photo.photography.secure_vault.repeater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.photo.photography.R;
import com.photo.photography.edit_views.SquareImagesView;
import com.photo.photography.secure_vault.ImageLoader.ImagesLoaderGrid;
import com.photo.photography.secure_vault.models.VaultFile;
import com.photo.photography.secure_vault.utils.VaultFileUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeaterPrivateVaultGrid extends BaseAdapter {
    private final boolean mIsFromVideo;
    LayoutInflater mInflater;
    Context mContext;
    List<VaultFile> mGalleryModelList;
    ImagesLoaderGrid imageLoader;

    public RepeaterPrivateVaultGrid(Context mContext, List<VaultFile> data, boolean isFromVideo) {
        this.mContext = mContext;
        this.mGalleryModelList = data;
        mIsFromVideo = isFromVideo;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        new VaultFileUtil(mContext).getFile("support");
        imageLoader = new ImagesLoaderGrid(mContext);
    }

    @Override
    public int getCount() {
        return mGalleryModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGalleryModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final RowHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_private_valuts, parent, false);
            holder = new RowHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RowHolder) convertView.getTag();
        }

        // set the status according to this Category item
        imageLoader.DisplayImage(mGalleryModelList.get(position).getThumbnail(), holder.imageView);
        if (mIsFromVideo) {
            holder.mVideoPlayIcon.setVisibility(View.VISIBLE);
        } else {
            holder.mVideoPlayIcon.setVisibility(View.GONE);
        }

        holder.checkedView.bringToFront();
        if (mGalleryModelList.get(position).getIsSelected()) {
            holder.checkedView.setVisibility(View.VISIBLE);
            holder.mVideoPlayIcon.setVisibility(mIsFromVideo ? View.GONE : View.GONE);
        } else {
            holder.checkedView.setVisibility(View.GONE);
            holder.mVideoPlayIcon.setVisibility(mIsFromVideo ? View.VISIBLE : View.GONE);
        }

        return convertView;
    }

    public class RowHolder {

        @BindView(R.id.imageViewFromMediaChooserGridItemRowView)
        SquareImagesView imageView;

        @BindView(R.id.checkedViewFromMediaChooserGridItemRowView)
        FrameLayout checkedView;

        @BindView(R.id.ic_video_play)
        ImageView mVideoPlayIcon;

        public RowHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
