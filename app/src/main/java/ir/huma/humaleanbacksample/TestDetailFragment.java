package ir.huma.humaleanbacksample;

import android.graphics.Color;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.DetailsOverviewRow;
import android.support.v17.leanback.widget.ListRow;
import android.view.View;
import android.widget.Toast;

import ir.huma.humaleanbacklib.fragments.BaseDetailFragment;
import ir.huma.humaleanbacklib.presenter.BasePresenter;
import ir.huma.humaleanbacklib.presenter.DetailsDescriptionPresenter;
import ir.huma.humaleanbacklib.presenter.IconHeaderItem;
import ir.huma.humaleanbacksample.model.Video;

public class TestDetailFragment extends BaseDetailFragment {
    @Override
    public void initial() {
//        addAction(new Action(1, "hello"));
        setLogoDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
//        setBackgrouundDrawable(getResources().getDrawable(R.drawable.test));
        //setBackgroundColor(Color.YELLOW);
//        setActionColor(Color.TRANSPARENT);
//        setBackgroundColor(Color.TRANSPARENT);
//        setDetailsColor(Color.TRANSPARENT);
        setBackgroundResId(R.drawable.test);
        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);

        ArrayObjectAdapter adapter1 = new ArrayObjectAdapter(cardPresenter);
        for (int i = 0; i < 10; i++) {
            adapter1.add(new Video());
        }
        IconHeaderItem headerItem = new IconHeaderItem("hello");
//        headerItem.setTypeface(FontManager.instance().getTypeface());
        getAdapter().add(new ListRow(headerItem, adapter1));
        getAdapter().notifyItemRangeChanged(getAdapter().size()-1,1);
    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {

    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {
        Toast.makeText(getContext(), "click!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public DetailsOverviewRow getDetailsOverview() {
        setDetailsView(R.layout.detail_view_content, new DetailsDescriptionPresenter.OnViewReady<Video>() {
            @Override
            public void onReady(View v, Video o) {
//                v.requestFocus();
            }
        });
        return new DetailsOverviewRow(new Video("+18"));
    }


}
