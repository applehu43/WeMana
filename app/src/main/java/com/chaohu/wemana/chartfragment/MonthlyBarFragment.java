package com.chaohu.wemana.chartfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaohu.wemana.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;


public class MonthlyBarFragment extends BaseGraphFragment {

    public static Fragment newInstance() {
        return new MonthlyBarFragment();
    }

    private BarChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_bar, container, false);

        mChart = (BarChart) v.findViewById(R.id.barChart1);

        mChart.setDescription("");
        mChart.setDrawGridBackground(false);

        mChart.setData(generateMonthBarData(7));
        mChart.animateXY(1000, 1000);

        YAxis leftAxis = mChart.getAxisLeft();
        addUpperLower(mChart.getYMax(), leftAxis);

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextSize(12f);

        return v;
    }
}
