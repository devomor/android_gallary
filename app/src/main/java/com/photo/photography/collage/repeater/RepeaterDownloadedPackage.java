package com.photo.photography.collage.repeater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.photo.photography.R;
import com.photo.photography.collage.callback.CallbackOnDownloadedPackageClick;
import com.photo.photography.collage.model.DataItemPackageInfo;
import com.photo.photography.collage.util.PhotoUtil;

import java.util.List;

/**
 * Created by Sira on 3/7/2018.
 */
public class RepeaterDownloadedPackage extends BaseSwipeAdapter {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<DataItemPackageInfo> mItems;
    private CallbackOnDownloadedPackageClick mListener;

    public RepeaterDownloadedPackage(Context context, List<DataItemPackageInfo> objects, CallbackOnDownloadedPackageClick listener) {
        mContext = context;
        mItems = objects;
        mInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    public void remove(DataItemPackageInfo item) {
        mItems.remove(item);
        notifyDataSetChanged();
        closeAllItems();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View convertView = mInflater.inflate(R.layout.row_downloaded_packages, null);
        SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewWithTag("Bottom2"));

        ViewHolder holder = new ViewHolder();
        holder.deleteImageView = convertView.findViewById(R.id.deleteImage);
        holder.itemLayout = convertView.findViewById(R.id.itemLayout);
        holder.thumbnailView = (ImageView) convertView.findViewById(R.id.thumbnailView);
        holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
        holder.timeView = (TextView) convertView.findViewById(R.id.timeView);
        convertView.setTag(holder);

        return convertView;
    }

    public void setListener(CallbackOnDownloadedPackageClick listener) {
        mListener = listener;
    }

    @Override
    public void fillValues(final int position, final View convertView) {
        final DataItemPackageInfo info = mItems.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDeleteButtonClick(position, info);
                }
            }
        });

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position, info);
                }
            }
        });

        holder.titleView.setText(info.getTitle());
        holder.timeView.setText(info.getLastModified());
        PhotoUtil.loadImageWithGlide(mContext, holder.thumbnailView, info.getThumbnail());
    }

    private class ViewHolder {
        View deleteImageView;
        View itemLayout;
        ImageView thumbnailView;
        TextView titleView;
        TextView timeView;
    }
}
