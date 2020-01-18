/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package ir.huma.humaleanbacklib.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.transition.TransitionHelper;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.OnChildLaidOutListener;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import ir.huma.humaleanbacklib.R;
import ir.huma.humaleanbacklib.Util.MyBackgroundManager;

/**
 * A fragment for rendering items in a vertical grids.
 */
public abstract class BaseGridFragment extends Fragment implements BrowseSupportFragment.MainFragmentAdapterProvider, FragmentProvider {
    private static final String TAG = "VerticalGridFragment";
    private static boolean DEBUG = false;
    private ImageView backgroundImageView;
    private ObjectAdapter mAdapter;
    private VerticalGridPresenter mGridPresenter;
    private VerticalGridPresenter.ViewHolder mGridViewHolder;
    private Object mSceneAfterEntranceTransition;
    private FrameLayout frameLayout;
    private int mSelectedPosition = -1;
    MyBackgroundManager backgroundManager;

    private BrowseSupportFragment.MainFragmentAdapter mMainFragmentAdapter =
            new BrowseSupportFragment.MainFragmentAdapter(this) {
                @Override
                public void setEntranceTransitionState(boolean state) {
                    BaseGridFragment.this.setEntranceTransitionState(state);
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
    }

    private void showOrHideTitle() {
        if (mGridViewHolder.getGridView().findViewHolderForAdapterPosition(mSelectedPosition)
                == null) {
            return;
        }

        //if (mMainFragmentAdapter.getFragmentHost() != null)
        if (!mGridViewHolder.getGridView().hasPreviousViewInSameRow(mSelectedPosition)) {
            frameLayout.setVisibility(View.VISIBLE);
            if (mMainFragmentAdapter.getFragmentHost() != null)
                mMainFragmentAdapter.getFragmentHost().showTitleView(true);
        } else {
            frameLayout.setVisibility(View.GONE);
            if (mMainFragmentAdapter.getFragmentHost() != null)
                mMainFragmentAdapter.getFragmentHost().showTitleView(false);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grid_fragment, container, false);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup gridDock = (ViewGroup) view.findViewById(R.id.browse_grid_dock);
        frameLayout = view.findViewById(R.id.frameLayout);
        backgroundImageView = view.findViewById(R.id.backgroundImageView);
        backgroundManager = new MyBackgroundManager(getContext(), backgroundImageView);
        initial();

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

    public View setTitleView(int layoutResId) {
        return LayoutInflater.from(getContext()).inflate(layoutResId, frameLayout, true);
    }

    public void setBackground(Drawable drawable) {
        backgroundManager.setBackground(drawable, true);
    }

    public void setBackground(Bitmap bitmap) {
        backgroundManager.setBackground(bitmap, true);
    }

    public void setBackground(int resourceId) {
        backgroundManager.setBackground(resourceId, true);
    }

    public void setBackground(String url) {
        backgroundManager.setBackground(url, true);
    }

}
