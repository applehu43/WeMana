
package com.chaohu.wemana.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.chaohu.wemana.R;
import com.chaohu.wemana.adapter.ChartFragmentPagerAdapter;

/**
 * Demonstrates how to keep your charts straight forward, simple and beautiful with the MPAndroidChart library.
 * 
 * @author Philipp Jahoda
 */
public class SimpleChartDemo extends FragmentActivity {

    private ChartFragmentPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
            setContentView(R.layout.activity_wemana_graph);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(1);
        pagerAdapter = new ChartFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

    }

}
