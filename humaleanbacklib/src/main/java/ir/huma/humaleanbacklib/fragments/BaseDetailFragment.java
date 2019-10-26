package ir.huma.humaleanbacklib.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.DetailsSupportFragment;
import android.support.v17.leanback.app.DetailsSupportFragmentBackgroundController;
import android.support.v17.leanback.media.MediaPlayerGlue;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BaseOnItemViewClickedListener;
import android.support.v17.leanback.widget.BaseOnItemViewSelectedListener;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import android.support.v17.leanback.widget.FullWidthDetailsOverviewSharedElementHelper;
import android.support.v17.leanback.widget.ItemAlignmentFacet;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import ir.atitec.everythingmanager.manager.FontManager;
import ir.huma.humaleanbacklib.R;
import ir.huma.humaleanbacklib.Util.ImageLoader;
import ir.huma.humaleanbacklib.presenter.DetailsDescriptionPresenter;
import ir.huma.humaleanbacklib.presenter.MyListRowPresenter;
import ir.huma.exoplayerlib.ExoMediaPlayerWithGlue;
import ir.huma.exoplayerlib.ExoPlayerAdapter;
import ir.huma.exoplayerlib.VideoData;

public abstract class BaseDetailFragment extends DetailsSupportFragment implements FragmentProvider {

    public static final String TRANSITION_NAME = "t_for_transition";
    private ClassPresenterSelector rowPresenterSelector;
    private String quality;
    private boolean isRowMode;
    private ExoMediaPlayerWithGlue mMediaPlayerGlue;

    private DetailsDescriptionPresenter detailsDescriptionPresenter;
    private DetailsOverviewRow detailsOverview;

    private DetailsSupportFragmentBackgroundController mDetailsBackground;
    private VideoData[] videoData;

    private Object backgroundImage;
    private Object logoImage;

    private int actionColor = -1;
    private int detailsColor = -1;
    private int backgroundColor = -1;
    private int marginBottom = 210;

    private ArrayObjectAdapter actionAdapter = new ArrayObjectAdapter();

    ArrayObjectAdapter mRowsAdapter;
    private BackgroundManager mBackgroundManager;
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    FullWidthDetailsOverviewRowPresenter fullWidthDetailsOverviewRowPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUI();
        initial();
        setupEventListeners();


        //mBackgroundManager.setDrawable(mDefaultBackground);
    }

    private void setupUI() {
        detailsOverview = getDetailsOverview();
        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
        fullWidthDetailsOverviewRowPresenter = new FullWidthDetailsOverviewRowPresenter(
                detailsDescriptionPresenter) {

            @Override
            protected void onLayoutLogo(ViewHolder viewHolder, int oldState, boolean logoChanged) {
                final View v = viewHolder.getLogoViewHolder().view;
                ViewGroup.MarginLayoutParams m1 = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                int y1 = m1.topMargin;

                super.onLayoutLogo(viewHolder, oldState, logoChanged);
                if (y1 != 0) {
                    final ViewGroup.MarginLayoutParams m2 = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
                    final int y2 = m2.topMargin;
                    m2.topMargin = y1;
                    v.setLayoutParams(m2);
                    v.animate().translationY(y2 - y1).setDuration(200).start();
                }
            }

            @Override
            protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
                // Customize Actionbar and Content by using custom colors.
                final RowPresenter.ViewHolder viewHolder = super.createRowViewHolder(parent);

                if (actionColor != -1) {
                    View actionsView = viewHolder.view.
                            findViewById(R.id.details_overview_actions_background);
                    actionsView.setBackgroundColor(actionColor);
                }
//                getActivity().getResources().getColor(R.color.fastlane_background)


                final View detailsView = viewHolder.view.findViewById(R.id.details_frame);
                if (detailsColor != -1)
                    detailsView.setBackgroundColor(detailsColor);
//                getResources().getColor(R.color.fastlane_background_light)
                detailsView.getLayoutParams().height -= marginBottom;

                if (backgroundColor == detailsColor && backgroundColor != -1)
                    detailsView.setElevation(0);


                return viewHolder;
            }
        };

//        rowPresenter.setInitialState(FullWidthDetailsOverviewRowPresenter.STATE_HALF);
//        FullWidthDetailsOverviewSharedElementHelper mHelper = new FullWidthDetailsOverviewSharedElementHelper();
//        mHelper.setSharedElementEnterTransition(getActivity(), TRANSITION_NAME);
//        rowPresenter.setListener(mHelper);


        fullWidthDetailsOverviewRowPresenter.setParticipatingEntranceTransition(false);
        prepareEntranceTransition();


        rowPresenterSelector = new ClassPresenterSelector();
        rowPresenterSelector.addClassPresenter(ListRow.class, new MyListRowPresenter());


        rowPresenterSelector.addClassPresenter(DetailsOverviewRow.class, fullWidthDetailsOverviewRowPresenter);
        rowPresenterSelector.addClassPresenter(ListRow.class, new MyListRowPresenter());

        mRowsAdapter = new ArrayObjectAdapter(rowPresenterSelector);


//        detailsOverview.setActionsAdapter(actionAdapter);
        mRowsAdapter.add(detailsOverview);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initializeBackground();
                startEntranceTransition();

            }
        }, 100);
        setAdapter(mRowsAdapter);
//        ItemAlignmentFacet it = ((ItemAlignmentFacet)rowPresenter.getFacet(ItemAlignmentFacet.class));
//        it.setAlignmentDefs(new ItemAlignmentFacet.ItemAlignmentDef[]{it.getAlignmentDefs()[1]});
    }


    public void removeTop() {
        fullWidthDetailsOverviewRowPresenter.setFacet(ItemAlignmentFacet.class, new ItemAlignmentFacet());
    }

    private void initializeBackground() {


        if (logoImage != null) {
            if (logoImage instanceof String) {
                new ImageLoader().setReadyListener(new ImageLoader.ReadyListener() {
                    @Override
                    public void onReady(Bitmap bitmap) {
                        detailsOverview.setImageBitmap(getContext(), bitmap);

                    }
                }).load(getContext(), (String) logoImage);
            } else if (logoImage instanceof Bitmap) {
                detailsOverview.setImageBitmap(getContext(), (Bitmap) logoImage);
            } else if (logoImage instanceof Drawable) {
                detailsOverview.setImageDrawable((Drawable) logoImage);
            }
        }
        if (videoData != null && videoData.length > 0) {

            mDetailsBackground = new DetailsSupportFragmentBackgroundController(this);

            if (backgroundColor != -1) {
                mDetailsBackground.setSolidColor(backgroundColor);
                mDetailsBackground.enableParallax();
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDetailsBackground.switchToRows();
                    @SuppressLint("RestrictedApi") MediaPlayerGlue playerGlue = new MediaPlayerGlue(getActivity());
                    mDetailsBackground.setupVideoPlayback(playerGlue);

//

                    mMediaPlayerGlue = new ExoMediaPlayerWithGlue(new ExoPlayerAdapter(getActivity()), null, quality, getVideoData());
                    mMediaPlayerGlue.setQualityChange(new ExoMediaPlayerWithGlue.OnQualityChange() {
                        @Override
                        public void onQuality(int index, String quality) {
//                            AppController.pref.edit().putString("defaultQuality", quality).commit();
                        }
                    });
                    mMediaPlayerGlue.setTypeface(FontManager.instance(R.string.IRANSans2).getTypeface());
                    mDetailsBackground.setupVideoPlayback(mMediaPlayerGlue);
                    mMediaPlayerGlue.build();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mDetailsBackground.switchToVideo();
                            isRowMode = false;
                        }
                    }, 200);


                }
            }, 1000);


        } else if (backgroundImage != null) {

            if (backgroundImage instanceof String) {
                new ImageLoader().setReadyListener(new ImageLoader.ReadyListener() {
                    @Override
                    public void onReady(Bitmap bitmap) {
                        mBackgroundManager.setBitmap(bitmap);
//                        mDetailsBackground.setCoverBitmap(bitmap);
                    }
                }).load(getContext(), (String) backgroundImage);
            } else if (backgroundImage instanceof Bitmap) {
                mBackgroundManager.setBitmap((Bitmap) backgroundImage);

//                mDetailsBackground.setCoverBitmap((Bitmap) backgroundImage);
            } else if (backgroundImage instanceof Drawable) {
                mBackgroundManager.setBitmap(drawableToBitmap((Drawable) backgroundImage));
//                mDetailsBackground.setCoverBitmap(drawableToBitmap((Drawable) backgroundImage));
            } else if(backgroundImage instanceof Integer){

                RequestOptions options = new RequestOptions()
                        .centerCrop();
                Glide.with(this)
                        .asBitmap()
                        .load(backgroundImage)
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>(mMetrics.widthPixels, mMetrics.heightPixels) {
                            @Override
                            public void onResourceReady(
                                    Bitmap resource,
                                    Transition<? super Bitmap> transition) {
                                mBackgroundManager.setBitmap(resource);
                            }
                        });

            }

        }

    }

    private void setupEventListeners() {
        setOnItemViewSelectedListener(new BaseOnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Object row) {
                onItemSelectedListener(itemViewHolder != null ? itemViewHolder.view : null, item, 0, 0);
            }
        });
        setOnItemViewClickedListener(new BaseOnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Object row) {
                onItemClickListener(itemViewHolder != null ? itemViewHolder.view : null, item, 0, 0);
            }
        });
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public int addAction(Action action) {
        actionAdapter.add(action);
        return actionAdapter.size() - 1;
    }

    public void replaceAction(int pos, Action action) {
        actionAdapter.replace(pos, action);
    }

    public void updateAction(int pos) {
        actionAdapter.notifyItemRangeChanged(pos, 1);
    }

    public ArrayObjectAdapter getActionAdapter() {
        return actionAdapter;
    }

    public VideoData[] getVideoData() {
        return videoData;
    }

    public void setVideoData(VideoData... videoData) {
        this.videoData = videoData;
    }


    public abstract DetailsOverviewRow getDetailsOverview();


    public void setBackgrouundUri(Uri backgroundImage) {
        this.backgroundImage = backgroundImage.toString();
    }

    public void setBackgroundResId(int backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void setBackgrouundDrawable(Drawable backgroundImage) {
        this.backgroundImage = backgroundImage;
        if (mDetailsBackground != null) {
            mDetailsBackground.setCoverBitmap(drawableToBitmap((Drawable) backgroundImage));
        }
    }

    public void setBackgrouundBitmap(Bitmap backgroundImage) {
        this.backgroundImage = backgroundImage;
        if (mDetailsBackground != null) {
            mDetailsBackground.setCoverBitmap((Bitmap) backgroundImage);
        }
    }

    public void setLogoUri(Uri logoImage) {
        this.logoImage = logoImage.toString();
    }

    public void setLogoBitmap(Bitmap logoImage) {
        this.logoImage = logoImage;
    }

    public void setLogoDrawable(Drawable logoImage) {
        this.logoImage = logoImage;
    }


    public int getActionColor() {
        return actionColor;
    }

    public void setActionColor(int actionColor) {
        this.actionColor = actionColor;
    }

    public int getDetailsColor() {
        return detailsColor;
    }

    public void setDetailsColor(int detailsColor) {
        this.detailsColor = detailsColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public void setDetailsView(int layoutResId, DetailsDescriptionPresenter.OnViewReady onViewReady) {
        detailsDescriptionPresenter = new DetailsDescriptionPresenter(getContext(), layoutResId, onViewReady);
    }

    private Drawable resize(Bitmap b, DisplayMetrics metrics) {
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, metrics.widthPixels + 180, metrics.heightPixels, false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }


    @Override
    public ArrayObjectAdapter getAdapter() {
        return mRowsAdapter;
    }
}
