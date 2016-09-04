package com.chaohu.wemana.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.chaohu.wemana.fragment.MeasureFragment;
import com.chaohu.wemana.fragment.SettingsFragment;
import com.chaohu.wemana.fragment.WeightGraphFragment;
import com.chaohu.wemana.fragment.WeightListFragment;

/**
 * Created by chaohu on 2016/3/30.
 */
public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private FragmentManager fm;

    private MeasureFragment measureFragment = null;
    private WeightGraphFragment graphFragment = null;
    private WeightListFragment listFragment = null;
    private SettingsFragment settingsFragment = null;

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
        measureFragment = new MeasureFragment();
        graphFragment = new WeightGraphFragment();
        listFragment = new WeightListFragment();
        settingsFragment = new SettingsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = measureFragment;
                break;
            case 1:
                fragment = graphFragment;
                break;
            case 2:
                fragment = listFragment;
                break;
            case 3:
                fragment = settingsFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        System.out.println("position Destroy" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        if(object instanceof WeightGraphFragment || object instanceof SettingsFragment){
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
