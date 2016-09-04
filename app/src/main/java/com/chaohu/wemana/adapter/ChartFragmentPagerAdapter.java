package com.chaohu.wemana.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chaohu.wemana.chartfragment.MonthlyBarFragment;
import com.chaohu.wemana.chartfragment.WeeklyBarFragment;
import com.chaohu.wemana.chartfragment.DailyLineFragment;

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
                f = DailyLineFragment.newInstance();
                break;
            case 1:
                f = WeeklyBarFragment.newInstance();
                break;
            case 2:
                f = MonthlyBarFragment.newInstance();
        }

        return f;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
