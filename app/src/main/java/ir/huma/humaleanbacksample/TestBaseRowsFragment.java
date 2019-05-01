package ir.huma.humaleanbacksample;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.VerticalGridPresenter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ir.huma.humaleanbacklib.Util.CustomTitleView;
import ir.huma.humaleanbacklib.fragments.BaseBrowseFragment;
import ir.huma.humaleanbacklib.fragments.BaseRowsFragment;
import ir.huma.humaleanbacklib.test.BasePresenter;
import ir.huma.humaleanbacklib.test.IconHeaderItem;
import ir.huma.humaleanbacklib.test.MyListRowPresenter;
import ir.huma.humaleanbacksample.model.Video;

public class TestBaseRowsFragment extends BaseRowsFragment {
    ArrayObjectAdapter mRowsAdapter;


    @Override
    public void initial() {
//        setShowHeader(true);

        // set fastLane (or headers) background color
//        setBrandColor(ResourcesCompat.getColor(getResources(), R.color.fastlane_background, null));

        // set search icon color
//        setSearchAffordanceColor(Color.TRANSPARENT);

//
        MyListRowPresenter p = new MyListRowPresenter();
//        p.setHorizontalNumRows(3);
//        p.setRtl(true);
//        p.setShadowEnabled(true);
        mRowsAdapter = new ArrayObjectAdapter(p);
        setAlignment(100);

        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);

        ArrayObjectAdapter adapter1 = new ArrayObjectAdapter(cardPresenter);
        for (int i = 0; i < 10; i++) {
            adapter1.add(new Video());
        }
        IconHeaderItem headerItem = new IconHeaderItem("hello");
//        headerItem.setTypeface(FontManager.instance().getTypeface());
        mRowsAdapter.add(new ListRow(headerItem, adapter1));

        setAdapter(mRowsAdapter);


//        setTitleView(R.layout.custom_titleview, R.id.search_orb, new CustomTitleView.OnTitleReadyListener() {
//            @Override
//            public void onReady(View v) {
//
//            }
//        });


//        setOnSearchClickedListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
//            }
//        });
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
