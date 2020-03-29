package ir.huma.humaleanbacklib.presenter;


import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.leanback.R;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;
import ir.atitec.everythingmanager.manager.FontManager;

public class MyActionPresenterSelector extends PresenterSelector {

    private final OneLineActionPresenter mOneLineActionPresenter = new OneLineActionPresenter();
    private final TwoLineActionPresenter mTwoLineActionPresenter = new TwoLineActionPresenter();
    private final Presenter[] mPresenters = new Presenter[]{
            mOneLineActionPresenter, mTwoLineActionPresenter};

    private Typeface typeface;

    @Override
    public Presenter getPresenter(Object item) {
        Action action = (Action) item;
        if (TextUtils.isEmpty(action.getLabel2())) {
            return mOneLineActionPresenter;
        } else {
            return mTwoLineActionPresenter;
        }
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        mOneLineActionPresenter.setTypeface(typeface);
        mTwoLineActionPresenter.setTypeface(typeface);
    }

    @Override
    public Presenter[] getPresenters() {
        return mPresenters;
    }

    static class ActionViewHolder extends Presenter.ViewHolder {
        Action mAction;
        Button mButton;
        int mLayoutDirection;

        public ActionViewHolder(View view, int layoutDirection) {
            super(view);
            mButton = (Button) view.findViewById(R.id.lb_action_button);
            mLayoutDirection = layoutDirection;
        }
    }

    abstract static class ActionPresenter extends Presenter {
        @Override
        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
            Action action = (Action) item;
            MyActionPresenterSelector.ActionViewHolder vh = (MyActionPresenterSelector.ActionViewHolder) viewHolder;
            vh.mAction = action;
            Drawable icon = action.getIcon();
            if (icon != null) {
                final int startPadding = vh.view.getResources()
                        .getDimensionPixelSize(R.dimen.lb_action_with_icon_padding_start);
                final int endPadding = vh.view.getResources()
                        .getDimensionPixelSize(R.dimen.lb_action_with_icon_padding_end);
                vh.view.setPaddingRelative(startPadding, 0, endPadding, 0);
            } else {
                final int padding = vh.view.getResources()
                        .getDimensionPixelSize(R.dimen.lb_action_padding_horizontal);
                vh.view.setPaddingRelative(padding, 0, padding, 0);
            }
            if (vh.mLayoutDirection == View.LAYOUT_DIRECTION_RTL) {
                vh.mButton.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
            } else {
                vh.mButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            }
        }

        @Override
        public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
            MyActionPresenterSelector.ActionViewHolder vh = (MyActionPresenterSelector.ActionViewHolder) viewHolder;
            vh.mButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            vh.view.setPadding(0, 0, 0, 0);
            vh.mAction = null;
        }
    }

    static class OneLineActionPresenter extends MyActionPresenterSelector.ActionPresenter {
        private Typeface typeface;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lb_action_1_line, parent, false);

            return new MyActionPresenterSelector.ActionViewHolder(v, parent.getLayoutDirection());
        }

        @Override
        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
            super.onBindViewHolder(viewHolder, item);
            MyActionPresenterSelector.ActionViewHolder vh = ((MyActionPresenterSelector.ActionViewHolder) viewHolder);
            Action action = (Action) item;
            if (typeface != null)
                vh.mButton.setTypeface(typeface);
            vh.mButton.setText(action.getLabel1());
        }

        public void setTypeface(Typeface typeface) {
            this.typeface = typeface;
        }
    }

    static class TwoLineActionPresenter extends MyActionPresenterSelector.ActionPresenter {
        private Typeface typeface;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lb_action_2_lines, parent, false);
            return new MyActionPresenterSelector.ActionViewHolder(v, parent.getLayoutDirection());
        }

        @Override
        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
            super.onBindViewHolder(viewHolder, item);
            Action action = (Action) item;
            MyActionPresenterSelector.ActionViewHolder vh = (MyActionPresenterSelector.ActionViewHolder) viewHolder;
            if (typeface != null)
                vh.mButton.setTypeface(typeface);
            CharSequence line1 = action.getLabel1();
            CharSequence line2 = action.getLabel2();
            if (TextUtils.isEmpty(line1)) {
                vh.mButton.setText(line2);
            } else if (TextUtils.isEmpty(line2)) {
                vh.mButton.setText(line1);
            } else {
                vh.mButton.setText(line1 + "\n" + line2);
            }
        }

        public void setTypeface(Typeface typeface) {
            this.typeface = typeface;
        }
    }
}

