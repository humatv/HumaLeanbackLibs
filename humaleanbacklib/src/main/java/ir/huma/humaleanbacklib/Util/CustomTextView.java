package ir.huma.humaleanbacklib.Util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


import java.util.Date;

import ir.huma.humaleanbacklib.R;


public class CustomTextView extends AppCompatTextView {
    private static final String TAG = "TextView";
    private Date date;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_tvCustomFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        if (asset != null && !asset.isEmpty()) {
            Typeface tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            setTypeface(tf);
        }

        return true;
    }


    public void setDate(Date date) {
        this.date = date;
    }
}
