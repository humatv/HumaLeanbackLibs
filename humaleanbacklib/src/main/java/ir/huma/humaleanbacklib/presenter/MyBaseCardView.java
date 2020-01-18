package ir.huma.humaleanbacklib.presenter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.ObjectAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public abstract class MyBaseCardView<T> extends BaseCardView {


    private static final String TAG = "Base Card";

    private int layoutResId;
    ObjectAdapter adapter;
    private T data;

    public MyBaseCardView(Context context, int layoutResId) {
        super(context);
        this.layoutResId = layoutResId;
        init();

    }

    public MyBaseCardView(Context context, int layoutResId, AttributeSet attrs) {
        super(context, attrs);
        this.layoutResId = layoutResId;

        init();
    }

    public MyBaseCardView(Context context, int layoutResId, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.layoutResId = layoutResId;

        init();
    }

    public ObjectAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ObjectAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void setSelected(boolean selected) {
        int pos = 0;

        if (adapter != null &&  adapter instanceof ArrayObjectAdapter) {
            pos = ((ArrayObjectAdapter) adapter).indexOf(data);
        }

        changeSelected(selected, pos);

    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(false);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(layoutResId, this);

    }


    @Override
    public void setBackground(Drawable background) {
//        super.setBackground(background);
    }


    protected void animateSelected() {
        if (getAnimationLayout() != null) {
            getAnimationLayout().animate()
                    .translationY(getAnimationLayout().getHeight())
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            getAnimationLayout().setVisibility(View.GONE);
                        }
                    });
        }
    }

    protected void animateUnSelected() {
        if (getAnimationLayout() != null) {

        }
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    protected View getAnimationLayout() {
        return null;
    }

    public abstract void fillData(T t);

    protected abstract void changeSelected(boolean selected, int pos);


}

