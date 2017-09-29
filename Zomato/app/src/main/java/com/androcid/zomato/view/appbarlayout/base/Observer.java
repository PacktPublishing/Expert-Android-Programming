

package com.androcid.zomato.view.appbarlayout.base;

import android.view.View;

/**
 * Created by Androcid on 2/3/16.
 */
public interface Observer {

  View getView();

  void setOnScrollListener(OnScrollListener onScrollListener);
}
