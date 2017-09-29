

package com.androcid.zomato.view.appbarlayout.base;

import android.view.View;

import com.androcid.zomato.view.appbarlayout.SmoothAppBarLayout;


/**
 * Created by Androcid on 2/11/16.
 */
public interface ObservableFragment {

  /**
   * Get view that handles scrollEvent
   *
   * @return NestedScrollView or RecyclerView
   */
  View getScrollTarget();


  /**
   * Listener for offset changed event
   *
   * @param target         The view that is currently selected by ViewPager
   * @param verticalOffset Current vertical offset of SmoothAppBarLayout. It has the same value with smoothAppBarLayout.getCurrentOffset()
   * @return False if scroll hasn't been initiated or is waiting for async loading. If false, it will stop propagating ViewPager
   */
  boolean onOffsetChanged(SmoothAppBarLayout smoothAppBarLayout, View target, int verticalOffset);
}
