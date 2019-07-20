package ir.huma.humaleanbacksample;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ir.huma.humaleanbacklib.Util.CustomTitleView;
import ir.huma.humaleanbacklib.fragments.BaseBrowseFragment;
import ir.huma.humaleanbacklib.presenter.BasePresenter;
import ir.huma.humaleanbacklib.presenter.IconHeaderItem;
import ir.huma.humaleanbacklib.presenter.MyListRowPresenter;
import ir.huma.humaleanbacksample.model.Video;

public class TestBaseBrowseFragment extends BaseBrowseFragment {
        ArrayObjectAdapter mRowsAdapter;


    @Override
    public void initial() {
        setShowHeader(true);

        // set fastLane (or headers) background color
        setBrandColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));

        // set search icon color
        setSearchAffordanceColor(Color.TRANSPARENT);

//
        MyListRowPresenter p = new MyListRowPresenter();

        p.setRtl(true);
//        p.setShadowEnabled(true);
        mRowsAdapter = new ArrayObjectAdapter(p);


        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);

        ArrayObjectAdapter adapter1 = new ArrayObjectAdapter(cardPresenter);
        for (int i = 0; i < 10; i++) {
            adapter1.add(new Video());
        }
        IconHeaderItem headerItem = new IconHeaderItem("hello");
        headerItem.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/IRANSans.ttf"));
        mRowsAdapter.add(new ListRow(headerItem, adapter1));
        mRowsAdapter.add(new ListRow(headerItem, adapter1));
        mRowsAdapter.add(new ListRow(headerItem, adapter1));

        setAdapter(mRowsAdapter);

        setTitleView(R.layout.custom_titleview, R.id.search_orb, new CustomTitleView.OnTitleReadyListener() {
            @Override
            public void onReady(View v) {

            }
        });


        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
            }
        });
//        setBackgroundUri("https://bayanbox.ir/view/4386502884462510503/%D9%85%D9%86%D8%B8%D8%B1%D9%87-232.jpeg", false);
    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {
        Log.d("TestBaseBrowseFragment", "selected= " + rowPos + " : " + pos);
    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {

    }
}
