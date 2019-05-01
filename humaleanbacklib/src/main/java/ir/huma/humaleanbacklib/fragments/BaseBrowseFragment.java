package ir.huma.humaleanbacklib.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.TitleViewAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.Timer;
import java.util.TimerTask;

import ir.huma.humaleanbacklib.R;
import ir.huma.humaleanbacklib.Util.CustomTitleView;
import ir.huma.humaleanbacklib.Util.ImageLoader;

/**
 * created by Morteza.
 * company: Mobin Tabaran
 * create Date: 09 May 2018
 * user Group: mobin
 * project: humaMagazine
 * package: ir.huma.android.magazine.fragment
 */

public abstract class BaseBrowseFragment extends BrowseSupportFragment implements FragmentProvider{


    private static final long BACKGROUND_UPDATE_DELAY = 400;
    private static final String TAG = BaseBrowseFragment.class.getSimpleName();


    public BackgroundManager mBackgroundManager;

    protected DisplayMetrics mMetrics;
    protected Timer mBackgroundTimer;

    private Object background;
    public static final Handler HANDLER = new Handler();
    private boolean mShowHeader = true;
    private int drawableResId = R.drawable.default_background;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();
        initial();
        setHeaders();
        setEventListener();
    }

    private void setEventListener() {

        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                int rowPos = 0;
                for (int i = 0; i < getAdapter().size(); i++) {
                    if (row == getAdapter().get(i)) {
                        rowPos = i;
                        break;
                    }
                }
                int pos = 0;
                if (row instanceof ListRow) {
                    for (int i = 0; i < ((ListRow) row).getAdapter().size(); i++) {
                        if (((ListRow) row).getAdapter().get(i) == item) {
                            pos = i;
                            break;
                        }
                    }
                }

                onItemSelectedListener(itemViewHolder != null ? itemViewHolder.view : null, item, rowPos, pos);

            }
        });


        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                int rowPos = 0;
                for (int i = 0; i < getAdapter().size(); i++) {
                    if (row == getAdapter().get(i)) {
                        rowPos = i;
                        break;
                    }
                }
                int pos = 0;
                if (row instanceof ListRow) {
                    for (int i = 0; i < ((ListRow) row).getAdapter().size(); i++) {
                        if (((ListRow) row).getAdapter().get(i) == item) {
                            pos = i;
                            break;
                        }
                    }
                }

                onItemClickListener(itemViewHolder != null ? itemViewHolder.view : null, item, rowPos, pos);

            }
        });
    }

    private void setHeaders() {

        if (!mShowHeader) {
            setHeadersTransitionOnBackEnabled(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setSelectedPosition(0, false);
                    startHeadersTransition(false);
                    setHeadersState(HEADERS_HIDDEN);
                }
            }, 10);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setSelectedPosition(0, false);
                    startHeadersTransition(false);
                    setHeadersState(HEADERS_DISABLED);
                }
            }, 100);
        } else {
            prepareEntranceTransition();
        }
    }


    private void prepareBackgroundManager() {

        if (mBackgroundManager == null) {
            try {
                mBackgroundManager = BackgroundManager.getInstance(getActivity());
                mBackgroundManager.attach(getActivity().getWindow());
                mMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    protected class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("backgroundManager", "UpdateBackgroundTask");
                    updateBackground(getBackground());
                }
            });
        }
    }

    private int tryLoadingBackground = 0;

    protected void updateBackground(final Object background) {

        if (mBackgroundManager == null) {
            prepareBackgroundManager();
        }

        Log.e("backgroundManager", "updateBackground");

        if (background != null && background instanceof String) {
            new ImageLoader()
                    .setSize(mMetrics.widthPixels + 180, mMetrics.heightPixels)
                    .setReadyListener(new ImageLoader.ReadyListener() {
                        @Override
                        public void onReady(Bitmap bitmap) {
                            if (BaseBrowseFragment.this.background == background) {
                                mBackgroundManager.setBitmap(bitmap);
                            }
                        }
                    }).load(getContext(), (String) background);
//            final RequestOptions options = new RequestOptions().override(mMetrics.widthPixels+180, mMetrics.heightPixels).centerCrop();
//
//            Glide.with(this).asBitmap().apply(options).load(background).addListener(new RequestListener<Bitmap>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                    if (BaseBrowseFragment.this.background == background && tryLoadingBackground < 5) {
//                        Glide.with(getActivity()).asBitmap().load(background).addListener(this);
//                    }
//                    tryLoadingBackground++;
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                    if (BaseBrowseFragment.this.background == background) {
//                        mBackgroundManager.setBitmap(resource);
//                    }
//                    return false;
//                }
//            }).preload();

        } else if (background != null && background instanceof Drawable) {
            mBackgroundManager.setDrawable((Drawable) background);
        } else {
            mBackgroundManager.setDrawable(ResourcesCompat.getDrawable(getResources(), drawableResId, null));
        }

        if (mBackgroundTimer != null) {
            mBackgroundTimer.cancel();
        }
    }


    public void startBackgroundTimer(boolean hasDelay) {

        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }

        mBackgroundTimer = new Timer();

        mBackgroundTimer.schedule(new UpdateBackgroundTask(), hasDelay ? BACKGROUND_UPDATE_DELAY : 0);

    }


    @Override
    public void onDestroy() {
        if (null != mBackgroundTimer) {
            Log.d(TAG, "destroy : " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
        super.onDestroy();
    }


    public void setBackgroundDrawable(Drawable drawable) {
        this.background = drawable;
    }

    public void setBackgroundUri(String backgroundUri) {
        this.background = backgroundUri;
    }


    public void setBackgroundDrawable(Drawable drawable, boolean hasDelay) {
        this.background = drawable;
        startBackgroundTimer(hasDelay);
    }

    public void setBackgroundUri(String backgroundUri, boolean hasDelay) {
        this.background = backgroundUri;
        startBackgroundTimer(hasDelay);
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public void setDrawableResId(int drawableResId) {
        this.drawableResId = drawableResId;
    }

    public Object getBackground() {
        return background;
    }


    public boolean isShowHeader() {
        return mShowHeader;
    }

    public void setShowHeader(boolean mShowHeader) {
        this.mShowHeader = mShowHeader;
    }

    public void setTitleView(int resId, int searchOrbViewId, CustomTitleView.OnTitleReadyListener onTitleReadyListener) {
        ((CustomTitleView) getTitleView()).addView(resId, searchOrbViewId, onTitleReadyListener);
    }


}
