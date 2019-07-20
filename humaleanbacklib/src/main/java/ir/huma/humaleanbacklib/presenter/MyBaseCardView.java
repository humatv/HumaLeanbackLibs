package ir.huma.humaleanbacklib.presenter;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public abstract class MyBaseCardView<T> extends BaseCardView {


    private static final String TAG = "Base Card";

    private int layoutResId;
    ObjectAdapter adapter;

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
        if (adapter instanceof ArrayObjectAdapter) {
            pos = ((ArrayObjectAdapter) adapter).indexOf(this);
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


    protected View getAnimationLayout() {
        return null;
    }

    public abstract void fillData(T t);

    protected abstract void changeSelected(boolean selected, int pos);


}

