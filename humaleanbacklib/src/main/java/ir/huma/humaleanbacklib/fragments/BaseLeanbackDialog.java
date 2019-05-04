package ir.huma.humaleanbacklib.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import java.io.ByteArrayOutputStream;

import ir.huma.humaleanbacklib.PublicActivity;
import ir.huma.humaleanbacklib.R;
import ir.huma.humaleanbacklib.Util.GuidedStepsUtil;

public class BaseLeanbackDialog extends BaseGuidedStepFragment {
    private View.OnClickListener positiveClickListener;
    private View.OnClickListener negativeClickListener;

    @Override
    public void initial() {
        Bundle bundle = getArguments();


        if (bundle.containsKey("style")) {
            setStyle(bundle.getInt("style"));
        }

        String title = bundle.getString("title");
        String description = bundle.getString("description");
        String positiveText = bundle.getString("positiveText");
        String negativeText = bundle.getString("negativeText");
        Drawable positiveIcon = null, negativeIcon = null, icon = null;
        if (bundle.containsKey("positiveIcon")) {
            positiveIcon = new BitmapDrawable((Bitmap) bundle.getParcelable("positiveIcon"));
        }
        if (bundle.containsKey("negativeIcon")) {
            negativeIcon = new BitmapDrawable((Bitmap) bundle.getParcelable("negativeIcon"));
        }
        if (bundle.containsKey("icon")) {
            icon = new BitmapDrawable((Bitmap) bundle.getParcelable("icon"));
        }

        addActions(GuidedStepsUtil.getAction(1, positiveText, "", positiveIcon, getActivity()));
        addActions(GuidedStepsUtil.getAction(2, negativeText, "", negativeIcon, getActivity()));

        setGuidance(new GuidanceStylist.Guidance(title, description, "", icon));

        if (bundle.containsKey("typeface")) {
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), bundle.getString("typeface"));
            setTitleTypeface(typeface);
            setTitleTypeface(typeface);
        }
    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {
        if (((Action) item).getId() == 1) {
            if (positiveClickListener != null) {
                positiveClickListener.onClick(v);
            }
        } else if (negativeClickListener != null) {
            negativeClickListener.onClick(v);
        }
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public View.OnClickListener getPositiveClickListener() {
        return positiveClickListener;
    }

    public void setPositiveClickListener(View.OnClickListener positiveClickListener) {
        this.positiveClickListener = positiveClickListener;
    }

    public View.OnClickListener getNegativeClickListener() {
        return negativeClickListener;
    }

    public void setNegativeClickListener(View.OnClickListener negativeClickListener) {
        this.negativeClickListener = negativeClickListener;
    }

    public static class Builder {
        private String title = "";
        private String description = "";
        private String positiveText = "";
        private String negativeText = "";
        private int style = -1;
        private Typeface typeface;
        private boolean isRtl;
        private Bitmap icon;
        private Bitmap positiveIcon;
        private Bitmap negativeIcon;

        private View.OnClickListener positiveClickListener;
        private View.OnClickListener negativeClickListener;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPositiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder setNegativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder setStyle(int style) {
            this.style = style;
            return this;
        }

        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        public Builder setRtl(boolean rtl) {
            isRtl = rtl;
            return this;
        }

        public Builder setIcon(Bitmap icon) {
            this.icon = icon;
            return this;
        }

        public Builder setPositiveIcon(Bitmap positiveIcon) {
            this.positiveIcon = positiveIcon;
            return this;
        }

        public Builder setNegativeIcon(Bitmap negativeIcon) {
            this.negativeIcon = negativeIcon;
            return this;
        }


        public Builder setIcon(Drawable icon) {
            this.icon = ((BitmapDrawable) icon).getBitmap();
            return this;
        }

        public Builder setPositiveIcon(Drawable positiveIcon) {
            this.positiveIcon = ((BitmapDrawable) positiveIcon).getBitmap();
            return this;
        }

        public Builder setNegativeIcon(Drawable negativeIcon) {
            this.negativeIcon = ((BitmapDrawable) negativeIcon).getBitmap();
            return this;
        }

        public Builder setPositiveClickListener(View.OnClickListener positiveClickListener) {
            this.positiveClickListener = positiveClickListener;
            return this;
        }

        public Builder setNegativeClickListener(View.OnClickListener negativeClickListener) {
            this.negativeClickListener = negativeClickListener;
            return this;
        }

        public void build(FragmentActivity context, int frameLayoutId) {
            FragmentTransaction tx = context.getSupportFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("description", description);
            bundle.putString("positiveText", positiveText);
            bundle.putString("negativeText", negativeText);
            if (icon != null)
                bundle.putParcelable("icon", icon);
            if (positiveIcon != null)
                bundle.putParcelable("positiveIcon", positiveIcon);
            if (negativeIcon != null)
                bundle.putParcelable("negativeIcon", negativeIcon);
            if (style != -1)
                bundle.putInt("style", style);

            BaseLeanbackDialog f = new BaseLeanbackDialog();
            f.setArguments(bundle);
            f.setPositiveClickListener(positiveClickListener);
            f.setNegativeClickListener(negativeClickListener);

            f.setTitleTypeface(typeface);
            f.setActionTypeface(typeface);

            tx.add(frameLayoutId, f);
            tx.addToBackStack("BaseLaenbackDialog");
            tx.commit();

//            PublicActivity.startWithFragmentForResult(context, BaseLeanbackDialog.class, bundle, isRtl, requestCode);
        }
    }
}
