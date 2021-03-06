package ir.huma.humaleanbacklib.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.SearchSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SearchBar;
import androidx.leanback.widget.SearchEditText;
import androidx.leanback.widget.SpeechRecognitionCallback;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import ir.atitec.everythingmanager.utility.Util;
import ir.huma.humaleanbacklib.R;
import ir.huma.humaleanbacklib.Util.ImageLoader;

public abstract class BaseSearchFragment extends SearchSupportFragment implements SearchSupportFragment.SearchResultProvider, FragmentProvider, OnBackPressListener {

    private boolean isPersianVoice = true;
    public final int VOICE_REQUEST_CODE = 342;
    public final int VOICE_REQUEST_PERMISSION = 653;
    private SearchBar searchBar;
    public boolean isKeyboardOpen = false;
    private ArrayObjectAdapter adapter;
    private Typeface typeface;
    private SearchEditText editText;
    private long createTime = System.currentTimeMillis();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customizeSearchView();
        setSearchResultProvider(this);
        startVoice();
        initial();
        setEventListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(receiver, new IntentFilter("ir.huma.action.newVoiceSearch"));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (!searchBar.isRecognizing()) {
            startRecognition();
//                Log.e("search", "newVoice!!");
//            }

        }
    };

    private void setEventListener() {
        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                try {
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
                } catch (Exception e) {

                }
            }
        });

        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                try {
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
                } catch (Exception e) {

                }
            }
        });
    }

    private void startVoice() {
        if (hasVoicePermission()) {

            setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
                @Override
                public void recognizeSpeech() {

                    try {
                        Intent in = getRecognizerIntent();

                        if (isPersianVoice) {
                            in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "fa-IR");
                            in.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{"fa"});
                            in.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR");
                            in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "fa");
                            in.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.lb_search_bar_hint));
                        }
                        startActivityForResult(in, VOICE_REQUEST_CODE);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            requestVoicePermission();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                setSearchQuery(data, true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == VOICE_REQUEST_PERMISSION && hasVoicePermission()) {
            startVoice();
        }
    }

    public boolean hasVoicePermission() {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestVoicePermission() {
        if (!hasVoicePermission())
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_PERMISSION);
    }


    private void customizeSearchView() {

        try {
            Field f0 = SearchSupportFragment.class.getDeclaredField("mAutoStartRecognition");
            f0.setAccessible(true);
            f0.set(this, false);

            Field f1 = SearchSupportFragment.class.getDeclaredField("mSearchBar");
            f1.setAccessible(true);
            searchBar = (SearchBar) f1.get(this);
            if (searchBar != null) {
                //searchBar.setSpeechRecognizer(null);

                Field f2 = searchBar.getClass().getDeclaredField("mSearchTextEditor");
                f2.setAccessible(true);
                editText = (SearchEditText) f2.get(searchBar);
                if (isPersianVoice) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//                        ((RelativeLayout.LayoutParams)editText.getLayoutParams()).setMargins(0,0,Util.dpToPx(getActivity(), 30),0);
                            editText.setPadding(0, 0, 10, 0);
//                        searchBar.stopRecognition();
                            if (typeface != null) {
                                editText.setTypeface(typeface);
                            }
                        }
                    }, 200);
                }

                final View.OnFocusChangeListener changeListener = editText.getOnFocusChangeListener();
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        changeListener.onFocusChange(v, hasFocus);
                        if (hasFocus) {
                            isKeyboardOpen = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    editText.setSelection(editText.getText().length());
                                }
                            }, 100);
                        } else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isKeyboardOpen = false;
                                }
                            }, 200);
                        }
                    }
                });

//                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                    @Override
//                    public boolean onEditorAction(TextView v, int action, KeyEvent event) {
//                        if (EditorInfo.IME_ACTION_DONE == action ||
//                                EditorInfo.IME_ACTION_GO == action) {
//                            Util.hideKeyboard(getActivity());
//                            onQueryTextSubmit(editText.getText().toString());
//                            return true;
//                        }
//                        return false;
//                    }
//                });


                editText.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                getActivity().finish();
                                return true;
                            } else if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                                //editText.requestFocus();
                                return true;
                            } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                                startRecognition();
                            }
                        }
                        return false;
                    }
                });

            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    public boolean isPersianVoice() {
        return isPersianVoice;
    }

    public void setPersianVoice(boolean persianVoice) {
        isPersianVoice = persianVoice;
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return adapter;
    }

    public ArrayObjectAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayObjectAdapter adapter) {
        this.adapter = adapter;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public SearchEditText getEditText() {
        return editText;
    }

    @Override
    public boolean onBackPress() {
        if (isKeyboardOpen) {
            Util.hideKeyboard(getActivity());
            return true;
        }
        return false;
    }


    private static final long BACKGROUND_UPDATE_DELAY = 400;

    protected BackgroundManager mBackgroundManager;
    protected DisplayMetrics mMetrics;
    protected Timer mBackgroundTimer;

    private Object background;
    protected static final Handler HANDLER = new Handler();

    private int drawableResId = R.drawable.default_background;


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
                            if (BaseSearchFragment.this.background == background) {
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

        mBackgroundTimer.schedule(new BaseSearchFragment.UpdateBackgroundTask(), hasDelay ? BACKGROUND_UPDATE_DELAY : 0);

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


    public void setRtl() {
        getView().findViewById(R.id.lb_results_frame).setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startRecognition() {
        if (!searchBar.isRecognizing())
            super.startRecognition();
    }

    @Override
    public void onResume() {
        try {
            Field f = SearchSupportFragment.class.getDeclaredField("mPendingStartRecognitionWhenPaused");
            f.setAccessible(true);
            f.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onResume();

    }
}
