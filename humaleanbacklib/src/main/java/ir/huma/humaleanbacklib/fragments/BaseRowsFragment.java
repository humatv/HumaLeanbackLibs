package ir.huma.humaleanbacklib.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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

    private ProgressBar progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        RelativeLayout f = new RelativeLayout(getContext());
        f.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        f.addView(v);
        f.addView(getProgress());
        return f;
    }

    public ProgressBar getProgress(){
        if(progress == null ){
            progress = new ProgressBar(getContext());
            progress.setVisibility(View.GONE);
            RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            progressParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
            progress.setLayoutParams(progressParams);
        }
        return progress;
    }


}
