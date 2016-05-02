package com.chaohu.wemana.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chaohu.wemana.chartfragment.ComplexityFragment;
import com.chaohu.wemana.chartfragment.SineCosineFragment;

/**
 * Created by chaohu on 2016/4/12.
 */
public class ChartFragmentPagerAdapter extends FragmentPagerAdapter {
    public ChartFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;

        switch(position) {
            case 0:
                f = SineCosineFragment.newInstance();
                break;
            case 1:
                f = ComplexityFragment.newInstance();
                break;
        }

        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
