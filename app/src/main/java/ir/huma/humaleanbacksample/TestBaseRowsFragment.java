package ir.huma.humaleanbacksample;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRow;
import android.util.Log;
import android.view.View;

import ir.huma.humaleanbacklib.fragments.BaseRowsFragment;
import ir.huma.humaleanbacklib.presenter.BasePresenter;
import ir.huma.humaleanbacklib.presenter.IconHeaderItem;
import ir.huma.humaleanbacklib.presenter.MyListRowPresenter;
import ir.huma.humaleanbacksample.model.Video;

public class TestBaseRowsFragment extends BaseRowsFragment {
    ArrayObjectAdapter mRowsAdapter;


    @Override
    public void initial() {
        MyListRowPresenter p = new MyListRowPresenter();
//        p.setHorizontalNumRows(3);
//        p.setRtl(true);
//        p.setShadowEnabled(true);
        mRowsAdapter = new ArrayObjectAdapter(p);
        setAlignment(100);

        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<Video, Video.MyVideoView>(getActivity(), Video.MyVideoView.class, R.layout.item_video);
        ArrayObjectAdapter adapter1 = new ArrayObjectAdapter(cardPresenter);
        cardPresenter.setAdapter(adapter1);
        for (int i = 0; i < 10; i++) {
            adapter1.add(new Video());
        }
        IconHeaderItem headerItem = new IconHeaderItem("hello");
//        headerItem.setTypeface(FontManager.instance().getTypeface());
        mRowsAdapter.add(new ListRow(headerItem, adapter1));
        mRowsAdapter.add(new ListRow(headerItem, adapter1));

        setAdapter(mRowsAdapter);

    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {
        Log.d("TestBaseBrowseFragment", "selected= " + rowPos + " : " + pos);
    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {

    }
}
