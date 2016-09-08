package com.chaohu.wemana.chartfragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaohu.wemana.R;
import com.chaohu.wemana.model.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


public class DailyLineFragment extends BaseGraphFragment implements OnChartValueSelectedListener {

    public static Fragment newInstance() {
        return new DailyLineFragment();
    }

    private LineChart mChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_line, container, false);

        mChart = (LineChart) v.findViewById(R.id.lineChart1);
        // enable scaling and dragging
        mChart.setDrawGridBackground(false);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setOnChartValueSelectedListener(this);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getActivity().getApplicationContext(),
                R.layout.custom_marker_view);
        // set the marker to the chart
        mChart.setMarkerView(mv);

        mChart.setDescription("");
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0){
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
//            mChart.setData(generateLineData(15));
            mChart.setData(generateLineData(31));


        if (mChart.getData() != null) {
            // 水平轴的图表值动画
            mChart.animateX(1800);

            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

            Legend l = mChart.getLegend();
            l.setForm(Legend.LegendForm.CIRCLE);

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines();
            leftAxis.setDrawGridLines(false);
            leftAxis.setTypeface(tf);
            leftAxis.setTextSize(10f);
            // 设置上下限和上下线
            addUpperLower(mChart.getLineData().getYMax(), leftAxis);

            XAxis xAxis = mChart.getXAxis();
            xAxis.setTypeface(tf);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setAvoidFirstLastClipping(true);
//        xAxis.setLabelsToSkip(2);
            xAxis.setTextSize(8f);

            mChart.getAxisRight().setEnabled(false);
        }else{
            mChart.setNoDataTextDescription("go recording");
        }

        return v;
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
        Log.i("select value","high light");
    }

    @Override
    public void onNothingSelected() {

    }
}
