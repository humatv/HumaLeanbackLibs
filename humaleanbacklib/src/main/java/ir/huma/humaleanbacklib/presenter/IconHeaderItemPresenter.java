package ir.huma.humaleanbacklib.presenter;

import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.leanback.widget.NonOverlappingLinearLayout;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowHeaderPresenter;
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

    //private float mUnselectedAlpha =1;

    boolean hasFocusable = true;

    boolean isRtl = false;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
//        mUnselectedAlpha = viewGroup.getResources()
//                .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
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
        viewHolder.view.setTag(iconHeaderItem);
        rootView.setAlpha(iconHeaderItem.getUnselectedAlpha());
        if (iconHeaderItem.getIcon() != null) { // Show icon only when it is set.

            iconView.setImageDrawable(iconHeaderItem.getIcon());
        } else {
            iconView.setVisibility(View.GONE);
        }

        AppCompatTextView label = (AppCompatTextView) rootView.findViewById(R.id.header_label);
        label.setText(iconHeaderItem.getName());
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, iconHeaderItem.getTextSize());
        label.setTextColor(iconHeaderItem.getTextColor());

        if (iconHeaderItem.isShadow()) {
            label.setShadowLayer(2, 4, 3, Color.BLACK);
        }
        if (iconHeaderItem.getTypeface() != null) {
            label.setTypeface(iconHeaderItem.getTypeface());
        }
//        rootView.setAlpha(1);
        label.setAlpha(1);

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
        IconHeaderItem item = (IconHeaderItem) holder.view.getTag();
        if(item != null) {
//            if(holder.getSelectLevel() >= 1){
//                holder.view.setAlpha(1);
//            } else {
//                holder.view.setAlpha(item.getUnselectedAlpha());
//            }
            holder.view.setAlpha(item.getUnselectedAlpha() + holder.getSelectLevel() *
                    (1.0f - item.getUnselectedAlpha()));
        }
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
