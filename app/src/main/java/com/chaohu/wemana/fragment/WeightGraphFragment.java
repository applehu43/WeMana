package com.chaohu.wemana.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chaohu.wemana.R;
import com.chaohu.wemana.adapter.ChartFragmentPagerAdapter;

/**
 * Created by chaohu on 2016/3/30.
 */
public class WeightGraphFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener{

    private Typeface tf;
    // 页签头标
    private RadioButton[] radioButtons = new RadioButton[3];
    private RadioGroup radioGroup;
    private  ChartFragmentPagerAdapter adapter;
    // 页签内容
    private ViewPager mPager;

    public WeightGraphFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        View view = inflater.inflate(R.layout.activity_wemana_graph,container,false);

        initTextView(view);

        mPager = (ViewPager) view.findViewById(R.id.graphPager);
        adapter = new ChartFragmentPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(1);
        mPager.addOnPageChangeListener(this);
        radioButtons[0].setSelected(true);
        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.text0:
                mPager.setCurrentItem(0);
                break;
            case R.id.text1:
                mPager.setCurrentItem(1);
                break;
            case R.id.text2:
                mPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        radioButtons[position].setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == 2) {
            int curItem = mPager.getCurrentItem();
            for (int i = 0; i < 3; i++) {
                if (curItem == i) {
                    radioButtons[i].setSelected(true);
                } else {
                    radioButtons[i].setSelected(false);
                }
            }
        }
    }


    /**
     * 初始化头标
     */
    private void initTextView(View view) {

        radioGroup = (RadioGroup) view.findViewById(R.id.timeFrame);

        radioButtons[0] = (RadioButton) view.findViewById(R.id.dailyFrame);
        radioButtons[1] = (RadioButton) view.findViewById(R.id.weeklyFrame);
        radioButtons[2] = (RadioButton) view.findViewById(R.id.monthlyFrame);

        radioGroup.setOnCheckedChangeListener(this);
    }
}
