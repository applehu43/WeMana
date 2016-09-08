package com.chaohu.wemana.chartfragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chaohu.wemana.model.DBColumn;
import com.chaohu.wemana.model.UserData;
import com.chaohu.wemana.utils.BMIDemo;
import com.chaohu.wemana.utils.DBOpenHelper;
import com.chaohu.wemana.utils.FileHelper;
import com.chaohu.wemana.utils.MyDateFormatUtil;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chaohu on 2016/5/8.
 */
public class BaseGraphFragment extends Fragment {

    private Typeface tf;
    /**
     * database
     */
    private DBOpenHelper myDB;

    public BaseGraphFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
//        myDB = new DBOpenHelper(getContext(), DBColumn.db_name, null, 1);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void addUpperLower(float max, YAxis leftAxis) {
        UserData userData = new FileHelper().heightAndWeight();
        BMIDemo demo;
        BigDecimal target;
        if(userData != null){
            demo = new BMIDemo(userData);
            target = new BigDecimal(userData.getWeight());
        }else{
            demo = new BMIDemo(BigDecimal.valueOf(175),BigDecimal.valueOf(60));
            target = BigDecimal.valueOf(60);
        }
        String[] names = {"Upper","Target","Lower"};
        BigDecimal BD_TWO = BigDecimal.valueOf(2);

        BigDecimal upper = demo.calculateUpperWeight();

        BigDecimal lower = demo.calculateLowerWeight();
        BigDecimal def_height = upper.subtract(lower).abs();

        BigDecimal given = BigDecimal.valueOf(max);
        BigDecimal l_upper,l_lower;
        // 目标值(T) 标准上限(U) 标准下限(L) 标准差(S=U-L)
        // T > U + S/2 超胖
        if (def_height.divide(BD_TWO).compareTo(target.subtract(upper))<0){
            // U 作为图表的起点
            addWhichLine(l_upper = target,names[1],leftAxis);
            addWhichLine(l_lower = upper,names[0],leftAxis);
        }
        // T < L - S/2 极瘦
        else if(def_height.divide(BD_TWO).compareTo(lower.subtract(target))<0){
            // T 作为图表的起点
            addWhichLine(l_upper = lower,names[2],leftAxis);
            addWhichLine(l_lower = target,names[1],leftAxis);
        }
        // L-S/2 <= T <= U+S/2`
        else{
            // L 作为图标的起点
            addWhichLine(l_lower = lower,names[2],leftAxis);
            addWhichLine(target,names[1],leftAxis);
            addWhichLine(l_upper = upper,names[0],leftAxis);
        }

        l_upper = l_upper.add(BigDecimal.valueOf(1));
        l_lower = l_lower.subtract(BigDecimal.valueOf(3));
        leftAxis.setAxisMinValue(l_lower.floatValue());
        leftAxis.setAxisMaxValue(l_upper.floatValue());
    }
    private void addWhichLine(BigDecimal value, String name, YAxis leftAxis) {
        LimitLine ll1 = new LimitLine(value.floatValue(), name);
        ll1.setLineWidth(0.5f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTypeface(tf);
        leftAxis.addLimitLine(ll1);
        LimitLine ll2 = new LimitLine(value.floatValue(), value.toString());
        ll2.setLineWidth(0.2f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(8f);
        leftAxis.addLimitLine(ll2);
    }
    protected LineData generateLineData(int day_len) {
        // 查询30天的数据
        String dateStr =  MyDateFormatUtil.dateList(new Date(), day_len);

        myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);

//        List<ContentValues> valuelist = this.jsonToArray();
//        DBOpenHelper.delteValues(myDB.getWritableDatabase());
//        DBOpenHelper.insertInto(myDB.getWritableDatabase(), valuelist);

        Cursor cursor = DBOpenHelper.queryWeightByDate(myDB.getReadableDatabase(),dateStr.split(","));

        ArrayList<String> xdate = new ArrayList<>();
        ArrayList<Entry> yweight = new ArrayList<>();

        UserData userData = new FileHelper().heightAndWeight();
        BMIDemo demo;
        if(userData != null){
            demo = new BMIDemo(userData);
        }else{
            demo = new BMIDemo(BigDecimal.valueOf(175),BigDecimal.valueOf(60));
        }
        BigDecimal upper = demo.calculateUpperWeight();
        BigDecimal lower = demo.calculateLowerWeight();
        BigDecimal def_height = upper.subtract(lower).abs().multiply(BigDecimal.valueOf(1.2));

        int len = cursor == null ? -1 : cursor.getCount();
        int i = 0;
        // all queried
        if (len == day_len){
            while (cursor.moveToNext()){
                String recordDate = cursor.getString(cursor.getColumnIndex(DBColumn.record_date));
                if (cursor.moveToLast()){
                    xdate.add("today");
                }else {
                    xdate.add(recordDate.substring(5,recordDate.length()));
                }
                Entry ds2 = new Entry(keep2scale(cursor.getFloat(cursor.getColumnIndex(DBColumn.weight_data))),++i);
                yweight.add(ds2);
            }

        }else if (len <= 0) {
            Log.i("No data","no data was recorded for last "+day_len+" days");
            return null;
        }else {
            while (cursor.moveToNext()) {
                Calendar calr = Calendar.getInstance();
                calr.setTime(MyDateFormatUtil.strToDate(MyDateFormatUtil.getToday()));
                calr.add(Calendar.DAY_OF_MONTH, -day_len+1 + i);
                String recordDate = cursor.getString(cursor.getColumnIndex(DBColumn.record_date));
                Date rdate = MyDateFormatUtil.strToDate(recordDate);
                Calendar recordCal = Calendar.getInstance();
                recordCal.setTime(rdate);

                float weight = keep2scale(cursor.getFloat(cursor.getColumnIndex(DBColumn.weight_data)));
                BigDecimal bd_weight = BigDecimal.valueOf(weight);
                if (calr.compareTo(recordCal) == 0){
                    if (bd_weight.subtract(upper).abs().compareTo(def_height) == 1
                            || bd_weight.subtract(lower).abs().compareTo(def_height) == 1){
                        // data is too big or too small, ignore it
                        i++;
                    }else {
                        Entry ds2 = new Entry(weight, i++);
                        yweight.add(ds2);
                    }
                }else {
                    i = MyDateFormatUtil.getDaysBetweenDates(calr.getTime(), rdate) + i;
                    if (bd_weight.subtract(upper).abs().compareTo(def_height) == 1
                            || bd_weight.subtract(lower).abs().compareTo(def_height) == 1) {
                    }else {
                        Entry ds2 = new Entry(weight, i);
                        yweight.add(ds2);
                    }
                    i++;
                }

            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(MyDateFormatUtil.strToDate(MyDateFormatUtil.getToday()));
            cal.add(Calendar.DAY_OF_MONTH, 1-day_len);
            for (int j=1; j<day_len; j++) {
                String recordDate = MyDateFormatUtil.dateToStr(cal.getTime());
                xdate.add(recordDate.substring(5, recordDate.length()));
                cal.add(Calendar.DAY_OF_MONTH,1);
            }
            xdate.add("today");
        }
        LineDataSet xlds = new LineDataSet(yweight, "last "+day_len+" days' weight");
        xlds.setLineWidth(2f);
        // do not show values
        xlds.setDrawValues(false);
        // show cubic on the line
        xlds.setDrawCubic(true);
        xlds.setCubicIntensity(0.09f);
        // 设置圆点
//        xlds.setDrawCircles(false);
        xlds.setCircleColor(Color.WHITE);
        xlds.setCircleRadius(1.8f);
        xlds.setHighLightColor(Color.rgb(244, 117, 117));
        xlds.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);

        // 设置 颜色填充
//        xlds.setDrawFilled(true);
//        if (Utils.getSDKInt() >= 18) {
//            // fill drawable only supported on api level 18 and above
//            Drawable drawable = ContextCompat.getDrawable(getActivity().getApplicationContext(),
//                    R.drawable.shape_numbers);
//            xlds.setFillDrawable(drawable);
//        }else {
//            xlds.setFillColor(Color.BLUE);
//        }

        LineData d = new LineData(xdate, xlds);
        d.setValueTypeface(tf);
        return d;
    }

    protected Map<String, Object> generateMonthCombineData(){
        myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);
        ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Entry> lineEntries = new ArrayList<>();
        ArrayList<String> xvals = new ArrayList<>();
        Cursor cursor = DBOpenHelper.queryAvgWeightMonthly(myDB.getReadableDatabase());
        int k = 0;
        if(cursor.moveToLast()) {
            do {
                float awd = keep2scale(cursor.getFloat(cursor.getColumnIndex("avg_weight_data")));
                barEntries.add(new BarEntry(awd, k));
                lineEntries.add(new Entry(awd*0.94F, k));
                k++;
                xvals.add(cursor.getString(cursor.getColumnIndex("record_month")));
            } while (cursor.moveToPrevious());
        }
        Map<String, Object> result = new HashMap<>();

        BarDataSet ds = new BarDataSet(barEntries,"the last "+xvals.size()+" months' weight");
        ds.setBarSpacePercent(33);
        barDataSets.add(ds);
        BarData d = new BarData(xvals, barDataSets);
        d.setValueTypeface(tf);

        LineDataSet xlds = new LineDataSet(lineEntries, "trend");
        xlds.setLineWidth(3.3f);
        xlds.setDrawCubic(true);
        xlds.setCircleColor(Color.rgb(240, 238, 70));
        xlds.setCircleRadius(6.8f);
        // do not show values
        xlds.setDrawValues(false);
        xlds.setColor(Color.rgb(240, 238, 70));
        xlds.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSets.add(xlds);
        LineData ld = new LineData(xvals,lineDataSets);

        result.put("xValue", xvals);
        result.put("barData", d);
        result.put("lineData", ld);
        return result;
    }

    protected BarData generateMonthBarData(){
        myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xvals = new ArrayList<>();
        Cursor cursor = DBOpenHelper.queryAvgWeightMonthly(myDB.getReadableDatabase());
        int k = 0;
        if(cursor.moveToLast()) {
            do {
                entries.add(new BarEntry(keep2scale(cursor.getFloat(cursor.getColumnIndex("avg_weight_data")))
                        , k++));
                xvals.add(cursor.getString(cursor.getColumnIndex("record_month")));
//            if (k >= month_len)
//                break;
            } while (cursor.moveToPrevious());
        }
        BarDataSet ds = new BarDataSet(entries,"the last "+xvals.size()+" months' weight");
        ds.setBarSpacePercent(33);
        sets.add(ds);
        BarData d = new BarData(xvals, sets);
        d.setValueTypeface(tf);
        return d;
    }
    protected BarData generateWeekBarData(int week_len) {
        // 查询10周的数据
        String dateStr=MyDateFormatUtil.dateList(new Date(), 7 * week_len + 1);
        String[] dates = dateStr.split(",");
        myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);
        ArrayList<IBarDataSet> sets = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> xvals = new ArrayList<>();
        for (int k=0; k<week_len; k++) {

            String[] weekdate = new String[7];
            System.arraycopy(dates, 7 * (week_len - k - 1), weekdate,0,7);
            xvals.add((week_len-k)+"周");
            Cursor cursor = DBOpenHelper.queryAvgWeightByDate(myDB.getReadableDatabase(), weekdate);
            while (cursor.moveToNext()) {
                entries.add(new BarEntry(keep2scale(cursor.getFloat(cursor.getColumnIndex("avg_weight_data")))
                        , k));
            }
        }

        BarDataSet ds = new BarDataSet(entries, "the last " + week_len + " weeks' weight");
        ds.setBarSpacePercent(19);
        sets.add(ds);
        if (entries.size() == 0 ){
            xvals = new ArrayList<>();
        }
        BarData d = new BarData(xvals, sets);
        d.setValueTypeface(tf);
        return d;
    }

    private static List<ContentValues> jsonToArray() {
        int length = 93;
        double baseWeight = 64.58D;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<ContentValues> list = new ArrayList<>();
        for (int i=0; i<=length; i++){
            Calendar calNext = Calendar.getInstance();
            calNext.setTime(new Date());
            // random days forget record weight(less than 8 days)
            int j = (int) (Math.random() * 5);
            i = i+j;
            calNext.add(Calendar.DAY_OF_MONTH, -i);
            ContentValues content = new ContentValues();
            content.put("weight_data","0"+BigDecimal.valueOf(Math.random()*4.62 + baseWeight)
                    .setScale(1,BigDecimal.ROUND_HALF_UP).toString());
            content.put("record_date", sdf.format(calNext.getTime()));
            list.add(content);
        }
        return list;
    }

    private static float keep2scale(float f){
        return BigDecimal.valueOf(f).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
