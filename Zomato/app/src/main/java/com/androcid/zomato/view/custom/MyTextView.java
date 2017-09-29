package com.androcid.zomato.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androcid.zomato.R;
import com.androcid.zomato.util.MyFont;

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    MyFont myFont;

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        myFont = new MyFont(context);


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewEx, 0, 0);


        myFont.setFont(this);
        /*
        if (fontName!=null) {
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
            setTypeface(myTypeface);
        }*/

        try {
            boolean isHtml = a.getBoolean(R.styleable.TextViewEx_isHtml, true);
            if (isHtml) {
                String text = a.getString(R.styleable.TextViewEx_android_text);
                if (text != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
                    }
                    else {
                        setText(Html.fromHtml(text));
                    }
                    //setText(text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(final CharSequence text, final BufferType type) {
        super.setText(text, type);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
}
