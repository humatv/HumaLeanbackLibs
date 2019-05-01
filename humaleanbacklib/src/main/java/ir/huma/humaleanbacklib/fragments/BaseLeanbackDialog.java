package ir.huma.humaleanbacklib.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.view.View;

import ir.huma.humaleanbacklib.PublicActivity;
import ir.huma.humaleanbacklib.Util.GuidedStepsUtil;

public class BaseLeanbackDialog extends BaseGuidedStepFragment {
    @Override
    public void initial() {
        Bundle bundle = getArguments();


        if (bundle.containsKey("style")) {
            setStyle(bundle.getInt("style"));
        }

        if (bundle.containsKey("typeface")) {
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), bundle.getString("typeface"));
            setTitleTypeface(typeface);
            setTitleTypeface(typeface);
        }

        String title = bundle.getString("title");
        String description = bundle.getString("description");
        String positiveText = bundle.getString("positiveText");
        String negativeText = bundle.getString("negativeText");
        addActions(GuidedStepsUtil.getAction(1, positiveText, "", getActivity()));
        addActions(GuidedStepsUtil.getAction(2, negativeText, "", getActivity()));

        setGuidance(new GuidanceStylist.Guidance(title, description, "", null));
    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {
        if (((Action) item).getId() == 1) {
            getActivity().setResult(Activity.RESULT_OK);
        }
        getActivity().finish();
    }


    public static class Builder {
        String title = "";
        String description = "";
        String positiveText = "";
        String negativeText = "";
        int style = -1;
        String typefaceInAsset;
        boolean isRtl;

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

        public Builder setTypefaceInAsset(String typefaceInAsset) {
            this.typefaceInAsset = typefaceInAsset;
            return this;
        }

        public Builder setRtl(boolean rtl) {
            isRtl = rtl;
            return this;
        }

        public void build(Activity context, int requestCode) {
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("description", description);
            bundle.putString("positiveText", positiveText);
            bundle.putString("negativeText", negativeText);
            if (style != -1)
                bundle.putInt("style", style);
            if (typefaceInAsset != null)
                bundle.putString("typeface", typefaceInAsset);

            PublicActivity.startWithFragmentForResult(context, BaseLeanbackDialog.class, bundle, isRtl, requestCode);
        }
    }
}
