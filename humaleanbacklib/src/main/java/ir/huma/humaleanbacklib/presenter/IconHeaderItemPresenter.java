package ir.huma.humaleanbacklib.presenter;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.NonOverlappingLinearLayout;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ir.huma.humaleanbacklib.R;


/**
 * Customized HeaderItem Presenter to show {@link IconHeaderItem}
 */
public class IconHeaderItemPresenter extends RowHeaderPresenter {

    private static final String TAG = IconHeaderItemPresenter.class.getSimpleName();

    private float mUnselectedAlpha;

    boolean hasFocusable = true;

    boolean isRtl = false;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mUnselectedAlpha = viewGroup.getResources()
                .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.icon_header_item, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        IconHeaderItem iconHeaderItem = (IconHeaderItem) ((Row) o).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setFocusable(hasFocusable);
        if (isRtl)
            ((NonOverlappingLinearLayout) rootView.getParent()).setGravity(Gravity.RIGHT);

        ImageView iconView = (ImageView) rootView.findViewById(R.id.header_icon);

        if (iconHeaderItem.getIcon() != null) { // Show icon only when it is set.

            iconView.setImageDrawable(iconHeaderItem.getIcon());
        } else {
            iconView.setVisibility(View.GONE);
        }

        TextView label = (TextView) rootView.findViewById(R.id.header_label);
        label.setText(iconHeaderItem.getName());
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, iconHeaderItem.getTextSize());
        if (iconHeaderItem.isShadow()) {
            label.setShadowLayer(2, 4, 3, Color.BLACK);
        }
        if (iconHeaderItem.getTypeface() != null) {
            label.setTypeface(iconHeaderItem.getTypeface());
        }
//        rootView.setAlpha(mUnselectedAlpha);
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // no op
    }

    // TODO: TEMP - remove me when leanback onCreateViewHolder no longer sets the mUnselectAlpha,AND
    // also assumes the xml inflation will return a RowHeaderView
    @Override
    protected void onSelectLevelChanged(ViewHolder holder) {
        // this is a temporary fix
//        holder.view.setAlpha(mUnselectedAlpha + holder.getSelectLevel() *
//                (1.0f - mUnselectedAlpha));
    }


    public boolean isHasFocusable() {
        return hasFocusable;
    }

    public IconHeaderItemPresenter setHasFocusable(boolean hasFocusable) {
        this.hasFocusable = hasFocusable;
        return this;
    }

    public boolean isRtl() {
        return isRtl;
    }

    public IconHeaderItemPresenter setRtl(boolean rtl) {
        isRtl = rtl;
        return this;
    }


}
