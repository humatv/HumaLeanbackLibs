package ir.huma.humaleanbacklib.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.GuidedAction;
import android.support.v17.leanback.widget.GuidedDatePickerAction;

import java.util.Date;
import java.util.List;

public class GuidedStepsUtil {


    public static GuidedAction getAction(long id, String title, String desc, Context context) {
        return new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .editDescription(desc)
                .description(desc)
                .build();
    }

    public static GuidedAction getAction(long id, String title, String desc, Drawable drawable, Context context) {
        return new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .icon(drawable)
                .description(desc)
                .build();
    }

    public static GuidedAction getAction(long id, String title, String desc, int resDrawable, Context context) {
        return new GuidedAction.Builder(context)
                .id(id)
                .icon(resDrawable)
                .title(title)
                .description(desc)
                .build();
    }

    public static GuidedAction getEditableAction(long id, String title, String desc, int inputType, Context context) {
        return new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .descriptionEditable(true)
                .descriptionEditInputType(inputType)
                .descriptionInputType(inputType)
                .autoSaveRestoreEnabled(true)
                .description(desc)
                .build();
    }

    public static GuidedAction getDropDownAction(long id, String title, String desc, List<GuidedAction> selectionActions, Context context) {
        return new GuidedAction.Builder(context)
                .id(id)
                .title(title)
                .description(desc)
                .subActions(selectionActions)
                .build();
    }

    public static GuidedAction getCheckedAction(int id, String title, String desc, boolean checked, Context context) {
        GuidedAction guidedAction = new GuidedAction.Builder(context)
                .title(title)
                .description(desc)
                .checkSetId(id)
                .build();
        guidedAction.setChecked(checked);
        return guidedAction;
    }

    public static GuidedAction getDateAction(long id, String title, long date, Context context) {
        return new GuidedDatePickerAction.Builder(context)
                .id(id)
                .date(date)
                .datePickerFormat("DMY")
                .maxDate(new Date().getTime())
                .title(title)
                .build();
    }


}
