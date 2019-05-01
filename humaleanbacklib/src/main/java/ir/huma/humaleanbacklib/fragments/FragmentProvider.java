package ir.huma.humaleanbacklib.fragments;

import android.view.View;

public interface FragmentProvider {
    void initial();

    void onItemSelectedListener(View v, Object item, int rowPos, int pos);

    void onItemClickListener(View v, Object item, int rowPos, int pos);
}
