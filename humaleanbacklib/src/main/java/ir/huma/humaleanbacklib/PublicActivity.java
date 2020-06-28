package ir.huma.humaleanbacklib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.app.GuidedStepSupportFragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import ir.huma.humaleanbacklib.fragments.OnBackPressListener;

public class PublicActivity extends FragmentActivity {
    InputMethodManager imm;
    Fragment f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        final View activityRootView = findViewById(R.id.topLayout);

        if (getIntent().getBooleanExtra("isRtl", false))
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        if (null == savedInstanceState) {

            String fragmentName = getIntent().getStringExtra("fragment");
            Bundle bundle = getIntent().getBundleExtra("bundle");
            try {
                f = (Fragment) Class.forName(fragmentName).newInstance();
                if (bundle != null)
                    f.setArguments(bundle);
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.topLayout, f);
                tx.commit();

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);


    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    public static void startWithFragment(Context context, Class<? extends Fragment> fragmentClass, Bundle bundle, boolean isRtl) {
        Intent intent = new Intent(context, PublicActivity.class);
        intent.putExtra("fragment", fragmentClass.getCanonicalName());
        intent.putExtra("isRtl", isRtl);
        if (bundle != null)
            intent.putExtra("bundle", bundle);

        context.startActivity(intent);
    }

    public static void startWithFragmentForResult(Activity context, Class<? extends Fragment> fragmentClass, Bundle bundle, boolean isRtl, int requestCode) {
        Intent intent = new Intent(context, PublicActivity.class);
        intent.putExtra("fragment", fragmentClass.getCanonicalName());
        intent.putExtra("isRtl", isRtl);
        if (bundle != null)
            intent.putExtra("bundle", bundle);

        context.startActivityForResult(intent, requestCode);
    }


    @Override
    public void onBackPressed() {
        if (f != null && f instanceof OnBackPressListener) {
            try {
                if (((OnBackPressListener) f).onBackPress()) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onBackPressed();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            f.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            f.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
