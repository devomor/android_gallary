package com.photo.photography.duplicatephotos.repeater;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.common.CommonUsed;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.callback.CallbackMarked;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class RepeaterGridView extends ArrayAdapter {
    private final Context context;
    private final Activity gVAActivity;
    private final int groupSetPosition;
    private final ImageLoader imageLoader;
    private final LayoutInflater inflater;
    private final CallbackMarked markedListener;
    private final DisplayImageOptions options;
    private final CheckBox parentCheckbox;
    List<IndividualGroups> groupOfDupes;
    private List<ImagesItem> data = new ArrayList();
    private int totalNumberOffilesInSet;

    public RepeaterGridView(int i, List<IndividualGroups> list, CallbackMarked markedListener2, Context context2, Activity activity, List<ImagesItem> list2, CheckBox checkBox, ImageLoader imageLoader2, DisplayImageOptions displayImageOptions) {
        super(context2, 0, list2);
        this.context = context2;
        this.gVAActivity = activity;
        this.groupOfDupes = list;
        this.groupSetPosition = i;
        this.markedListener = markedListener2;
        this.imageLoader = imageLoader2;
        this.options = displayImageOptions;
        this.data = list2;
        this.parentCheckbox = checkBox;
        this.inflater = (LayoutInflater) context2.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public int getCount() {
        this.totalNumberOffilesInSet = this.data.size();
        return this.totalNumberOffilesInSet;
    }

    @Override
    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public View getView(final int pos, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        ImagesItem imageItem = this.data.get(pos);
        if (view == null) {
            view = this.inflater.inflate(R.layout.row_group_duplicate_images, viewGroup, false);
            viewHolder = new ViewHolder(view);
            viewHolder.tViewSize.setText(GlobalVarsAndFunction.getStringSizeLengthFile(imageItem.getSizeOfTheFile()));
            viewHolder.checkBox.setChecked(imageItem.isImageCheckBox());
            viewHolder.checkBox.setTag(pos);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewHolder.checkBox.performClick();
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, final boolean z) {
                compoundButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        int p = 0;
                        try {
                            ImagesItem imageItem = (ImagesItem) data.get(pos);
                            imageItem.setImageCheckBox(z);
                            int count = getCount();
                            if (imageItem.isImageCheckBox()) {
                                for (int i2 = 0; i2 < getCount(); i2++) {
                                    if (((ImagesItem) data.get(i2)).isImageCheckBox()) {
                                        p++;
                                    }
                                }
                                if (p != count) {
                                    if (GlobalVarsAndFunction.getTabSelected() != 0) {
                                        GlobalVarsAndFunction.file_To_Be_Deleted_Similar.add(imageItem);
                                        GlobalVarsAndFunction.addSizeSimilar(imageItem.getSizeOfTheFile());
                                        markedListener.updateMarkedSimilar();
                                    } else {
                                        GlobalVarsAndFunction.file_To_Be_Deleted_Exact.add(imageItem);
                                        GlobalVarsAndFunction.addSizeExact(imageItem.getSizeOfTheFile());
                                        markedListener.updateMarkedExact();
                                    }
                                    parentCheckbox.setChecked(true);
                                    groupOfDupes.get(groupSetPosition).setGroupSetCheckBox(true);
                                }
                            } else {
                                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                                    GlobalVarsAndFunction.file_To_Be_Deleted_Similar.remove(imageItem);
                                    GlobalVarsAndFunction.subSizeSimilar(imageItem.getSizeOfTheFile());
                                    markedListener.updateMarkedSimilar();
                                } else {
                                    GlobalVarsAndFunction.file_To_Be_Deleted_Exact.remove(imageItem);
                                    GlobalVarsAndFunction.subSizeExact(imageItem.getSizeOfTheFile());
                                    markedListener.updateMarkedExact();
                                }
                            }
                            if (p < count - 1) {
                                parentCheckbox.setChecked(false);
                                groupOfDupes.get(groupSetPosition).setGroupSetCheckBox(false);
                            } else {
                                parentCheckbox.setChecked(true);
                                groupOfDupes.get(groupSetPosition).setGroupSetCheckBox(true);
                            }
                            if (count == p) {
                                CommonUsed.showmsg(context, context.getString(R.string.All_photos_of_the_same_group_cannot_be_selected));
                                imageItem.setImageCheckBox(false);
                                viewHolder.checkBox.setChecked(false);
                                return;
                            }
                            imageItem.setImageCheckBox(z);
                        } catch (Exception e) {
                            Log.e("TAG", "Error : " + e.getMessage());
                        }
                    }
                });
            }
        });
        if (viewHolder.image != null) {
            new RepeaterGridviewLoader(this.context, viewHolder.image, viewHolder.checkBox, this.imageLoader, this.options).execute(imageItem);
        }
        return view;
    }

    class ViewHolder {
        CheckBox checkBox;
        ImageView image;
        TextView tViewSize;

        ViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.img_duplicate_image);
            this.checkBox = (CheckBox) view.findViewById(R.id.individualcheckbox);
            this.tViewSize = (TextView) view.findViewById(R.id.show_size_on_image);
        }
    }
}
