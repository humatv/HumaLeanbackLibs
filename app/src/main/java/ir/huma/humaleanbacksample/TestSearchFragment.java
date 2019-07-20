package ir.huma.humaleanbacksample;

import android.graphics.Typeface;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.view.View;

import ir.huma.humaleanbacklib.fragments.BaseSearchFragment;
import ir.huma.humaleanbacklib.presenter.BasePresenter;
import ir.huma.humaleanbacklib.presenter.IconHeaderItem;
import ir.huma.humaleanbacklib.presenter.MyListRowPresenter;
import ir.huma.humaleanbacksample.model.Video;

public class TestSearchFragment extends BaseSearchFragment {
    String[] str = {"علی", "علیرضا", "محمد علی", "جواد", "رضا", "حامد", "اضغر", "حمید", "محمد رضا", "علی اکبر", "اکبر"};
    ArrayObjectAdapter adapter1;

    @Override
    public void initial() {

        MyListRowPresenter p = new MyListRowPresenter();
        setAdapter(new ArrayObjectAdapter(p));
        setRtl();

        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);
        adapter1 = new ArrayObjectAdapter(cardPresenter);
        IconHeaderItem headerItem = new IconHeaderItem("نام ها");
        getAdapter().add(new ListRow(headerItem, adapter1));

        setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/IRANSans.ttf"));

        startRecognition();

    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public boolean onQueryTextChange(String newQuery) {

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter1.clear();
        for (int i = 0; i < str.length; i++) {
            if (str[i].indexOf(query) != -1)
                adapter1.add(new Video(str[i]));
        }

        return false;
    }
}
