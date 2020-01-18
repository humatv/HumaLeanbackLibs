package ir.huma.humaleanbacklib.Util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Timer;
import java.util.TimerTask;

import ir.huma.humaleanbacklib.R;

public class MyBackgroundManager {
    private static final int BACKGROUND_UPDATE_DELAY = 400;

    Context context;
    ImageView backgroundLayout;
    Timer mBackgroundTimer;
    Handler handler;
    Object drawable;
    int tryLoadImage = 0;
    ObjectAnimator alphaAnimator;
//    Drawable dd;

    private int defaultBackgroundResource = R.drawable.default_background;

    public MyBackgroundManager(Context context, final ImageView backgroundLayout) {
        this.backgroundLayout = backgroundLayout;
        this.context = context;
        handler = new Handler();

        alphaAnimator = ObjectAnimator.ofFloat(backgroundLayout, View.ALPHA, 1.0f, 0.3f);
        alphaAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                Log.d("MainFragmentUI", "startAnim");

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                    if (d != dd) {
//                        setBackgroundDrawable(dd);
//                    }
                Log.d("MainFragmentUI", "endAnim");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                doImage(drawable);
//                if (drawable != null && dd != null) {
//                    backgroundLayout.setImageDrawable(dd);
//                }
            }
        });
        alphaAnimator.setRepeatCount(1);
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);
        alphaAnimator.setDuration(250);
    }

    public void setBackground(Object drawable, boolean hasDelay, boolean smooth) {
        if (this.drawable == drawable) {
            return;
        }
        this.drawable = drawable;
        if (mBackgroundTimer != null) {
            mBackgroundTimer.cancel();
        }
        if (!hasDelay) {
            updateBackground(drawable, smooth);
        } else {
            mBackgroundTimer = new Timer();
            mBackgroundTimer.schedule(new UpdateBackgroundTask(drawable, smooth), BACKGROUND_UPDATE_DELAY);
        }
    }

    public void setBackground(Object drawable, boolean hasDelay) {
        setBackground(drawable, hasDelay, true);
    }

    public void setBackground(Object drawable) {
        setBackground(drawable, true, true);
    }

    protected class UpdateBackgroundTask extends TimerTask {
        Object drawable;
        boolean smooth;

        public UpdateBackgroundTask(Object drawable, boolean smooth) {
            this.drawable = drawable;
            this.smooth = smooth;
        }

        @Override
        public void run() {
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            updateBackground(drawable, smooth);
                        } catch (Exception e) {

                        }
                    }
                });
            } catch (Exception e) {

            }
        }
    }

    String newUri;

    protected void updateBackground(Object object, final boolean smooth) {
//        final RequestOptions options = new RequestOptions();
//        Log.d("MainFragmentUI", "updateBackground");
//        if (!((MainActivity) context).ismShyMode()) {
//            backgroundLayout.setImageDrawable(new BitmapDrawable(AppController.blackShadow));
//            dd = null;
//            alphaAnimator.cancel();
//        } else
        if (object instanceof String) {
            final String uri = (String) object;
            newUri = uri;
            tryLoadImage = 0;
            if (uri != null && !uri.isEmpty()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (uri == newUri) {
                            Glide.with(context).asBitmap().load(uri)

                                    .addListener(new RequestListener<Bitmap>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                            if (uri == newUri && tryLoadImage < 5) {
                                                Glide.with(context).asBitmap().load(uri).addListener(this).preload();
                                            }
                                            Log.d("MainFragmentUI", "onFailed");

                                            tryLoadImage++;
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(final Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                            if (uri == newUri) {
                                                drawable = new BitmapDrawable(context.getResources(), resource);
                                                Log.d("MainFragmentUI", "onResourceReady");
                                                if (smooth) {
                                                    alphaAnimator.start();
                                                } else {
                                                    doImage(drawable);
                                                }

                                            }
                                            return false;
                                        }
                                    }).preload();
                        }
                    }
                }, 200);
            }
        } else {
            if (smooth) {
                alphaAnimator.start();
            } else {
                doImage(object);
            }
        }
        mBackgroundTimer.cancel();
    }

    private void doImage(Object object) {
        if (object == null) {
            backgroundLayout.setImageResource(defaultBackgroundResource);
        } else if (object instanceof Drawable) {
            backgroundLayout.setImageDrawable((Drawable) object);
        } else if (object instanceof Bitmap) {
            backgroundLayout.setImageDrawable(new BitmapDrawable((Bitmap) object));
        } else if (object instanceof Integer) {
            backgroundLayout.setImageResource((Integer) object);
        }
    }

    public void setDefaultBackgroundResource(int defaultBackgroundResource) {
        this.defaultBackgroundResource = defaultBackgroundResource;
    }
}
