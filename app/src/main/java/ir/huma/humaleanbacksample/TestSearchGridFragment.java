package ir.huma.humaleanbacksample;

import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.view.View;
import android.widget.Toast;

import ir.huma.humaleanbacklib.fragments.BaseSearchGridFragment;
import ir.huma.humaleanbacklib.fragments.SearchGridFragment;
import ir.huma.humaleanbacklib.test.BasePresenter;
import ir.huma.humaleanbacksample.model.Video;

public class TestSearchGridFragment extends BaseSearchGridFragment {
    String[] str = {"علی", "علیرضا", "محمد علی", "جواد", "رضا", "حامد", "اضغر", "حمید", "محمد رضا", "علی اکبر", "اکبر"};
    ArrayObjectAdapter adapter1;
    @Override
    public void initial() {
//        setmRowsSupportFragment(new TestGridFragment());

        VerticalGridPresenter presenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_LARGE, false);
        presenter.setShadowEnabled(false);
        presenter.setNumberOfColumns(3);
        setGridPresenter(presenter);

        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);


        adapter1 = new ArrayObjectAdapter(cardPresenter);
//        for (int i = 0; i < 10; i++) {
//            adapter1.add(new Video());
//        }
        setAdapter(adapter1);
        startRecognition();


        setBackgroundDrawable(getResources().getDrawable(R.color.colorAccent),false);

    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        Toast.makeText(getActivity(), "change : "+newQuery, Toast.LENGTH_SHORT).show();
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
