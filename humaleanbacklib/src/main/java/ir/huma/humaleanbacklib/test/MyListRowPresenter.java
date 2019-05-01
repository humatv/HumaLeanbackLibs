package ir.huma.humaleanbacklib.test;

import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.View;

import ir.huma.humaleanbacklib.test.IconHeaderItemPresenter;

public class MyListRowPresenter extends ListRowPresenter {

    private static final String TAG = MyListRowPresenter.class.getSimpleName();
    private RowPresenter.ViewHolder holder;
    int horizontalNumRows = 1;
    boolean isRtl = false;

    public MyListRowPresenter() {
        this(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);

    }

    public MyListRowPresenter(int focusZoomFactor, boolean useFocusDimmer) {
        super(focusZoomFactor, useFocusDimmer);
        setHeaderPresenter(new IconHeaderItemPresenter().setHasFocusable(false));

        setShadowEnabled(false);
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        this.holder = holder;
        super.onBindRowViewHolder(holder, item);
        final ListRow rtlListRow = (ListRow) item;
        ViewHolder viewHolder = ((ViewHolder) holder);

        viewHolder.getListRowPresenter().setShadowEnabled(false);
        final HorizontalGridView horizontalGridView = viewHolder.getGridView();
        if (isRtl)
            horizontalGridView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        horizontalGridView.setAlpha(0.9f);
        horizontalGridView.offsetLeftAndRight(200);
        horizontalGridView.setNumRows(getHorizontalNumRows());

    }


    public MyListRowPresenter setHorizontalNumRows(int numRows) {
        this.horizontalNumRows = numRows;
        return this;
    }

    public int getHorizontalNumRows() {
        return horizontalNumRows;
    }

    public void setSelectedItem(int position) {
        if (holder != null) {
            ViewHolder vh = (ViewHolder) holder;
            vh.getGridView().setSelectedPosition(position);
        }
    }

    public boolean isRtl() {
        return isRtl;
    }

    public MyListRowPresenter setRtl(boolean rtl) {
        isRtl = rtl;
        return this;
    }

    @Override
    public boolean isUsingDefaultListSelectEffect() {
        return false;
    }
}
