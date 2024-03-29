package ir.huma.humaleanbacklib.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;
import androidx.leanback.widget.GuidedActionAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
    private List<GuidedAction> actionList = new ArrayList<>();
    private Integer descriptionTextSize;
    private Integer descriptionTextColor;
    private boolean autoEmptyFields = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        initial();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = super.onCreateView(inflater, container, savedInstanceState);
        if (titleTypeface != null)
            FontManager.instance(titleTypeface).setTypefaceImmediate(v);

        try {
            Field f = GuidedStepSupportFragment.class.getDeclaredField("mAdapter");
            f.setAccessible(true);
            actionAdapter = (GuidedActionAdapter) f.get(this);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        TextView textView = v.findViewById(R.id.guidance_description);
        if (descriptionTextSize != null) {
            textView.setTextSize(descriptionTextSize);
        }
        if (descriptionTextColor != null) {
            textView.setTextColor(descriptionTextColor);
        }

        textView.setMaxLines(20);


        return v;
    }


    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        super.onCreateActions(actions, savedInstanceState);
        actions.addAll(actionList);
    }

    View lastSelectedTitle;
    View lastSelectedDescription;
    View lastSelectedIcon;

    @Override
    public void onGuidedActionFocused(final GuidedAction action) {
        final int current = getActions().indexOf(action);
        if (actionTypeface != null) {
            int start = current - 10 < 0 ? 0 : current - 10;
            int end = current + 10 > getActions().size() ? getActions().size() : current + 10;
            for (int i = start; i < end; i++) {
                FontManager.instance(actionTypeface).setTypefaceImmediate(getActionItemView(i));
            }
        }
        if (lastSelectedTitle != null) {
            lastSelectedTitle.setSelected(false);
        }
        if (lastSelectedDescription != null) {
            lastSelectedDescription.setSelected(false);
            lastSelectedDescription.setOnFocusChangeListener(null);
        }
        if (lastSelectedIcon != null) {
            lastSelectedIcon.setSelected(false);
        }

        final View v = getActionItemView(current);
        try {
            lastSelectedTitle = v.findViewById(R.id.guidedactions_item_title);
            lastSelectedDescription = v.findViewById(R.id.guidedactions_item_description);
            lastSelectedIcon = v.findViewById(R.id.guidedactions_item_icon);
            if(lastSelectedDescription instanceof EditText)
                ((EditText) lastSelectedDescription).setInputType(action.getInputType());
            if(lastSelectedTitle instanceof EditText)
                ((EditText) lastSelectedTitle).setInputType(action.getInputType());
            lastSelectedTitle.setSelected(true);
            lastSelectedDescription.setSelected(true);
            lastSelectedIcon.setSelected(true);

            if (action.isDescriptionEditable()) {
                lastSelectedDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        EditText editText = v.findViewById(R.id.guidedactions_item_description);
                        if (b) {
                            if(isAutoEmptyFields()){
                                editText.setText("");
                            } else {
                                editText.setSelection(editText.getText().length());
                            }
                            if (lastSelectedTitle != null) {
                                lastSelectedTitle.setSelected(false);
                            }
                            if (lastSelectedDescription != null) {
                                lastSelectedDescription.setSelected(false);
                            }
                            if (lastSelectedIcon != null) {
                                lastSelectedIcon.setSelected(false);
                            }
                            lastSelectedTitle = null;
                            lastSelectedDescription = null;
                            lastSelectedIcon = null;
                        } else {
                            action.setDescription(editText.getText());
                            view.setOnFocusChangeListener(null);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        onItemSelectedListener(v, action, 0, current);
    }


    @Override
    public long onGuidedActionEditedAndProceed(GuidedAction action) {
        return super.onGuidedActionEditedAndProceed(action);
    }


    @Override
    public void onGuidedActionEdited(GuidedAction action) {
        super.onGuidedActionEdited(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        int current = getActions().indexOf(action);
        onItemClickListener(getActionItemView(current), action, 0, current);
    }

    public void addActions(GuidedAction... actions) {
        for (int i = 0; i < actions.length; i++) {
            actionList.add(actions[i]);
        }
        if (actionAdapter != null) {
            getActions().clear();
            getActions().addAll(actionList);
            actionAdapter.notifyDataSetChanged();
        }
    }

    public void addActions(int index, GuidedAction... actions) {
        for (int i = 0; i < actions.length; i++) {
            actionList.add(index + i, actions[i]);
        }
        if (actionAdapter != null) {
            getActions().clear();
            getActions().addAll(actionList);
            actionAdapter.notifyDataSetChanged();
        }
    }

    public void clearActions() {
        actionList.clear();

        if (actionAdapter != null) {
            getActions().clear();
            actionAdapter.notifyDataSetChanged();
        }
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

    public void setDescriptionTextSize(Integer descriptionTextSize) {
        this.descriptionTextSize = descriptionTextSize;
    }

    public void setDescriptionTextColor(Integer descriptionTextColor) {
        this.descriptionTextColor = descriptionTextColor;
    }

    public boolean isAutoEmptyFields() {
        return autoEmptyFields;
    }

    public void setAutoEmptyFields(boolean autoEmptyFields) {
        this.autoEmptyFields = autoEmptyFields;
    }

    @Override
    public int onProvideTheme() {
        return style;
//        return super.onProvideTheme();
    }
}
