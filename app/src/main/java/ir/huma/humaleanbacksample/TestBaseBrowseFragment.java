package ir.huma.humaleanbacksample;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.ListRow;
import android.util.Log;
import android.view.KeyEvent;
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
        setShowHeader(false);
        //setInitBackgroundManager(false);
        // set fastLane (or headers) background color
        setBrandColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));

        // set search icon color
        setSearchAffordanceColor(Color.TRANSPARENT);

//
        MyListRowPresenter p = new MyListRowPresenter();

        p.setRtl(false);
//        p.setShadowEnabled(true);
        mRowsAdapter = new ArrayObjectAdapter(p);
        p.setAdapter(mRowsAdapter);
        p.setOnKeyInterceptListener(new BaseGridView.OnKeyInterceptListener() {
            @Override
            public boolean onInterceptKeyEvent(KeyEvent event) {
                Log.d("TestBaseBrowseFragment", event.toString());

                return false;
            }
        });
//        p.setOnLongClickListener(new MyListRowPresenter.OnLongClickListener() {
//            @Override
//            public void onLongClickListener(View v, Object item, int rowPos, int pos) {
//                Log.d("TestBaseBrowseFragment", "longClick1= " + rowPos + " : " + pos);
//
////                Toast.makeText(getContext(), "Hello "+rowPos+" "+pos, Toast.LENGTH_SHORT).show();
//            }
//        });
        BasePresenter<Video, Video.MyVideoView> cardPresenter = new BasePresenter<>(getActivity(), Video.MyVideoView.class, R.layout.item_video);
        cardPresenter.setOnItemLongClickListener(new BasePresenter.onItemLongClickListener() {
            @Override
            public boolean onLongClick(View v, Object item, int rowPos, int pos) {
                Log.d("TestBaseBrowseFragment", "longClick2= " + rowPos + " : " + pos);

//                Toast.makeText(getContext(), "longClick : " + rowPos+" " + pos, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        ArrayObjectAdapter adapter1 = new ArrayObjectAdapter(cardPresenter);
        cardPresenter.setAdapter(adapter1);
        for (int i = 0; i < 10; i++) {
            adapter1.add(new Video());
        }
        IconHeaderItem headerItem = new IconHeaderItem("Test");
        headerItem.setShadow(true);
        headerItem.setTextSize(22);
        headerItem.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"fonts/IRANSans.ttf"));
        headerItem.setUnselectedAlpha(0.5f);
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
//        setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.background2), false);
    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {
        Log.d("TestBaseBrowseFragment", "selected= " + rowPos + " : " + pos);
    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {
//        Toast.makeText(getContext(), "click!!", Toast.LENGTH_SHORT).show();
        Log.d("TestBaseBrowseFragment", "click= " + rowPos + " : " + pos);

    }
}
