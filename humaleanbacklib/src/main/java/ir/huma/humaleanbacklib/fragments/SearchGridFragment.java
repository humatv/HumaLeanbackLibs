package ir.huma.humaleanbacklib.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.app.PermissionHelper;
import android.support.v17.leanback.app.SearchSupportFragment;
import android.support.v17.leanback.transition.TransitionHelper;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnChildLaidOutListener;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SearchBar;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ir.huma.humaleanbacklib.R;
import ir.huma.humaleanbacklib.Util.ImageLoader;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * A fragment for rendering items in a vertical grids.
 */
public abstract class SearchGridFragment extends Fragment implements BrowseSupportFragment.MainFragmentAdapterProvider, FragmentProvider {
    private static final String TAG = "VerticalGridFragment";
    private static boolean DEBUG = false;

    private ObjectAdapter mAdapter;
    private VerticalGridPresenter mGridPresenter;
    private VerticalGridPresenter.ViewHolder mGridViewHolder;

    private Object mSceneAfterEntranceTransition;
    private int mSelectedPosition = -1;

    private static final long BACKGROUND_UPDATE_DELAY = 400;

    protected BackgroundManager mBackgroundManager;
    protected DisplayMetrics mMetrics;
    protected Timer mBackgroundTimer;

    private Object background;
    protected static final Handler HANDLER = new Handler();

    private int drawableResId = R.drawable.default_background;




    private BrowseSupportFragment.MainFragmentAdapter mMainFragmentAdapter =
            new BrowseSupportFragment.MainFragmentAdapter(this) {
                @Override
                public void setEntranceTransitionState(boolean state) {
                    SearchGridFragment.this.setEntranceTransitionState(state);
                }

            };

    /**
     * Sets the grid presenter.
     */
    public void setGridPresenter(VerticalGridPresenter gridPresenter) {
        if (gridPresenter == null) {
            throw new IllegalArgumentException("Grid presenter may not be null");
        }
        mGridPresenter = gridPresenter;

        mGridPresenter.setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

                int position = mGridViewHolder.getGridView().getSelectedPosition();
                if (DEBUG) Log.v(TAG, "grid selected position " + position);
                gridOnItemSelected(position);

                onItemSelectedListener(itemViewHolder != null ? itemViewHolder.view : null, item, 0, position);
            }
        });
        mGridPresenter.setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {

                onItemClickListener(itemViewHolder != null ? itemViewHolder.view : null, item, 0, mGridViewHolder.getGridView().getSelectedPosition());
            }
        });

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
                            if (SearchGridFragment.this.background == background) {
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

        mBackgroundTimer.schedule(new SearchGridFragment.UpdateBackgroundTask(), hasDelay ? BACKGROUND_UPDATE_DELAY : 0);

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


    /**
     * Returns the grid presenter.
     */
    public VerticalGridPresenter getGridPresenter() {
        return mGridPresenter;
    }

    /**
     * Sets the object adapter for the fragment.
     */
    public void setAdapter(ObjectAdapter adapter) {
        mAdapter = adapter;
        updateAdapter();
    }

    /**
     * Returns the object adapter.
     */
    public ObjectAdapter getAdapter() {
        return mAdapter;
    }


    final private OnChildLaidOutListener mChildLaidOutListener =
            new OnChildLaidOutListener() {
                @Override
                public void onChildLaidOut(ViewGroup parent, View view, int position, long id) {
                    if (position == 0) {
                        showOrHideTitle();
                    }
                }
            };


    private void gridOnItemSelected(int position) {
        if (position != mSelectedPosition) {
            mSelectedPosition = position;
            showOrHideTitle();
        }
        mSearchBar.setVisibility(position >= mGridPresenter.getNumberOfColumns() ? View.GONE : View.VISIBLE);
    }

    private void showOrHideTitle() {
        if (mGridViewHolder.getGridView().findViewHolderForAdapterPosition(mSelectedPosition)
                == null) {
            return;
        }
        if (mMainFragmentAdapter.getFragmentHost() != null)
            if (!mGridViewHolder.getGridView().hasPreviousViewInSameRow(mSelectedPosition)) {
                mMainFragmentAdapter.getFragmentHost().showTitleView(true);
            } else {
                mMainFragmentAdapter.getFragmentHost().showTitleView(false);
            }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.search_grid_fragment, container, false);
        createSearchView(v);
        if (savedInstanceState == null) {
            // auto start recognition if this is the first time create fragment
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSearchBar.startRecognition();
                }
            }, SPEECH_RECOGNITION_DELAY_MS);
        }
        prepareBackgroundManager();
        return v;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initial();
        ViewGroup gridDock = (ViewGroup) view.findViewById(R.id.browse_grid_dock);
        mGridViewHolder = mGridPresenter.onCreateViewHolder(gridDock);
        gridDock.addView(mGridViewHolder.view);
        mGridViewHolder.getGridView().setOnChildLaidOutListener(mChildLaidOutListener);

        mSceneAfterEntranceTransition = TransitionHelper.createScene(gridDock, new Runnable() {
            @Override
            public void run() {
                setEntranceTransitionState(true);
            }
        });

        if (mMainFragmentAdapter.getFragmentHost() != null)
            getMainFragmentAdapter().getFragmentHost().notifyViewCreated(mMainFragmentAdapter);
        updateAdapter();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initial();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mGridViewHolder = null;
    }

    @Override
    public BrowseSupportFragment.MainFragmentAdapter getMainFragmentAdapter() {
        return mMainFragmentAdapter;
    }

    /**
     * Sets the selected item position.
     */
    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        if (mGridViewHolder != null && mGridViewHolder.getGridView().getAdapter() != null) {
            mGridViewHolder.getGridView().setSelectedPositionSmooth(position);
        }
    }

    private void updateAdapter() {
        if (mGridViewHolder != null) {
            mGridPresenter.onBindViewHolder(mGridViewHolder, mAdapter);
            if (mSelectedPosition != -1) {
                mGridViewHolder.getGridView().setSelectedPosition(mSelectedPosition);
            }
        }
    }

    void setEntranceTransitionState(boolean afterTransition) {
        mGridPresenter.setEntranceTransitionState(mGridViewHolder, afterTransition);
    }




    private SearchBar.SearchBarPermissionListener mPermissionListener =
            new SearchBar.SearchBarPermissionListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void requestAudioPermission() {
                    PermissionHelper.requestPermissions(SearchGridFragment.this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
                }
            };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE && permissions.length > 0) {
            if (permissions[0].equals(Manifest.permission.RECORD_AUDIO)
                    && grantResults[0] == PERMISSION_GRANTED) {
                startRecognition();
            }
        }
    }

    private View createSearchView(View root){

//        FrameLayout searchFrame = (FrameLayout) root.findViewById(android.support.v17.leanback.R.id.lb_search_frame);
        mSearchBar = (SearchBar) root.findViewById(android.support.v17.leanback.R.id.lb_search_bar);
        mSearchBar.setSearchBarListener(new SearchBar.SearchBarListener() {
            @Override
            public void onSearchQueryChange(String query) {
//                if (DEBUG) Log.v(TAG, String.format("onSearchQueryChange %s %s", query,
//                        null == mProvider ? "(null)" : mProvider));
//                if (null != mProvider) {
                    retrieveResults(query);
//                } else {
//                    mPendingQuery = query;
//                }
            }
            @Override
            public void onSearchQuerySubmit(String query) {
                if (DEBUG) Log.v(TAG, String.format("onSearchQuerySubmit %s", query));
                queryComplete();

//                if (null != mProvider) {
                    onQueryTextSubmit(query);
//                }
            }
            @Override
            public void onKeyboardDismiss(String query) {
                if (DEBUG) Log.v(TAG, String.format("onKeyboardDismiss %s", query));
                queryComplete();
            }
        });
        mSearchBar.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        mSearchBar.setPermissionListener(mPermissionListener);
//        readArguments(getArguments());
//        if (null != mBadgeDrawable) {
//            setBadgeDrawable(mBadgeDrawable);
//        }
//        if (null != mTitle) {
//            setTitle(mTitle);
//        }
        // Inject the RowsSupportFragment in the results container
//        if (getChildFragmentManager().findFragmentById(android.support.v17.leanback.R.id.lb_results_frame) == null) {
////            mRowsSupportFragment
//            getChildFragmentManager().beginTransaction()
//                    .replace(android.support.v17.leanback.R.id.lb_results_frame,mRowsSupportFragment ).commit();
//        } else {
//            mRowsSupportFragment = (BaseGridFragment) getChildFragmentManager()
//                    .findFragmentById(android.support.v17.leanback.R.id.lb_results_frame);
//        }
//        mRowsSupportFragment.setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
//            @Override
//            public void onItemSelected(ViewHolder itemViewHolder, Object item,
//                                       RowPresenter.ViewHolder rowViewHolder, Row row) {
//                int position = mRowsSupportFragment.getVerticalGridView().getSelectedPosition();
//                if (DEBUG) Log.v(TAG, String.format("onItemSelected %d", position));
//                mSearchBar.setVisibility(0 >= position ? View.VISIBLE : View.GONE);
//                if (null != mOnItemSelectedListener) {
//                    mOnItemSelectedListener.onItemSelected(item, row);
//                }
//                if (null != mOnItemViewSelectedListener) {
//                    mOnItemViewSelectedListener.onItemSelected(itemViewHolder, item,
//                            rowViewHolder, row);
//                }
//            }
//        });
//        mRowsSupportFragment.setOnItemViewClickedListener(new OnItemViewClickedListener() {
//            @Override
//            public void onItemClicked(ViewHolder itemViewHolder, Object item,
//                                      RowPresenter.ViewHolder rowViewHolder, Row row) {
//                int position = mRowsSupportFragment.getVerticalGridView().getSelectedPosition();
//                if (DEBUG) Log.v(TAG, String.format("onItemClicked %d", position));
//                if (null != mOnItemClickedListener) {
//                    mOnItemClickedListener.onItemClicked(item, row);
//                }
//                if (null != mOnItemViewClickedListener) {
//                    mOnItemViewClickedListener.onItemClicked(itemViewHolder, item,
//                            rowViewHolder, row);
//                }
//            }
//        });
//        mRowsSupportFragment.setExpand(true);
//        if (null != mProvider) {
//            onSetSearchResultProvider();
//        }

        return root;
    }





    static class ExternalQuery {
        String mQuery;
        boolean mSubmit;

        ExternalQuery(String query, boolean submit) {
            mQuery = query;
            mSubmit = submit;
        }
    }




    static final int AUDIO_PERMISSION_REQUEST_CODE = 0;

    private static final String ARG_PREFIX = SearchGridFragment.class.getCanonicalName();
    private static final String ARG_QUERY =  ARG_PREFIX + ".query";
    private static final String ARG_TITLE = ARG_PREFIX  + ".title";
    private static final long SPEECH_RECOGNITION_DELAY_MS = 300;
    private static final String EXTRA_LEANBACK_BADGE_PRESENT = "LEANBACK_BADGE_PRESENT";

    private final int RESULTS_CHANGED = 0x1;
    private final int QUERY_COMPLETE = 0x2;
    private int mStatus;
    private SearchBar mSearchBar;
//    private SearchSupportFragment.SearchResultProvider mProvider;
    private SpeechRecognitionCallback mSpeechRecognitionCallback;
    private SpeechRecognizer mSpeechRecognizer;
    private String mPendingQuery = null;
    private String mTitle;
    private Drawable mBadgeDrawable;
    private ExternalQuery mExternalQuery;
    boolean mAutoStartRecognition = true;

    private boolean mIsPaused;
    private boolean mPendingStartRecognitionWhenPaused;

//    private final ObjectAdapter.DataObserver mAdapterObserver = new ObjectAdapter.DataObserver() {
//        @Override
//        public void onChanged() {
//            // onChanged() may be called multiple times e.g. the provider add
//            // rows to ArrayObjectAdapter one by one.
//            mHandler.removeCallbacks(mResultsChangedCallback);
//            mHandler.post(mResultsChangedCallback);
//        }
//    };
    private final Handler mHandler = new Handler();
//    private final Runnable mResultsChangedCallback = new Runnable() {
//        @Override
//        public void run() {
////            if (DEBUG) Log.v(TAG, "adapter size " + mResultAdapter.size());
////            if (mRowsSupportFragment != null
////                    && mRowsSupportFragment.getAdapter() != mResultAdapter) {
////                if (!(mRowsSupportFragment.getAdapter() == null && mResultAdapter.size() == 0)) {
////                    mRowsSupportFragment.setAdapter(mResultAdapter);
////                }
////            }
//            mStatus |= RESULTS_CHANGED;
//            if ((mStatus & QUERY_COMPLETE) != 0) {
//                focusOnResults();
//            }
////            updateSearchBarNextFocusId();
//        }
//    };
//    private final Runnable mSetSearchResultProvider = new Runnable() {
//        @Override
//        public void run() {
//            // Retrieve the result adapter
////            ObjectAdapter adapter = mProvider.getResultsAdapter();
//            if (adapter != mResultAdapter) {
//                boolean firstTime = mResultAdapter == null;
//                releaseAdapter();
//                mResultAdapter = adapter;
//                if (mResultAdapter != null) {
//                    mResultAdapter.registerObserver(mAdapterObserver);
//                }
////                if (null != mRowsSupportFragment) {
////                    // delay the first time to avoid setting a empty result adapter
////                    // until we got first onChange() from the provider
////                    if (!(firstTime && (mResultAdapter == null || mResultAdapter.size() == 0))) {
////                        mRowsSupportFragment.setAdapter(mResultAdapter);
////                    }
////                    executePendingQuery();
////                }
////                updateSearchBarNextFocusId();
//            }
//        }
//    };

    private void retrieveResults(String searchQuery) {
        if (DEBUG) Log.v(TAG, String.format("retrieveResults %s", searchQuery));
        onQueryTextChange(searchQuery);
        mStatus &= ~QUERY_COMPLETE;
    }
    private void queryComplete() {
        mStatus |= QUERY_COMPLETE;
        focusOnResults();
    }

    private void focusOnResults() {
//        if (mRowsSupportFragment == null ||
//                mRowsSupportFragment.getVerticalGridView() == null ||
//                mResultAdapter.size() == 0) {
//            return;
//        }
//        mRowsSupportFragment.setSelectedPosition(0);
//        if (mRowsSupportFragment.getVerticalGridView().requestFocus()) {
//            mStatus &= ~RESULTS_CHANGED;
//        }
    }
//    private void onSetSearchResultProvider() {
//        mHandler.removeCallbacks(mSetSearchResultProvider);
//        mHandler.post(mSetSearchResultProvider);
//    }

//    private void updateSearchBarNextFocusId() {
//        if (mSearchBar == null || mResultAdapter == null) {
//            return;
//        }
////        final int viewId = (mResultAdapter.size() == 0 || mRowsSupportFragment == null ||
////                mRowsSupportFragment.getVerticalGridView() == null) ? 0 :
////                mRowsSupportFragment.getVerticalGridView().getId();
////        mSearchBar.setNextFocusDownId(viewId);
//    }

//    private void releaseAdapter() {
//        if (mResultAdapter != null) {
//            mResultAdapter.unregisterObserver(mAdapterObserver);
//            mResultAdapter = null;
//        }
//    }
    private void executePendingQuery() {
        if (null != mPendingQuery ) {
            String query = mPendingQuery;
            mPendingQuery = null;
            retrieveResults(query);
        }
    }
//    private void readArguments(Bundle args) {
//        if (null == args) {
//            return;
//        }
//        if (args.containsKey(ARG_QUERY)) {
//            setSearchQuery(args.getString(ARG_QUERY));
//        }
//        if (args.containsKey(ARG_TITLE)) {
//            setTitle(args.getString(ARG_TITLE));
//        }
//    }
//    private void setSearchQuery(String query) {
//        mSearchBar.setSearchQuery(query);
//    }
//
//
//    public void setTitle(String title) {
//        mTitle = title;
//        if (null != mSearchBar) {
//            mSearchBar.setTitle(title);
//        }
//    }
    /**
     * Returns the title set in the search bar.
     */
//    public String getTitle() {
//        if (null != mSearchBar) {
//            return mSearchBar.getTitle();
//        }
//        return null;
//    }
//    /**
//     * Sets the badge drawable that will be shown inside the search bar next to
//     * the title.
//     */
//    public void setBadgeDrawable(Drawable drawable) {
//        mBadgeDrawable = drawable;
//        if (null != mSearchBar) {
//            mSearchBar.setBadgeDrawable(drawable);
//        }
//    }
    /**
     * Returns the badge drawable in the search bar.
     */
//    public Drawable getBadgeDrawable() {
//        if (null != mSearchBar) {
//            return mSearchBar.getBadgeDrawable();
//        }
//        return null;
//    }



    @Override
    public void onResume() {
        super.onResume();
        mIsPaused = false;
        if (mSpeechRecognitionCallback == null && null == mSpeechRecognizer) {
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(
                    getContext());
            mSearchBar.setSpeechRecognizer(mSpeechRecognizer);
        }
        if (mPendingStartRecognitionWhenPaused) {
            mPendingStartRecognitionWhenPaused = false;
            mSearchBar.startRecognition();
        } else {
            // Ensure search bar state consistency when using external recognizer
            mSearchBar.stopRecognition();
        }
    }
    @Override
    public void onPause() {
        releaseRecognizer();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        if (null != mBackgroundTimer) {
            Log.d(TAG, "destroy : " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
//        releaseAdapter();
        super.onDestroy();
    }
    private void releaseRecognizer() {
        if (null != mSpeechRecognizer) {
            mSearchBar.setSpeechRecognizer(null);
            mSpeechRecognizer.destroy();
            mSpeechRecognizer = null;
        }
    }

    /**
     * Sets the search provider that is responsible for returning results for the
     * search query.
     */
//    public void setSearchResultProvider(SearchSupportFragment.SearchResultProvider searchResultProvider) {
//        if (mProvider != searchResultProvider) {
//            mProvider = searchResultProvider;
//            onSetSearchResultProvider();
//        }
//    }

    /**
     * Sets this callback to have the fragment pass speech recognition requests
     * to the activity rather than using a SpeechRecognizer object.
     * @deprecated Launching voice recognition activity is no longer supported. App should declare
     *             android.permission.RECORD_AUDIO in AndroidManifest file.
     */

    public void setSpeechRecognitionCallback(SpeechRecognitionCallback callback) {
        mSpeechRecognitionCallback = callback;
        if (mSearchBar != null) {
            mSearchBar.setSpeechRecognitionCallback(mSpeechRecognitionCallback);
        }
        if (callback != null) {
            releaseRecognizer();
        }
    }

    public Intent getRecognizerIntent() {
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        if (mSearchBar != null && mSearchBar.getHint() != null) {
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, mSearchBar.getHint());
        }
        recognizerIntent.putExtra(EXTRA_LEANBACK_BADGE_PRESENT, mBadgeDrawable != null);
        return recognizerIntent;
    }

    /**
     * Sets the text of the search query based on the {@link RecognizerIntent#EXTRA_RESULTS} in
     * the given intent, and optionally submit the query.  If more than one result is present
     * in the results list, the first will be used.
     *
     * @param intent Intent received from a speech recognition service.
     * @param submit Whether to submit the query.
     */
    public void setSearchQuery(Intent intent, boolean submit) {
        ArrayList<String> matches = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (matches != null && matches.size() > 0) {
            setSearchQuery(matches.get(0), submit);
        }
    }

    /**
     * Sets the text of the search query and optionally submits the query. Either
     * {@link SearchSupportFragment.SearchResultProvider#onQueryTextChange onQueryTextChange} or
     * {@link SearchSupportFragment.SearchResultProvider#onQueryTextSubmit onQueryTextSubmit} will be
     * called on the provider if it is set.
     *
     * @param query The search query to set.
     * @param submit Whether to submit the query.
     */
    public void setSearchQuery(String query, boolean submit) {
        if (DEBUG) Log.v(TAG, "setSearchQuery " + query + " submit " + submit);
        if (query == null) {
            return;
        }
        mExternalQuery = new ExternalQuery(query, submit);
        applyExternalQuery();
        if (mAutoStartRecognition) {
            mAutoStartRecognition = false;
            mHandler.removeCallbacks(mStartRecognitionRunnable);
        }
    }

    final Runnable mStartRecognitionRunnable = new Runnable() {
        @Override
        public void run() {
            mAutoStartRecognition = false;
            mSearchBar.startRecognition();
        }
    };


    /**
     * Starts speech recognition.  Typical use case is that
     * activity receives onNewIntent() call when user clicks a MIC button.
     * Note that SearchSupportFragment automatically starts speech recognition
     * at first time created, there is no need to call startRecognition()
     * when fragment is created.
     */
    public void startRecognition() {
        if (mIsPaused) {
            mPendingStartRecognitionWhenPaused = true;
        } else {
            mSearchBar.startRecognition();
        }
    }

    private void applyExternalQuery() {
        if (mExternalQuery == null || mSearchBar == null) {
            return;
        }
        mSearchBar.setSearchQuery(mExternalQuery.mQuery);
        if (mExternalQuery.mSubmit) {
            submitQuery(mExternalQuery.mQuery);
        }
        mExternalQuery = null;
    }

    void submitQuery(String query) {
        queryComplete();
        onQueryTextSubmit(query);
//        if (null != mProvider) {
//            mProvider.onQueryTextSubmit(query);
//        }
    }

    public abstract boolean onQueryTextSubmit(String query);
    public abstract boolean onQueryTextChange(String query);
}
