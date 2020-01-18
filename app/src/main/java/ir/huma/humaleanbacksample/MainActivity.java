package ir.huma.humaleanbacksample;

import android.content.Context;
import android.os.Handler;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import ir.huma.humaleanbacklib.HumaUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onBaseBrowseClick(View view) {
        HumaUtil.startFragment(this, TestBaseBrowseFragment.class, null);
    }

    public void onGridClick(View view) {
        HumaUtil.startFragment(this, TestGridFragment.class, null);
    }

    public void onFragmentFactoryClick(View view) {

        Bundle bundle2 = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view,
                "hello").toBundle();
        HumaUtil.startFragment(this, TestFragmentFactory.class, bundle2);


    }

    public void onBaseRowsClick(View view) {
        HumaUtil.startFragment(this, TestBaseRowsFragment.class, null);

    }

    public void onGuidedStepClick(View view) {
        HumaUtil.startFragment(this, TestGuidedStepFragment.class, null, true);

    }

    public void onSearchPageClick(View view) {
        HumaUtil.startFragment(this, TestSearchFragment.class, null);

    }


    public void onDetailFragmentClick(View view) {
        Bundle b= new Bundle();
        b.putString("key","value");
        HumaUtil.startFragment(this, TestDetailFragment.class, null);

    }

    public void onTestSearchGrid(View view) {
        HumaUtil.startFragment(this, TestSearchGridFragment.class, null);

    }
}
