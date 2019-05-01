package ir.huma.humaleanbacklib.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v17.leanback.app.GuidedStepSupportFragment;
import android.support.v17.leanback.widget.GuidanceStylist;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v17.leanback.widget.GuidedActionAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guidedkeyboardfixerlib.MyGuidedStepSupportFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ir.atitec.everythingmanager.manager.FontManager;
import ir.huma.humaleanbacklib.R;

public abstract class BaseGuidedStepFragment extends MyGuidedStepSupportFragment implements FragmentProvider {

    private Typeface titleTypeface;
    private Typeface actionTypeface;
    private int style = -1;
    private GuidanceStylist.Guidance guidance;
    private GuidedActionAdapter actionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initial();
        final View v = super.onCreateView(inflater, container, savedInstanceState);

        FontManager.instance(titleTypeface).setTypefaceImmediate(v);

        return v;
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        super.onCreateActions(actions, savedInstanceState);
        try {
            Field f = GuidedStepSupportFragment.class.getDeclaredField("mAdapter");
            f.setAccessible(true);
            actionAdapter = (GuidedActionAdapter) f.get(this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuidedActionFocused(GuidedAction action) {
        int current = getActions().indexOf(action);
        if (actionTypeface != null) {
            int start = current - 10 < 0 ? 0 : current - 10;
            int end = current + 10 > getActions().size() ? getActions().size() : current + 10;
            for (int i = start; i < end; i++) {
                FontManager.instance(actionTypeface).setTypefaceImmediate(getActionItemView(i));
            }
        }
        onItemSelectedListener(getActionItemView(current), action, 0, current);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        int current = getActions().indexOf(action);

        onItemClickListener(getActionItemView(current), action, 0, current);
    }

    public void addActions(GuidedAction... actions) {
        for (int i = 0; i < actions.length; i++) {
            getActions().add(actions[i]);
        }
        if (actionAdapter != null)
            actionAdapter.notifyDataSetChanged();
    }

    public void addActions(int index, GuidedAction... actions) {
        for (int i = 0; i < actions.length; i++) {
            getActions().add(index + i, actions[i]);
        }
        if (actionAdapter != null)
            actionAdapter.notifyDataSetChanged();
    }

    public void clearActions() {
        getActions().clear();
        if (actionAdapter != null)
            actionAdapter.notifyDataSetChanged();
    }


    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
        return guidance;
    }

    public GuidanceStylist.Guidance getGuidance() {
        return guidance;
    }

    public void setGuidance(GuidanceStylist.Guidance guidance) {
        this.guidance = guidance;
    }

    public void setTitleTypeface(Typeface titleTypeface) {
        this.titleTypeface = titleTypeface;
    }

    public void setActionTypeface(Typeface actionTypeface) {
        this.actionTypeface = actionTypeface;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public GuidedActionAdapter getActionAdapter() {
        return actionAdapter;
    }

    @Override
    public int onProvideTheme() {
        return style;
//        return super.onProvideTheme();
    }
}
