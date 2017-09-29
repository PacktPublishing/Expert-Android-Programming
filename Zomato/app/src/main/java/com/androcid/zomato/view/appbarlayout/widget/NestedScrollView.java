

package com.androcid.zomato.view.appbarlayout.widget;

import android.content.Context;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Androcid on 10/1/15.
 */
public class NestedScrollView extends android.support.v4.widget.NestedScrollView {

  protected List<OnScrollChangeListener> mOnScrollListeners;

  public NestedScrollView(Context context) {
    super(context);
  }

  public NestedScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public NestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    super.onScrollChanged(l, t, oldl, oldt);
    if (mOnScrollListeners != null) {
      int i = 0;
      for (int n = mOnScrollListeners.size(); i < n; i++) {
        mOnScrollListeners.get(i).onScrollChange(this, l, t, oldl, oldt);
      }
    }
  }

  public void addOnScrollListener(OnScrollChangeListener onScrollListener) {
    if (mOnScrollListeners == null) {
      mOnScrollListeners = new ArrayList<>();
    }
    mOnScrollListeners.add(onScrollListener);
  }
}
