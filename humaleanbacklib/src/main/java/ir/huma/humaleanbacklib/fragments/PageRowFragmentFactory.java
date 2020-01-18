package ir.huma.humaleanbacklib.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.leanback.app.BackgroundManager;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageRowFragmentFactory extends BrowseSupportFragment.FragmentFactory {
    //    private final BackgroundManager mBackgroundManager;
    private Context context;

    HashMap<Long, Fragment> fragmentHashMap = new HashMap<>();
    ArrayObjectAdapter adapter;
    FragmentProvider provider;

//    PageRowFragmentFactory(BackgroundManager backgroundManager, Context context) {
//        this.mBackgroundManager = backgroundManager;
//        this.context = context;
//    }


    public PageRowFragmentFactory(Context context, ArrayObjectAdapter adapter, FragmentProvider provider) {
        this.context = context;
        this.adapter = adapter;
        this.provider = provider;
    }

    @Override
    public Fragment createFragment(Object rowObj) {
//        mBackgroundManager.setDrawable(context.getResources().getDrawable(R.drawable.background));
        Row row = (Row) rowObj;
        try {
            for (int i = 0; i < adapter.size(); i++) {
                if (adapter.get(i) == row) {
                    provider.onItemSelectedListener(null, row, i, 0);
                }
            }
        } catch (Exception e) {

        }

        if (fragmentHashMap.containsKey(row.getHeaderItem().getId())) {
            return fragmentHashMap.get(row.getHeaderItem().getId());
        }

        return null;
    }

    public void addFragment(long id, Fragment f) {
        fragmentHashMap.put(id, f);
    }
}

