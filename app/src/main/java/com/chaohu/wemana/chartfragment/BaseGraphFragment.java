package com.chaohu.wemana.chartfragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaohu.wemana.model.UserData;
import com.chaohu.wemana.utils.BMIDemo;
import com.chaohu.wemana.utils.FileHelper;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.FileUtils;

import java.util.ArrayList;

/**
 * Created by chaohu on 2016/5/8.
 */
public class BaseGraphFragment extends Fragment {
    private Typeface tf;

    public String[] mLabels = new String[] { "Company A", "Company B", "Company C", "Company D", "Company E", "Company F" };
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

    private String getLabel(int i) {
        return mLabels[i];
    }

    public BaseGraphFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void addUpperLower(){
        UserData userData = new FileHelper().heightAndWeight();
        BMIDemo demo = new BMIDemo(userData);

        LimitLine ll1 = new LimitLine(demo.calculateUpperWeight().floatValue(), "Upper");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(Float.valueOf(userData.getWeight()), "Target");
//        LimitLine ll2 = new LimitLine(0.01F, "Target");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(10f);
        ll2.setTypeface(tf);

        LimitLine ll3 = new LimitLine(demo.calculateLowerWeight().floatValue(), "Lower");
        ll3.setLineWidth(4f);
        ll3.enableDashedLine(10f, 10f, 0f);
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll3.setTextSize(10f);
        ll3.setTypeface(tf);
    }

    protected BarData generateBarData(int dataSets, float range, int count) {

        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();

        for(int i = 0; i < dataSets; i++) {

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

//            entries = FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "stacked_bars.txt");

            for(int j = 0; j < count; j++) {
                entries.add(new BarEntry((float) (Math.random() * range) + range / 4, j));
            }

            BarDataSet ds = new BarDataSet(entries, getLabel(i));
            ds.setColors(ColorTemplate.VORDIPLOM_COLORS);
            sets.add(ds);
        }

        BarData d = new BarData(ChartData.generateXVals(0, count), sets);
        d.setValueTypeface(tf);
        return d;
    }

    protected ScatterData generateScatterData(int dataSets, float range, int count) {

        ArrayList<IScatterDataSet> sets = new ArrayList<IScatterDataSet>();

        ScatterChart.ScatterShape[] shapes = ScatterChart.getAllPossibleShapes();

        for(int i = 0; i < dataSets; i++) {

            ArrayList<Entry> entries = new ArrayList<Entry>();

            for(int j = 0; j < count; j++) {
                entries.add(new Entry((float) (Math.random() * range) + range / 4, j));
            }

            ScatterDataSet ds = new ScatterDataSet(entries, getLabel(i));
            ds.setScatterShapeSize(12f);
            ds.setScatterShape(shapes[i % shapes.length]);
            ds.setColors(ColorTemplate.COLORFUL_COLORS);
            ds.setScatterShapeSize(9f);
            sets.add(ds);
        }

        ScatterData d = new ScatterData(ChartData.generateXVals(0, count), sets);
        d.setValueTypeface(tf);
        return d;
    }

    protected LineData generateLineData() {

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();

        LineDataSet ds1 = new LineDataSet(FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "sine.txt"), "Sine function");
        LineDataSet ds2 = new LineDataSet(FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "cosine.txt"), "Cosine function");

        ds1.setLineWidth(2f);
        ds2.setLineWidth(2f);

        ds1.setDrawCircles(false);
        ds2.setDrawCircles(false);

        ds1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        ds2.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);

        sets.add(ds1);
        sets.add(ds2);


        int max = Math.max(sets.get(0).getEntryCount(), sets.get(1).getEntryCount());

        LineData d = new LineData(ChartData.generateXVals(0, max),  sets);
        d.setValueTypeface(tf);
        return d;
    }

    protected LineData getComplexity() {

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();

        LineDataSet ds1 = new LineDataSet(FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "n.txt"), "O(n)");
        LineDataSet ds2 = new LineDataSet(FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "nlogn.txt"), "O(nlogn)");
        LineDataSet ds3 = new LineDataSet(FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "square.txt"), "O(n\u00B2)");
        LineDataSet ds4 = new LineDataSet(FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "three.txt"), "O(n\u00B3)");

        ds1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        ds2.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        ds3.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        ds4.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);

        ds1.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        ds2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        ds3.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        ds4.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[3]);

        ds1.setLineWidth(2.5f);
        ds1.setCircleRadius(3f);
        ds2.setLineWidth(2.5f);
        ds2.setCircleRadius(3f);
        ds3.setLineWidth(2.5f);
        ds3.setCircleRadius(3f);
        ds4.setLineWidth(2.5f);
        ds4.setCircleRadius(3f);


        // load DataSets from textfiles in assets folders
        sets.add(ds1);
        sets.add(ds2);
        sets.add(ds3);
        sets.add(ds4);

        LineData d = new LineData(ChartData.generateXVals(0, ds1.getEntryCount()), sets);
        d.setValueTypeface(tf);
        return d;
    }

}
