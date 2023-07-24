package com.android.car_exam4;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class CarouselPageTransformer implements ViewPager2.PageTransformer {

    private static final float CENTER_PAGE_SCALE = 0.8f;
    private static final float SIDE_PAGE_SCALE = 0.6f;
    private static final float SIDE_PAGE_ALPHA = 0.4f;

    @Override
    public void transformPage(@NonNull View page, float position) {
        int viewPagerWidth = page.getWidth();
        int pageWidth = page.getWidth();
        float offset = position * -pageWidth;

        if (position < -1) { // Off-screen to the left
            page.setAlpha(0f);
        } else if (position <= 1) { // Visible pages
            float scaleFactor = Math.max(CENTER_PAGE_SCALE, 1 - Math.abs(position - 0.5f));
            page.setTranslationX(viewPagerWidth * 0.5f * (position - offset / viewPagerWidth));
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setAlpha(CENTER_PAGE_SCALE);
        } else { // Off-screen to the right
            page.setAlpha(0f);
        }

        // Scale and adjust alpha for side pages
        float scaleFactor = SIDE_PAGE_SCALE + (1 - SIDE_PAGE_SCALE) * (1 - Math.abs(position));
        page.setScaleX(scaleFactor);
        page.setScaleY(scaleFactor);
        page.setAlpha(SIDE_PAGE_ALPHA + (1 - SIDE_PAGE_ALPHA) * (1 - Math.abs(position)));
    }
}
