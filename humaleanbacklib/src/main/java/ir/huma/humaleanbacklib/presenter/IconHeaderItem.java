package ir.huma.humaleanbacklib.presenter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.HeaderItem;

public class IconHeaderItem extends HeaderItem {


    private static final String TAG = IconHeaderItem.class.getSimpleName();
    public static final int ICON_NONE = -1;

    /**
     * Hold an icon resource id
     */
    private Drawable icon = null;
    private Typeface typeface;
    private boolean shadow = false;
    /**
     * unit in sp
     */
    private int textSize = 16;
    private int textColor = Color.WHITE;

    public IconHeaderItem(long id, String name, Drawable icon) {
        super(id, name);
        this.icon = icon;
    }

    public IconHeaderItem(long id, String name) {
        this(id, name, null);
    }

    public IconHeaderItem(String name) {
        super(name);
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
