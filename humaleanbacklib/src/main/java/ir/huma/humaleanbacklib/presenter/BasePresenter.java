package ir.huma.humaleanbacklib.presenter;

import android.content.Context;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

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
            TView v = tViewClass.getConstructor(Context.class, int.class).newInstance(context, layoutResId);
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

}
