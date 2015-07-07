package com.autodesk.tct;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class SwipeItemPagerAdapter extends PagerAdapter {
    public final static int PAGE_CONTENT = 0;
    public final static int PAGE_ACTION = 1;
    private final static int PAGE_COUNT = 2;

    public SwipeItemPagerAdapter() {
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int viewId = 0;
        if (position == PAGE_CONTENT) {
            viewId = R.id.content_view;
        } else if (position == PAGE_ACTION) {
            viewId = R.id.action_view;
        }
        return container.findViewById(viewId);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // @Override
    // public float getPageWidth(int position) {
    // //
    // return 0.5f;
    // }
}
