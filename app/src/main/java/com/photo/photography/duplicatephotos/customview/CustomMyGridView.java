package com.photo.photography.duplicatephotos.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomMyGridView extends GridView {
    public CustomMyGridView(Context context) {
        super(context);
    }

    public CustomMyGridView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomMyGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void onMeasure(int i, int i2) {
        if (getLayoutParams().height == -2) {
            i2 = MeasureSpec.makeMeasureSpec(536870911, MeasureSpec.AT_MOST);
        }
        super.onMeasure(i, i2);
    }
}
