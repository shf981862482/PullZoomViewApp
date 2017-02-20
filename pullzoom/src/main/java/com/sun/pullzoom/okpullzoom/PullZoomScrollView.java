package com.sun.pullzoom.okpullzoom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

/**
 * Created by walkingmen on 2017/2/20.
 * 下拉放大的scrollview
 */

public class PullZoomScrollView extends PullZoomBaseView<ScrollView> {
    private static final String TAG = PullZoomScrollView.class.getSimpleName();
    private ZoomBackRunnable mZoomBackAnimation;
    private int mHeaderHeight;
    private Interpolator sSmoothToTopInterpolator;

    public PullZoomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHeaderHeight = 0;
        sSmoothToTopInterpolator = createDefaultInterpolator();
        mZoomBackAnimation = new ZoomBackRunnable();
    }

    private Interpolator createDefaultInterpolator() {
        return new DecelerateInterpolator(2.0f);
    }

    @Override
    public void addView(View child) {
        if (mWrapperView != null) {
            mWrapperView.addView(child);
        }
    }

    @Override
    protected ScrollView createWrapperView(Context context, AttributeSet attrs) {
        ScrollView scrollView = new ScrollView(context, attrs);
        scrollView.setId(Integer.MIN_VALUE + 1);
        return scrollView;
    }

    @Override
    protected int createDefaultPullZoomModel() {
        return ZOOM_HEADER;
    }

    @Override
    protected boolean isReadyZoom() {
        return mWrapperView.getScrollY() == 0;
    }

    @Override
    protected void pullZoomEvent(float scrollValue) {
        if (mZoomBackAnimation != null && !mZoomBackAnimation.isFinished()) {
            mZoomBackAnimation.abortAnimation();
        }

        if (mHeaderContainer != null) {
            ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
            layoutParams.height = (int) (Math.abs(scrollValue) + mHeaderHeight);
            mHeaderContainer.setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void smoothScrollToTop() {
        mZoomBackAnimation.startAnimation(ZOOM_BACK_DURATION);
    }


    public ScrollView getScrollView() {
        return mWrapperView;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (mHeaderContainer != null && mHeaderHeight <= 0) {
            mHeaderHeight = mHeaderContainer.getMeasuredHeight();
        }
    }


    private class ZoomBackRunnable implements Runnable {
        protected long mDuration;
        protected boolean mIsFinished = true;
        protected float mScale;
        protected long mStartTime;

        ZoomBackRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mZoomView != null && (!mIsFinished) && (mScale > 1.0f)) {
                // fix PullToZoomView bug  ---dinus
                // should not convert the System.currentTimeMillis() to float
                // otherwise the value of (System.currentTimeMillis() - mStartTime) will still be zero
                float zoomBackProgress = (System.currentTimeMillis() - mStartTime) / (float) mDuration;
                ViewGroup.LayoutParams localLayoutParams = mHeaderContainer.getLayoutParams();

                if (zoomBackProgress > 1.0f) {
                    localLayoutParams.height = mHeaderHeight;
                    mHeaderContainer.setLayoutParams(localLayoutParams);
                    mIsFinished = true;
                    return;
                }

                float currentSacle = mScale - (mScale - 1.0F) * sSmoothToTopInterpolator.getInterpolation(zoomBackProgress);
                localLayoutParams.height = (int) (currentSacle * mHeaderHeight);
                mHeaderContainer.setLayoutParams(localLayoutParams);
                post(this);
            }
        }

        public void startAnimation(long animationDuration) {
            if (mZoomView != null) {
                mStartTime = System.currentTimeMillis();
                mDuration = animationDuration;
                mScale = (float) mHeaderContainer.getHeight() / mHeaderHeight;
                mIsFinished = false;
                post(this);
            }
        }
    }
}
