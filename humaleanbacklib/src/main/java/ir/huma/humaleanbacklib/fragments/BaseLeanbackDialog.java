package ir.huma.humaleanbacklib.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.view.View;

import ir.huma.humaleanbacklib.Util.GuidedStepsUtil;

public class BaseLeanbackDialog extends BaseGuidedStepFragment {
    @Override
    public void initial() {
        Bundle bundle = getArguments();
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
}
