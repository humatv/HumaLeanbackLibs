package ir.huma.humaleanbacklib.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.lang.reflect.Field;

import ir.atitec.everythingmanager.utility.Util;
import ir.huma.humaleanbacklib.R;

public abstract class BaseSearchGridFragment extends SearchGridFragment implements OnBackPressListener {

    private boolean isPersianVoice = true;
    public final int VOICE_REQUEST_CODE = 342;
    public final int VOICE_REQUEST_PERMISSION = 653;
    private SearchBar searchBar;
    public boolean isKeyboardOpen = false;
    //    private ArrayObjectAdapter adapter;
    private Typeface typeface;
    private SearchEditText editText;
    private long createTime = System.currentTimeMillis();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customizeSearchView();
//        setSearchResultProvider(this);
        startVoice();
        setEventListener();
        //initial();
        getActivity().registerReceiver(receiver, new IntentFilter("ir.huma.launcher.newVoiceSearch"));
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startRecognition();
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setEventListener() {
//        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
//            @Override
//            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
//                int rowPos = 0;
//                for (int i = 0; i < getAdapter().size(); i++) {
//                    if (row == getAdapter().get(i)) {
//                        rowPos = i;
//                        break;
//                    }
//                }
//                int pos = 0;
//                if (row instanceof ListRow) {
//                    for (int i = 0; i < ((ListRow) row).getAdapter().size(); i++) {
//                        if (((ListRow) row).getAdapter().get(i) == item) {
//                            pos = i;
//                            break;
//                        }
//                    }
//                }
//                onItemSelectedListener(itemViewHolder != null ? itemViewHolder.view : null, item, rowPos, pos);
//            }
//        });
//
//        setOnItemViewClickedListener(new OnItemViewClickedListener() {
//            @Override
//            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
//                int rowPos = 0;
//                for (int i = 0; i < getAdapter().size(); i++) {
//                    if (row == getAdapter().get(i)) {
//                        rowPos = i;
//                        break;
//                    }
//                }
//                int pos = 0;
//                if (row instanceof ListRow) {
//                    for (int i = 0; i < ((ListRow) row).getAdapter().size(); i++) {
//                        if (((ListRow) row).getAdapter().get(i) == item) {
//                            pos = i;
//                            break;
//                        }
//                    }
//                }
//                onItemClickListener(itemViewHolder != null ? itemViewHolder.view : null, item, rowPos, pos);
//            }
//        });
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
            Field f0 = SearchGridFragment.class.getDeclaredField("mAutoStartRecognition");
            f0.setAccessible(true);
            f0.set(this, false);

            Field f1 = SearchGridFragment.class.getDeclaredField("mSearchBar");
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
//                                    onQueryTextSubmit(editText.getText().toString());
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
//                            submitQuery(editText.getText().toString());
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

//    @Override
//    public ObjectAdapter getResultsAdapter() {
//        return adapter;
//    }

//    public ArrayObjectAdapter getAdapter() {
//        return adapter;
//    }
//
//    public void setAdapter(ArrayObjectAdapter adapter) {
//        this.adapter = adapter;
//    }

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


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
