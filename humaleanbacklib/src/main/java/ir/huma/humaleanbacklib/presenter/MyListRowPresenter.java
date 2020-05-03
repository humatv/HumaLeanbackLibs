package ir.huma.humaleanbacklib.presenter;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.RowPresenter;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class MyListRowPresenter extends ListRowPresenter {

    private static final String TAG = MyListRowPresenter.class.getSimpleName();
    private ArrayObjectAdapter arrayObjectAdapter;
    private OnLongClickListener onLongClickListener;
    private BaseGridView.OnKeyInterceptListener onKeyInterceptListener;
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

    boolean longPress;

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, final Object item) {
        this.holder = holder;
        super.onBindRowViewHolder(holder, item);
        final ListRow rtlListRow = (ListRow) item;
        final ViewHolder viewHolder = ((ViewHolder) holder);

        viewHolder.getListRowPresenter().setShadowEnabled(false);
        final HorizontalGridView horizontalGridView = viewHolder.getGridView();
        if (isRtl)
            horizontalGridView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        horizontalGridView.setAlpha(0.9f);
        horizontalGridView.offsetLeftAndRight(200);
        horizontalGridView.setPadding(padding[0], padding[1], padding[2], padding[3]);

        horizontalGridView.setNumRows(getHorizontalNumRows());

        horizontalGridView.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                if (onLongClickListener != null) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                        int position = 0;
                        if (arrayObjectAdapter != null)
                            position = arrayObjectAdapter.indexOf(viewHolder.getRow());
                        int subPosition = horizontalGridView.getSelectedPosition();
                        Object obj = item;

                        View v = horizontalGridView.findViewHolderForAdapterPosition(subPosition).itemView;

                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            long eventDuration = event.getEventTime() - event.getDownTime();
                            if (eventDuration > ViewConfiguration.getLongPressTimeout()) {
                                if (!longPress) {
                                    Log.d("MyVerticalGridView", "onKeyLongClick");
                                    try {
                                        onLongClickListener.onLongClickListener(v, obj, position, subPosition);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                longPress = true;
                                return true;
                            }
                            return false;
                        } else {
                            if (longPress) {
                                longPress = false;
                                return true;
                            } else {
                                Log.d("MyVerticalGridView", "onKeyClick");
//                            try {
//                                onItemClickListener.onItemClick(position, subPosition, obj, v, adapters[position]);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                                return false;
                            }
                        }
                    }
                }
                if (onKeyInterceptListener != null) {
                    return onKeyInterceptListener.onInterceptKeyEvent(event);
                }
                return false;
            }
        });

    }

    public MyListRowPresenter setHorizontalNumRows(int numRows) {
        this.horizontalNumRows = numRows;
        return this;
    }

    public int getHorizontalNumRows() {
        return horizontalNumRows;
    }

//    public void setSelectedItem(int position) {
//        if (holder != null) {
//            ViewHolder vh = (ViewHolder) holder;
//            vh.getGridView().setSelectedPosition(position);
//        }
//    }

    public boolean isRtl() {
        return isRtl;
    }

    public MyListRowPresenter setRtl(boolean rtl) {
        isRtl = rtl;
        iconHeaderItemPresenter.setRtl(rtl);
        return this;
    }

    public void setAdapter(ArrayObjectAdapter adapter) {
        this.arrayObjectAdapter = adapter;
    }

    @Override
    public boolean isUsingDefaultListSelectEffect() {
        return false;
    }


    public MyListRowPresenter setHorizontalGridViewPadding(int left, int top, int right, int bottom) {
        padding = new int[]{left, top, right, bottom};
        return this;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnKeyInterceptListener(BaseGridView.OnKeyInterceptListener onKeyInterceptListener) {
        this.onKeyInterceptListener = onKeyInterceptListener;
    }

    public interface OnLongClickListener {
        void onLongClickListener(View v, Object item, int rowPos, int pos);
    }
}
