package ir.huma.humaleanbacklib.presenter;

import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.RowPresenter;
import android.view.View;

public class MyListRowPresenter extends ListRowPresenter {

    private static final String TAG = MyListRowPresenter.class.getSimpleName();
    private RowPresenter.ViewHolder holder;
    int horizontalNumRows = 1;
    boolean isRtl = false;
    IconHeaderItemPresenter iconHeaderItemPresenter;
    int[] padding = new int[]{20, 0, 20, 0};

    public MyListRowPresenter() {
        this(FocusHighlight.ZOOM_FACTOR_MEDIUM, false);

    }

    public MyListRowPresenter(int focusZoomFactor, boolean useFocusDimmer) {
        super(focusZoomFactor, useFocusDimmer);
        setHeaderPresenter(iconHeaderItemPresenter = new IconHeaderItemPresenter().setHasFocusable(false));

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
//        horizontalGridView.setAlpha(0.9f);
        horizontalGridView.offsetLeftAndRight(200);
        horizontalGridView.setPadding(padding[0], padding[1], padding[2], padding[3]);

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
        iconHeaderItemPresenter.setRtl(rtl);
        return this;
    }

    @Override
    public boolean isUsingDefaultListSelectEffect() {
        return false;
    }


    public MyListRowPresenter setHorizontalGridViewPadding(int left, int top, int right, int bottom) {
        padding = new int[]{left, top, right, bottom};
        return this;
    }
}
