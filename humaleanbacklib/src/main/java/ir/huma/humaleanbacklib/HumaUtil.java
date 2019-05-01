package ir.huma.humaleanbacklib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import ir.huma.humaleanbacklib.Util.ImageLoader;
import ir.huma.humaleanbacklib.fragments.BaseBrowseFragment;
import ir.huma.humaleanbacklib.fragments.BaseLeanbackDialog;

public class HumaUtil {

    public static void startFragment(Context context, Class<? extends Fragment> fragmentClass) {
        PublicActivity.startWithFragment(context, fragmentClass, null, false);
    }

    public static void startFragment(Context context, Class<? extends Fragment> fragmentClass, Bundle bundle) {
        PublicActivity.startWithFragment(context, fragmentClass, bundle, false);
    }

    public static void startFragment(Context context, Class<? extends Fragment> fragmentClass, Bundle bundle, boolean isRtl) {
        PublicActivity.startWithFragment(context, fragmentClass, bundle, isRtl);
    }

    public static void startLeanbackFragmentForResult(Activity context, int requestCode, String title, String description, String positiveText, String negativeText, boolean isRtl) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("description", description);
        bundle.putString("positiveText", positiveText);
        bundle.putString("negativeText", negativeText);

        PublicActivity.startWithFragmentForResult(context, BaseLeanbackDialog.class, bundle, isRtl, requestCode);
    }

    public static void LoadImage(ImageView imageView, String url) {
        new ImageLoader().setImageView(imageView).load(imageView.getContext(), url);
    }


}
