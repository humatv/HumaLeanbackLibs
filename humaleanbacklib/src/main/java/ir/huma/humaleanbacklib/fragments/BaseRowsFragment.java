package ir.huma.humaleanbacklib.fragments;

import android.os.Bundle;
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.BaseOnItemViewClickedListener;
import androidx.leanback.widget.BaseOnItemViewSelectedListener;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowPresenter;

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
