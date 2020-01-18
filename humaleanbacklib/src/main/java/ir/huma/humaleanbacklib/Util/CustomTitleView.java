package ir.huma.humaleanbacklib.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.leanback.widget.SearchOrbView;
import androidx.leanback.widget.TitleViewAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Custom title view to be used in {@link androidx.leanback.app.BrowseFragment}.
 */
public class CustomTitleView extends RelativeLayout implements TitleViewAdapter.Provider {
    View view;
    SearchOrbView searchOrbView;

    private final TitleViewAdapter mTitleViewAdapter = new TitleViewAdapter() {
        @Override
        public View getSearchAffordanceView() {
            return searchOrbView;
        }

//        @Override
//        public void setTitle(CharSequence titleText) {
//            CustomTitleView.this.setTitle(titleText);
//        }
//
//        @Override
//        public void setBadgeDrawable(Drawable drawable) {
//            //CustomTitleView.this.setBadgeDrawable(drawable);
//        }

        @Override
        public void setOnSearchClickedListener(OnClickListener listener) {
            searchOrbView.setOnClickListener(listener);
        }
//
        @Override
        public void updateComponentsVisibility(int flags) {
//            if((flags & BRANDING_VIEW_VISIBLE) == BRANDING_VIEW_VISIBLE){
//                Toast.makeText(getContext(), "visible!!!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getContext(), "Gone!!!", Toast.LENGTH_SHORT).show();
//            }
            /*if ((flags & BRANDING_VIEW_VISIBLE) == BRANDING_VIEW_VISIBLE) {
                updateBadgeVisibility(true);
            } else {
                mAnalogClockView.setVisibility(View.GONE);
                mTitleView.setVisibility(View.GONE);
            }*/
//
//            int visibility = (flags & SEARCH_VIEW_VISIBLE) == SEARCH_VIEW_VISIBLE
//                    ? View.VISIBLE : View.INVISIBLE;
//            mSearchOrbView.setVisibility(visibility);
        }

//        private void updateBadgeVisibility(boolean visible) {
//            if (visible) {
//                view.setVisibility(View.VISIBLE);
//            } else {
//                view.setVisibility(View.GONE);
//            }
//        }
    };

    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void addView(int resId, int searchOrbViewId, OnTitleReadyListener onTitleReadyListener) {
        view = LayoutInflater.from(getContext()).inflate(resId, this);
        searchOrbView = view.findViewById(searchOrbViewId);
        onTitleReadyListener.onReady(view);
    }


    public interface OnTitleReadyListener {
        void onReady(View v);
    }

    @Override
    public TitleViewAdapter getTitleViewAdapter() {
        return mTitleViewAdapter;
    }
}
