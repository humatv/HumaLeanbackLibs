package ir.huma.humaleanbacklib.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.widget.Row;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageRowFragmentFactory extends BrowseSupportFragment.FragmentFactory {
//    private final BackgroundManager mBackgroundManager;
    private Context context;

    HashMap<Long, Fragment> fragmentHashMap = new HashMap<>();


//    PageRowFragmentFactory(BackgroundManager backgroundManager, Context context) {
//        this.mBackgroundManager = backgroundManager;
//        this.context = context;
//    }


    public PageRowFragmentFactory(Context context) {
        this.context = context;
    }

    @Override
    public Fragment createFragment(Object rowObj) {
//        mBackgroundManager.setDrawable(context.getResources().getDrawable(R.drawable.background));
        Row row = (Row) rowObj;

        if (fragmentHashMap.containsKey(row.getHeaderItem().getId())) {
            return fragmentHashMap.get(row.getHeaderItem().getId());
        }

        return null;
    }

    public void addFragment(long id, Fragment f) {
        fragmentHashMap.put(id, f);
    }
}

