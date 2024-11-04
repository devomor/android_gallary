package com.photo.photography.duplicatephotos.repeater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.photo.photography.R;
import com.photo.photography.duplicatephotos.extras.ImagesItem;
import com.photo.photography.duplicatephotos.extras.IndividualGroups;
import com.photo.photography.duplicatephotos.common.GlobalVarsAndFunction;
import com.photo.photography.duplicatephotos.customview.CustomMyGridView;
import com.photo.photography.duplicatephotos.callback.CallbackMarked;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class RepeaterIndividualGroup extends RecyclerView.Adapter<RepeaterIndividualGroup.IGViewHolder> {
    DisplayImageOptions displayImageOptions;
    List<IndividualGroups> groupOfDupes;
    Activity iGAActivity;
    Context iGAContext;
    ImageLoader imageLoader;
    CallbackMarked markedListener;

    public RepeaterIndividualGroup(Context context, Activity activity, List<IndividualGroups> list, CallbackMarked markedListener2, ImageLoader imageLoader2, DisplayImageOptions displayImageOptions2) {
        this.iGAContext = context;
        this.iGAActivity = activity;
        this.groupOfDupes = list;
        this.markedListener = markedListener2;
        this.imageLoader = imageLoader2;
        this.displayImageOptions = displayImageOptions2;
    }

    @Override
    public IGViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new IGViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_group_elements, viewGroup, false));
    }

    public void onBindViewHolder(final IGViewHolder iGViewHolder, final int position) {
        IndividualGroups individualGroup = this.groupOfDupes.get(position);
        TextView textView = iGViewHolder.textView;
        textView.setText(this.iGAContext.getString(R.string.set) + " " + individualGroup.getGroupTag());
        iGViewHolder.checkBox.setChecked(individualGroup.isGroupSetCheckBox());
        iGViewHolder.myGridView.setAdapter((ListAdapter) new RepeaterGridView(position, this.groupOfDupes, this.markedListener, this.iGAContext,
                this.iGAActivity, individualGroup.getIndividualGrpOfDupes(), iGViewHolder.checkBox, this.imageLoader, this.displayImageOptions));
        iGViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, final boolean z) {
                compoundButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        IndividualGroups individualGroup = groupOfDupes.get(position);
                        groupOfDupes.get(position).setGroupSetCheckBox(z);
                        RepeaterGridView gridViewAdapter = new RepeaterGridView(position, groupOfDupes, markedListener, iGAContext, iGAActivity,
                                setCheckBox(individualGroup.getIndividualGrpOfDupes(), z, individualGroup.getGroupTag()),
                                iGViewHolder.checkBox, imageLoader, displayImageOptions);
                        iGViewHolder.myGridView.setAdapter((ListAdapter) gridViewAdapter);
                        gridViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return this.groupOfDupes.size();
        } catch (NullPointerException e) {
            e.getCause();
            return 0;
        }
    }

    private List<ImagesItem> setCheckBox(List<ImagesItem> list, boolean z, int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < list.size(); i2++) {
            ImagesItem imageItem = list.get(i2);
            if (i2 != 0) {
                if (z) {
                    if (!imageItem.isImageCheckBox()) {
                        if (GlobalVarsAndFunction.getTabSelected() != 0) {
                            GlobalVarsAndFunction.file_To_Be_Deleted_Similar.add(imageItem);
                            GlobalVarsAndFunction.addSizeSimilar(imageItem.getSizeOfTheFile());
                            this.markedListener.updateMarkedSimilar();
                        } else {
                            GlobalVarsAndFunction.file_To_Be_Deleted_Exact.add(imageItem);
                            GlobalVarsAndFunction.addSizeExact(imageItem.getSizeOfTheFile());
                            this.markedListener.updateMarkedExact();
                        }
                    }
                } else if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Similar.remove(imageItem);
                    GlobalVarsAndFunction.subSizeSimilar(imageItem.getSizeOfTheFile());
                    this.markedListener.updateMarkedSimilar();
                } else {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Exact.remove(imageItem);
                    GlobalVarsAndFunction.subSizeExact(imageItem.getSizeOfTheFile());
                    this.markedListener.updateMarkedExact();
                }
                imageItem.setImageCheckBox(z);
                arrayList.add(imageItem);
            } else if (imageItem.isImageCheckBox()) {
                if (GlobalVarsAndFunction.getTabSelected() != 0) {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Similar.remove(imageItem);
                } else {
                    GlobalVarsAndFunction.file_To_Be_Deleted_Exact.remove(imageItem);
                }
                imageItem.setImageCheckBox(false);
                arrayList.add(imageItem);
            } else {
                imageItem.setImageCheckBox(false);
                arrayList.add(imageItem);
            }
        }
        return arrayList;
    }

    public class IGViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        CustomMyGridView myGridView;
        TextView textView;

        public IGViewHolder(View view) {
            super(view);
            this.textView = (TextView) view.findViewById(R.id.tv_grp_name);
            this.checkBox = (CheckBox) view.findViewById(R.id.cb_grp_checkbox);
            this.myGridView = (CustomMyGridView) view.findViewById(R.id.gv_images);
        }
    }
}
