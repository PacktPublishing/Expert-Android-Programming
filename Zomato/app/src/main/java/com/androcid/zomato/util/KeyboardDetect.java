package com.androcid.zomato.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * Created by Androcid on 20-01-2017.
 */

public class KeyboardDetect {
    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private View mChildOfContent;
    private int usableHeightPrevious;
   // private FrameLayout.LayoutParams frameLayoutParams;

    public KeyboardDetect(Activity activity) {
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
       // frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible
              //  frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
                if(listener!=null){
                    listener.onSoftKeyboardShown(true);
                }
            } else {
                // keyboard probably just became hidden
              //  frameLayoutParams.height = usableHeightSansKeyboard;
                if(listener!=null){
                    listener.onSoftKeyboardShown(false);
                }
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    private KeyboardListener listener;
    public void setKeyboardListener(KeyboardListener listener) {
        this.listener = listener;
    }
    public interface KeyboardListener {
        public void onSoftKeyboardShown(boolean b);
    }

}
