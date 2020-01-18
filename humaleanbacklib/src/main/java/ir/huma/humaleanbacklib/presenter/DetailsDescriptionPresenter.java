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
 *
 */

package ir.huma.humaleanbacklib.presenter;

import android.content.Context;
import androidx.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailsDescriptionPresenter extends Presenter {

    //    private ResourceCache mResourceCache = new ResourceCache();
    private Context mContext;
    public View view;
    private int layoutResId;
    private OnViewReady onViewReady;

    public DetailsDescriptionPresenter(Context context, int layoutResId, OnViewReady onViewReady) {
        mContext = context;
        this.layoutResId = layoutResId;
        this.onViewReady = onViewReady;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        view = LayoutInflater.from(mContext).inflate(layoutResId, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object obj) {
        onViewReady.onReady(viewHolder != null ? viewHolder.view : null, obj);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to do here.
    }


    public interface OnViewReady<T> {
        void onReady(View v, T o);
    }
}
