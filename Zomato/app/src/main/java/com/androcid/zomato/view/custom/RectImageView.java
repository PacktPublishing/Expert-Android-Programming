package com.androcid.zomato.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RectImageView extends ImageView {
    public RectImageView(Context context) {
        super(context);
    }

    public RectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = (int) (getMeasuredWidth());
        setMeasuredDimension(w, w / 2);
    }
}
