package ir.huma.humaleanbacklib.presenter;

import android.content.Context;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.VerticalGridView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;


public class BasePresenter<T, TView extends MyBaseCardView> extends Presenter {


    /**
     * create view holder by view {@linkplain #createView(ViewGroup)}
     *
     * @param parent view parent should be created from View group
     * @return the view holder
     */
    private Context context;
    private Class<TView> tViewClass;
    private int layoutResId;
    ObjectAdapter adapter;
    boolean longPress = false;
    private onItemLongClickListener onItemLongClickListener;

    public BasePresenter(Context context) {
        this.context = context;
    }

    public BasePresenter(Context context, Class<TView> tViewClass, int layoutResId) {
        this.context = context;
        this.tViewClass = tViewClass;
        this.layoutResId = layoutResId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

        return new ViewHolder(createView(parent));
    }

    public ObjectAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ObjectAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * cast bin view and pass into class object model
     *
     * @param viewHolder presenter view holder
     * @param item       item object
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {

        T t = (T) item;
        TView tView = (TView) viewHolder.view;
        tView.setData(t);
        tView.fillData(t);
//        onBindViewHolder(tView , t);
    }


    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        TView tView = (TView) viewHolder.view;
        onUnbindViewHolderInit(tView);
    }

    public Context getContext() {
        return context;
    }

    /**
     * onUnbind method cast view holder into view
     * should be override every where should be unUnbind
     *
     * @param tView the TView
     */
    protected void onUnbindViewHolderInit(TView tView) {

    }

    /**
     * bind view should be override
     * @param tView view
     * @param item the item
     */
//    protected abstract void onBindViewHolder(TView tView , T item );


    /**
     * create view from view holder
     *
     * @param parent view group
     * @return the created view of type TView
     */
    public TView createView(ViewGroup parent) {
        if (tViewClass == null) {
            throw new RuntimeException("if dont pass parameter tViewClass, you must override createView Method Of BasePresenter");
        }
        try {
            final TView v = tViewClass.getConstructor(Context.class, int.class).newInstance(context, layoutResId);
            v.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v2, int keyCode, KeyEvent event) {
                    if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            long eventDuration = event.getEventTime() - event.getDownTime();
                            if (eventDuration > ViewConfiguration.getLongPressTimeout()) {
                                if (!longPress) {
                                    longPress = true;
                                    if (onItemLongClickListener != null) {
                                        if (adapter != null) {
                                            int pos = 0, rowPos = 0;
                                            if (v2.getParent() instanceof HorizontalGridView) {
                                                pos = ((HorizontalGridView) v2.getParent()).getChildAdapterPosition(v2);
                                            }
                                            try {
                                                ViewParent temp = v2.getParent().getParent().getParent();
                                                VerticalGridView verticalGridView = (VerticalGridView) ((HorizontalGridView) v2.getParent()).getParent().getParent().getParent();
                                                rowPos = verticalGridView.getChildAdapterPosition((View) temp);
                                            } catch (Exception e) {

                                            }
                                            return onItemLongClickListener.onLongClick(v, v.getData(), rowPos, pos);
                                        }
                                    }
                                } else {
                                    return true;
                                }
                            }
                            return false;
                        } else {
                            if (longPress) {
                                longPress = false;
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                    return false;
                }
            });
            if (adapter != null) {
                v.setAdapter(adapter);
            }
            return v;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("can't instantiate from YourBaseCardView :P");
    }

    public void setOnItemLongClickListener(BasePresenter.onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface onItemLongClickListener {
        boolean onLongClick(View v, Object item, int rowPos, int pos);
    }

}
