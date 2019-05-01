package ir.huma.humaleanbacksample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.view.View;

import ir.huma.humaleanbacklib.fragments.BaseGridFragment;
import ir.huma.humaleanbacklib.test.BasePresenter;
import ir.huma.humaleanbacksample.model.Video;

public class TestGridFragment extends BaseGridFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VerticalGridPresenter presenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_LARGE, false);
        presenter.setShadowEnabled(false);
        presenter.setNumberOfColumns(3);
        setGridPresenter(presenter);

        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);


        ArrayObjectAdapter adapter1 = new ArrayObjectAdapter(cardPresenter);
        for (int i = 0; i < 10; i++) {
            adapter1.add(new Video());
        }
        setAdapter(adapter1);

    }

    @Override
    public void initial() {

    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {
//        Toast.makeText(getContext(), "select : "+pos , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {
//        Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
    }
}