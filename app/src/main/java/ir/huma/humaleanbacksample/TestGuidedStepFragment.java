package ir.huma.humaleanbacksample;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.huma.humaleanbacklib.Util.GuidedStepsUtil;
import ir.huma.humaleanbacklib.fragments.BaseGuidedStepFragment;

public class TestGuidedStepFragment extends BaseGuidedStepFragment {


    @Override
    public void initial() {
        setActionTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/IRANSans.ttf"));
        setTitleTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/IRANSans.ttf"));
        setStyle(R.style.MyLeanbackWizard);
        setGuidance(new GuidanceStylist.Guidance("عنوان",
                "توضیحات کامل",
                "بالای توضیحات", getResources().getDrawable(R.mipmap.ic_launcher)));


        for(int i=0;i<100;i++){
            addActions(GuidedStepsUtil.getAction(1, "اکشن : "+ i, "زیر اکشن", getContext()));
        }

    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {

    }
}