package com.chaohu.wemana.chartfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaohu.wemana.R;
import com.chaohu.wemana.fragment.WeightGraphFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;


public class SineCosineFragment extends WeightGraphFragment {

    public static Fragment newInstance() {
        return new SineCosineFragment();
    }

    private LineChart mChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_line, container, false);
        
        mChart = (LineChart) v.findViewById(R.id.lineChart1);
        
        mChart.setDescription("");

        mChart.setDrawGridBackground(false);
        
        mChart.setData(generateLineData());
        mChart.animateX(3000);
        
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaxValue(1.2f);
        leftAxis.setAxisMinValue(-1.2f);
        
        mChart.getAxisRight().setEnabled(false);
        
        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
        
        return v;
    }
}
