package ir.huma.humaleanbacksample;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.PageRow;
import android.view.View;
import android.widget.Toast;

import ir.huma.humaleanbacklib.fragments.BaseBrowseFragment;
import ir.huma.humaleanbacklib.fragments.PageRowFragmentFactory;
import ir.huma.humaleanbacklib.presenter.IconHeaderItem;
import ir.huma.humaleanbacklib.presenter.MyListRowPresenter;

public class TestFragmentFactory extends BaseBrowseFragment {
    ArrayObjectAdapter mRowsAdapter;

    @Override
    public void initial() {
        mRowsAdapter = new ArrayObjectAdapter(new MyListRowPresenter());

        PageRowFragmentFactory factory = new PageRowFragmentFactory(getActivity(), mRowsAdapter, this);
        factory.addFragment(1, new TestGridFragment());
        factory.addFragment(2, new TestGridFragment());
        factory.addFragment(3, new TestBaseRowsFragment());


        getMainFragmentRegistry().registerFragment(PageRow.class, factory);


        setAdapter(mRowsAdapter);
        createRows();
        startEntranceTransition();

        setBackgroundUri("https://bayanbox.ir/view/4386502884462510503/%D9%85%D9%86%D8%B8%D8%B1%D9%87-232.jpeg", false);

    }


    private void createRows() {
        IconHeaderItem headerItem1 = new IconHeaderItem(1, "test1", null);
//        headerItem1.setTypeface(FontManager.instance().getTypeface());
        PageRow pageRow1 = new PageRow(headerItem1);
        mRowsAdapter.add(pageRow1);

        IconHeaderItem headerItem3 = new IconHeaderItem(3, "test3", null);
//        headerItem1.setTypeface(FontManager.instance().getTypeface());
        PageRow pageRow3 = new PageRow(headerItem3);
        mRowsAdapter.add(pageRow3);


        IconHeaderItem headerItem2 = new IconHeaderItem(2, "test2", null);
//        headerItem2.setTypeface(FontManager.instance().getTypeface());
        PageRow pageRow2 = new PageRow(headerItem2);
        mRowsAdapter.add(pageRow2);

    }

    @Override
    public void onItemSelectedListener(View v, Object item, int rowPos, int pos) {
        Toast.makeText(getContext(), "selected :" + pos, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickListener(View v, Object item, int rowPos, int pos) {

    }
}
