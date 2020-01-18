package ir.huma.humaleanbacklib.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;

import androidx.annotation.Nullable;

public class ImageLoader {
    private int tryLoading = 5;
    private ImageView imageView;
    private int width;
    private int height;
    private Drawable placeHolder;
    private Drawable error;
    private ReadyListener readyListener;


    public void load(final Context context, final String url) {
        if (imageView != null && imageView.getTag() != null && imageView.getTag().equals(url)) {
            return;
        }
        if (imageView != null)
            imageView.setTag(url);

        final RequestOptions options = new RequestOptions().centerCrop();
        if (width != 0 || height != 0) {
            options.override(width, height);
        }
        if (error != null) {
            options.error(error);
        }
        if (placeHolder != null) {
            options.placeholder(placeHolder);
        }

        Glide.with(context).asBitmap().apply(options).load(url).addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                if (imageView != null && imageView.getTag() != null && imageView.getTag().equals(url) && tryLoading > 0) {
                    Glide.with(context).asBitmap().apply(options).load(url).addListener(this).preload();
                } else if (imageView == null && tryLoading > 0) {
                    Glide.with(context).asBitmap().apply(options).load(url).addListener(this).preload();
                } else if (tryLoading <= 0 && imageView != null) {
//                    imageView.setTag(null);
                }
                tryLoading--;
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                if (imageView != null && imageView.getTag() != null && imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(resource);
//                    imageView.setTag(null);
                }
                if (readyListener != null) {
                    readyListener.onReady(resource);
                }
                return false;
            }
        }).preload();
    }

    public ImageLoader setTryLoading(int tryLoading) {
        this.tryLoading = tryLoading;
        return this;
    }

    public ImageLoader setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public ImageLoader setSize(int width,int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ImageLoader setPlaceHolderDrawable(Drawable placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public ImageLoader setErrorDrawable(Drawable error) {
        this.error = error;
        return this;
    }

    public ImageLoader setReadyListener(ReadyListener readyListener) {
        this.readyListener = readyListener;
        return this;
    }


    public interface ReadyListener {
        void onReady(Bitmap bitmap);
    }

}