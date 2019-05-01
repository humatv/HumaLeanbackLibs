package ir.huma.humaleanbacklib.fragments;

import android.os.Bundle;
import android.support.v17.leanback.app.RowsSupportFragment;
import android.support.v17.leanback.widget.BaseOnItemViewClickedListener;
import android.support.v17.leanback.widget.BaseOnItemViewSelectedListener;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowPresenter;

public abstract class BaseRowsFragment extends RowsSupportFragment implements FragmentProvider {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initial();
        setOnItemViewSelectedListener(new BaseOnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Object row) {
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
            }
        });

        setOnItemViewClickedListener(new BaseOnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Object row) {
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
}
