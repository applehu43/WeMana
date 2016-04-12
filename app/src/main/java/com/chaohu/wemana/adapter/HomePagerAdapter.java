package com.chaohu.wemana.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * ViewPager适配器
 */
public class HomePagerAdapter extends PagerAdapter {

    public List<View> mListViews;

    public HomePagerAdapter(List<View> list) {
        this.mListViews = list;
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return mListViews.get(position);
    }
}
