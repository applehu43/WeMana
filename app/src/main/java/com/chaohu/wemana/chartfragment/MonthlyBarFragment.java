package com.chaohu.wemana.chartfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaohu.wemana.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineData;

import java.util.List;
import java.util.Map;


public class MonthlyBarFragment extends BaseGraphFragment {

    public static Fragment newInstance() {
        return new MonthlyBarFragment();
    }

    private CombinedChart mChart;
//    private BarChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_bar2, container, false);

        mChart = (CombinedChart) v.findViewById(R.id.barChart2);
//        mChart = (BarChart) v.findViewById(R.id.barChart2);

        mChart.setDescription("");
        mChart.setDrawGridBackground(false);

        Map<String, Object> combinedMap = generateMonthCombineData();
        List<String> xValue = (List<String>) combinedMap.get("xValue");
        BarData barData = (BarData) combinedMap.get("barData");
        LineData lineData = (LineData) combinedMap.get("lineData");

        CombinedData combinedData = new CombinedData(xValue);
        combinedData.setData(barData);
        combinedData.setData(lineData);
        mChart.setData(combinedData);

        mChart.setNoDataTextDescription("go recording");
        mChart.animateXY(2400, 900);

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
