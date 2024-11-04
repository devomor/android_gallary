package com.photo.photography;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

public class LinearLayoutManagerWrapp extends GridLayoutManager {

  public LinearLayoutManagerWrapp(Context context , int spanCount) {
    super(context,spanCount);
  }

  /*public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout,int spanCount) {
    super(context, orientation, spanCount);
  }

  public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }*/

  @Override
  public boolean supportsPredictiveItemAnimations() {
    return false;
  }
}