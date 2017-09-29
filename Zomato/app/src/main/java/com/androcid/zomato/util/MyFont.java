package com.androcid.zomato.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Hashtable;

public class MyFont {

	private Context context;
	private Typeface mTypeface;

	public static final int NORMAL = Typeface.NORMAL;
	public static final int ITALIC = Typeface.ITALIC;
	public static final int BOLD = Typeface.BOLD;
	public static final int BOLD_ITALIC = Typeface.BOLD_ITALIC;

	private final String folder = "font/";

	public static final String VERA_SANS = "AvenirNextLTPro-Regular.otf";
	public static final String VERA_SANS_BOLD = "AvenirNextLTPro-Bold.otf";

	public static final String FONT_REGULAR = VERA_SANS;
	public static final String FONT_BOLD = VERA_SANS_BOLD;

	public static final String DEFAULT_FONT = VERA_SANS;

	private String[] fontList =
		{
				VERA_SANS, VERA_SANS_BOLD
		};

	private static Hashtable<String, Typeface> sTypeFaces;
	
	public MyFont(Context context) {
		this.context = context;

		sTypeFaces = new Hashtable<>();
		makeFonts();
	}


	private void makeFonts() {
		AssetManager assetManager = context.getAssets();
		for(String fontType: fontList) {
			Typeface mTypeface = null;
			try {
				mTypeface = Typeface.createFromAsset(assetManager, folder + fontType);
				sTypeFaces.put(fontType, mTypeface);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private Typeface setMyFont(String fontType) {
		mTypeface = sTypeFaces.get(fontType);

		if(mTypeface==null) {
			mTypeface = Typeface.createFromAsset(context.getAssets(), folder + fontType);
			sTypeFaces.put(fontType, mTypeface);
		}
		return mTypeface;
	}
	
	private Typeface setMyFont(TextView textView, String fontType) {
		return setFont(textView, NORMAL, fontType);
	}
	
	
	public void setFont(TextView textView) {
		setFont(textView, NORMAL);
	}
	
	public Typeface setFont(TextView textView, int fontStyle) {
		
		String fontType = DEFAULT_FONT;
		return setFont(textView, fontStyle, fontType);
		
	}
	
	public Typeface setFont(TextView textView, String fontType) {
		return setFont(textView, NORMAL, fontType);
	}
	
	public Typeface setFont(TextView textView, int fontStyle, String fontType) {
		mTypeface = sTypeFaces.get(fontType);

		if(mTypeface==null) {
			mTypeface = Typeface.createFromAsset(context.getAssets(), folder + fontType);
			sTypeFaces.put(fontType, mTypeface);
		}
		if(textView!=null)
			textView.setTypeface(mTypeface, fontStyle);
		return mTypeface;
	}

    public void setAppFont(ViewGroup mContainer) {
        setAppFont(mContainer, setMyFont(DEFAULT_FONT));
    }

	public void setAppFont(ViewGroup mContainer, String mFont) {
		setAppFont(mContainer, setMyFont(mFont));
	}

    public void setAppFont(ViewGroup mContainer, String mFont, int typeface) {
        setAppFont(mContainer, setMyFont(mFont), typeface);
    }

	/**
	 * Recursively sets a {@link Typeface} to all
	 * {@link TextView}s in a {@link ViewGroup}.
	 */
    private void setAppFont(ViewGroup mContainer, Typeface mFont) {
        setAppFont(mContainer, mFont, NORMAL);
    }

	private void setAppFont(ViewGroup mContainer, Typeface mFont, int thickness)
	{
	    if (mContainer == null || mFont == null) return;
	    
	    final int mCount = mContainer.getChildCount();
	    
	    // Loop through all of the children.
	    for (int i = 0; i < mCount; ++i)
	    {
	        final View mChild = mContainer.getChildAt(i);
	        if (mChild instanceof TextView)
	        {
	            // Set the font if it is a TextView.
	            ((TextView) mChild).setTypeface(mFont, thickness);
	        }
	        else if (mChild instanceof ViewGroup)
	        {
	            // Recursively attempt another ViewGroup.
	            setAppFont((ViewGroup) mChild, mFont);
	        }
	    }
	}
	
	public void setBold(TextView textView, String font) {

		setFont(textView, BOLD, font);
		/*
		SpannableString spanString = new SpannableString(text);
		//spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		//spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
		//text.setText(spanString);
		textView.setTypeface(null, Typeface.BOLD);*/
	}
	/*
	private void setBold(TextView textView, String text) {
		SpannableString spanString = new SpannableString(text);
		//spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		//spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
		//text.setText(spanString);
		textView.setTypeface(null, Typeface.BOLD);
	}
	*/

	private void setBold(TextView textView) {
		//SpannableString spanString = new SpannableString(tempString);
		//spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		//spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		//spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
		//text.setText(spanString);
		textView.setTypeface(null, Typeface.BOLD);
	}


/*	private static String folderSta = "font/";
	public static Typeface getTypeface(Context context, String opensansBold) {
		Typeface mTypeface =  Typeface.createFromAsset(context.getAssets(), folderSta + opensansBold);
		return mTypeface;
	}*/
}
