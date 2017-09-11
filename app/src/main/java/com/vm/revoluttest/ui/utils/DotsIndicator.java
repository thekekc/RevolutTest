package com.vm.revoluttest.ui.utils;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vm.revoluttest.R;


/**
 * Quick realization of dots indicator in recycler view
 * Find view and attach a recycler view
 *
 */
public class DotsIndicator extends FrameLayout implements ViewPager.OnPageChangeListener {

    private DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
    private int pageCount = 0;
    private int dpMargin = getPixFromDp(2, metrics);
    private int chosenRadius = getPixFromDp(8, metrics);
    private int radius = getPixFromDp(8, metrics);
    @SuppressWarnings("FieldCanBeLocal")
    private int chosenColor = 0xffffffff, regularColor = 0x7fffffff;


    private View view;
    private LinearLayout dotsLayout;
    private ImageView[] dots;

    public DotsIndicator(Context context) {
        super(context);
        view = inflate(context);
    }

    public DotsIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context);
    }

    public DotsIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = inflate(context);
    }


    public void init(int pageCount) {
        if (pageCount > 1) {
            this.pageCount = pageCount;
            initDots();
        } else {
            dotsLayout.setVisibility(GONE);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dotsLayout = (LinearLayout) view.findViewById(R.id.layoutDots);
    }

    private void initDots() {
        dots = new ImageView[pageCount];
        dotsLayout.removeAllViews();
        switchBottomDots(0);
    }

    private void switchBottomDots(int currentPage) {
        if (dots == null) return;
        if (currentPage >= 0 && currentPage < dots.length) {
            dotsLayout.setGravity(Gravity.CENTER);
            for (int i = 0; i < dots.length; i++) {
                if (dots[i] == null) {
                    dots[i] = new ImageView(this.getContext());
                    dotsLayout.addView(dots[i]);
                }

                LinearLayout.LayoutParams layoutParams =
                        new LinearLayout.LayoutParams(radius, radius);
                layoutParams.setMargins(dpMargin, dpMargin, dpMargin, dpMargin);
                dots[i].setLayoutParams(layoutParams);
                dots[i].setImageDrawable(VectorDrawableCompat.create(
                        this.getContext().getResources(),
                        R.drawable.white_dot_transparent, null));
                dots[i].getDrawable().setColorFilter(regularColor,
                        android.graphics.PorterDuff.Mode.SRC_IN);

            }
            if (dots.length > 0) {
                dots[currentPage].setImageDrawable(VectorDrawableCompat.create(
                        this.getContext().getResources(),
                        R.drawable.white_dot, null));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(chosenRadius,
                    chosenRadius);
            layoutParams.setMargins(dpMargin, dpMargin, dpMargin, dpMargin);
            dots[currentPage].setLayoutParams(layoutParams);
            dots[currentPage].getDrawable().setColorFilter(chosenColor,
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    public void attach(RecyclerView view) {
        view.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager layoutManager =
                            ((LinearLayoutManager) recyclerView.getLayoutManager());
                    int position = layoutManager.findFirstVisibleItemPosition();
                    switchBottomDots(position % pageCount);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private int getPixFromDp(int dp, DisplayMetrics metrics) {
        float fpixels = metrics.density * dp;
        return (int) (fpixels + 0.5f);
    }

    private View inflate(Context context) {
        return inflate(context, R.layout.dots_indicator, this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switchBottomDots(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
