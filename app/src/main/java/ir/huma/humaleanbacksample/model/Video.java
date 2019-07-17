package ir.huma.humaleanbacksample.model;

import android.content.Context;
import android.widget.TextView;

import ir.huma.humaleanbacklib.test.MyBaseCardView;
import ir.huma.humaleanbacksample.R;

public class Video {
    public String name;
    public String imageUrl;
    public String videoUrl;

    public Video(String name) {
        this.name = name;
    }

    public Video() {
    }

    public static class MyVideoView extends MyBaseCardView<Video> {

        public MyVideoView(Context context, int layoutResId) {
            super(context, layoutResId);
        }

        @Override
        public void fillData(Video video) {
            TextView textView = findViewById(R.id.Textview);
            textView.setText(video.name);
        }

        @Override
        protected void changeSelected(boolean selected, int pos) {

        }


    }

}
