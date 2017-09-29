package com.androcid.zomato.util;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * Created by Androcid on 03-12-2015.
 */
public class CustomTypefaceSpan extends TypefaceSpan {

    private final Typeface newType;
    int mColor = 1;

    public CustomTypefaceSpan(String family, Typeface type) {
        super(family);
        newType = type;
    }

    public CustomTypefaceSpan(String family, Typeface type, int color) {
        super(family);
        newType = type;
        mColor = color;
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (mColor == 0)
            ds.setColor(0);
        applyCustomTypeFace(ds, newType);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType);
    }
}
