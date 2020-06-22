package ir.huma.humaleanbacklib.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.BrowseFrameLayout;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.ScaleFrameLayout;
import androidx.leanback.widget.TitleViewAdapter;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import androidx.leanback.widget.VerticalGridView;
import androidx.recyclerview.widget.RecyclerView;
import ir.huma.humaleanbacklib.R;
import ir.huma.humaleanbacklib.Util.CustomTitleView;
import ir.huma.humaleanbacklib.Util.ImageLoader;
import ru.noties.sbv.ScrollingBackgroundView;

/**
 * created by Morteza.
 * company: Mobin Tabaran
 * create Date: 09 May 2018
 * user Group: mobin
 * project: humaMagazine
 * package: ir.huma.android.magazine.fragment
 */

public abstract class BaseBrowseFragment extends BrowseSupportFragment implements FragmentProvider {


    private static final long BACKGROUND_UPDATE_DELAY = 400;
    private static final String TAG = BaseBrowseFragment.class.getSimpleName();


    public BackgroundManager mBackgroundManager;

    protected DisplayMetrics mMetrics;
    protected Timer mBackgroundTimer;
    private boolean isInitBackgroundManager = true;
    private Object background;
    private Drawable scrollableBackground;
    public static final Handler HANDLER = new Handler();
    private boolean mShowHeader = true;
    private int drawableResId = R.drawable.default_background;
    private int maxScrollImage = 2687;
    boolean longPress = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initial();
//        prepareBackgroundManager();
        setHeaders();
        setEventListener();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Toast.makeText(getActivity(), "code : " + keyCode, Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });

        BrowseFrameLayout browseFrameLayout = (BrowseFrameLayout) ((FrameLayout) getView()).getChildAt(0);

//        browseFrameLayout.setOnDispatchKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
////                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
////                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
////                        long eventDuration = event.getEventTime() - event.getDownTime();
////                        if (eventDuration > ViewConfiguration.getLongPressTimeout()) {
////                            if (!longPress) {
////                                onItemLongClickListener();
////                            }
////                            longPress = true;
////                            return true;
////                        }
////                        return true;
////                    } else {
////                        if (longPress) {
////                            longPress = false;
////                            return true;
////                        } else {
//////                        IntentHandler.startApplication(getApplicationContext(), appInfo.packageName);
////                            return false;
////                        }
////                    }
////                }
////                return false;
//            }
//        });
    }

    int lastRowPos = 0;

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
                if (lastRowPos != rowPos) {
                    lastRowPos = rowPos;
                    if (rowPos == 0 && scrollView != null) {
                        scrollView.scrollTo(0, 0);
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

    ImageView scrollingBackgroundView;
    ScrollView scrollView;


    private void prepareBackgroundManager() {
        if (scrollableBackground != null) {
            if (scrollingBackgroundView == null) {
                scrollView = new ScrollView(getContext());
                final ScaleFrameLayout frameLayout = getView().findViewById(R.id.scale_frame);
                VerticalGridView rv = getView().findViewById(R.id.container_list);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                scrollView.setLayoutParams(layoutParams);

                scrollingBackgroundView = new ImageView(getContext());
                scrollingBackgroundView.setScaleType(ImageView.ScaleType.FIT_XY);
                scrollingBackgroundView.setBackgroundColor(Color.YELLOW);

                RelativeLayout relative = new RelativeLayout(getContext());
                relative.addView(scrollingBackgroundView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxScrollImage));

                scrollView.addView(relative, FrameLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ViewGroup viewGroup = getView().findViewById(R.id.browse_frame);
                viewGroup.addView(scrollView, 0);

                rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
//                        scroll += dy;
//                        Log.d("BaseBrowse", "scroll : " + dy + " " + scroll + " " + frameLayout.getHeight());
//                        int scrollY = recyclerView.computeVerticalScrollOffset();
                        // mAppBarBg corresponds to your light green background view
                        scrollView.scrollBy(dx, dy);
                        Log.d("BaseBrowse", "scroll : " + scrollView.getScrollY());
//                        scrollingBackgroundView.scrollBy(dx, dy);
                    }

                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                });
            }
        } else if (mBackgroundManager == null && isInitBackgroundManager) {
            try {
                mBackgroundManager = BackgroundManager.getInstance(getActivity());
                mBackgroundManager.attachToView(getView().findViewById(R.id.container_list));
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

        if (scrollingBackgroundView == null && mBackgroundManager == null) {
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
                                if (mBackgroundManager != null && mBackgroundManager.isAttached())
//                                scrollingBackgroundView.setDrawable(new BitmapDrawable(getResources(), bitmap));
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

        } else if (background != null && background instanceof Drawable && mBackgroundManager != null && mBackgroundManager.isAttached()) {
            mBackgroundManager.setDrawable((Drawable) background);
        } else if (scrollableBackground != null && scrollingBackgroundView != null) {
            scrollingBackgroundView.setImageDrawable(scrollableBackground);
        } else if (mBackgroundManager != null && mBackgroundManager.isAttached()) {
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

    public void setScrollableBackgroundDrawable(Drawable drawable, boolean hasDelay) {
        this.scrollableBackground = drawable;
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

    public void onItemLongClickListener() {
        Toast.makeText(getContext(), "LongClick!!", Toast.LENGTH_SHORT).show();
    }

    public boolean isInitBackgroundManager() {
        return isInitBackgroundManager;
    }

    public void setInitBackgroundManager(boolean initBackgroundManager) {
        isInitBackgroundManager = initBackgroundManager;
    }

    @Override
    public void onDestroyView() {
        if (mBackgroundManager != null && mBackgroundManager.isAttached())
            mBackgroundManager.setDrawable(null);
//        mBackgroundManager = null;

        super.onDestroyView();
    }
}
