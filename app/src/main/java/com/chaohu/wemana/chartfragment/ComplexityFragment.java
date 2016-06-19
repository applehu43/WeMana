package com.chaohu.wemana.chartfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaohu.wemana.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;


public class ComplexityFragment extends BaseGraphFragment {

    public static Fragment newInstance() {
        return new ComplexityFragment();
    }

    private LineChart mChart;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_line, container, false);

        mChart = (LineChart) v.findViewById(R.id.lineChart1);

        mChart.setDescription("");

        mChart.setDrawGridBackground(false);

        mChart.setData(getComplexity());
        mChart.animateX(3000);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);

        return v;
    }
}
