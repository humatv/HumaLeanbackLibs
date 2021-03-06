package ir.huma.humaleanbacksample;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.FocusHighlight;
import androidx.leanback.widget.VerticalGridPresenter;
import android.view.View;
import android.widget.Toast;

import ir.huma.humaleanbacklib.fragments.BaseSearchGridFragment;
import ir.huma.humaleanbacklib.presenter.BasePresenter;
import ir.huma.humaleanbacksample.model.Video;

public class TestSearchGridFragment extends BaseSearchGridFragment {
    String[] str = {"علی", "علیرضا", "محمد علی", "جواد", "رضا", "حامد", "اضغر", "حمید", "محمد رضا", "علی اکبر", "اکبر"};
    ArrayObjectAdapter adapter1;
    @Override
    public void initial() {

        VerticalGridPresenter presenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_LARGE, false);
        presenter.setShadowEnabled(false);
        presenter.setNumberOfColumns(3);
        setGridPresenter(presenter);

        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);


        adapter1 = new ArrayObjectAdapter(cardPresenter);
        cardPresenter.setAdapter(adapter1);
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
        Toast.makeText(getActivity(), "sumbit : "+query, Toast.LENGTH_SHORT).show();

        adapter1.clear();
        for (int i = 0; i < str.length; i++) {
            if (str[i].indexOf(query) != -1)
                adapter1.add(new Video(str[i]));
        }

        return false;
    }
}
