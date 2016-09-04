package com.chaohu.wemana.chartfragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        // 给定数值超出图表的(S=U-L)上或下界2倍 视为无效值
        if (def_height.multiply(BD_TWO).compareTo(given.subtract(l_lower).abs()) < 0
                && def_height.multiply(BD_TWO).compareTo(given.subtract(l_upper).abs()) < 0){
        }
        else{
            if(given.subtract(l_lower).abs().compareTo(given.subtract(l_upper).abs()) > 0){
                l_upper = l_upper.add(BigDecimal.valueOf(3));
            }else if (given.subtract(l_lower).abs().compareTo(given.subtract(l_upper).abs()) < 0){
                l_lower = l_lower.subtract(BigDecimal.valueOf(3));
            }

        }
        l_upper = l_upper.add(BigDecimal.valueOf(1));
        l_lower = l_lower.subtract(BigDecimal.valueOf(3));
        leftAxis.setAxisMinValue(l_lower.floatValue());
        leftAxis.setAxisMaxValue(l_upper.floatValue());
    }
    private void addWhichLine(BigDecimal value, String name, YAxis leftAxis) {
        LimitLine ll1 = new LimitLine(value.floatValue(), name);
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTypeface(tf);
        leftAxis.addLimitLine(ll1);
        LimitLine ll2 = new LimitLine(value.floatValue(), value.toString());
        ll2.setLineWidth(2f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        leftAxis.addLimitLine(ll2);
    }
    protected LineData generateLineData(int day_len) {
        // 查询30天的数据
        String dateStr =  MyDateFormatUtil.dateList(new Date(), day_len);

        myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);

//        List<ContentValues> valuelist = this.jsonToArray();
//        DBOpenHelper.delteValues(myDB.getWritableDatabase());
//        DBOpenHelper.insertInto(myDB.getWritableDatabase(),valuelist);

        Cursor cursor = DBOpenHelper.queryWeightByDate(myDB.getReadableDatabase(),dateStr.split(","));

        ArrayList<String> xdate = new ArrayList<String>();
        ArrayList<Entry> yweight = new ArrayList<Entry>();
        int i = 0;
//        int len = cursor.getCount();
        Calendar cal = Calendar.getInstance();
        cal.setTime(MyDateFormatUtil.strToDate(MyDateFormatUtil.getToday()));
        while (cursor.moveToNext()){
            String recordDate = cursor.getString(cursor.getColumnIndex(DBColumn.record_date));
            Date rdate = MyDateFormatUtil.strToDate(recordDate);
            Calendar rcal = Calendar.getInstance();
            rcal.setTime(rdate);
            if (cal.compareTo(rcal) == 0){
                xdate.add("today");
            }else {
//                xdate.add(""+ (len - i - 1));
                xdate.add(recordDate.substring(5,recordDate.length()));
            }
            Entry ds2 = new Entry(keep2scale(cursor.getFloat(cursor.getColumnIndex(DBColumn.weight_data))),++i);
            yweight.add(ds2);
        }
        LineDataSet xlds = new LineDataSet(yweight, "last "+day_len+" days' weight");
        xlds.setLineWidth(2f);
        xlds.setDrawCircles(false);
        xlds.setDrawValues(false);
        xlds.setColor(ColorTemplate.VORDIPLOM_COLORS[1]);
        LineData d = new LineData(xdate, xlds);
        d.setValueTypeface(tf);
        return d;
    }

    protected BarData generateMonthBarData(int month_len){
        myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);
        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        ArrayList<String> xvals = new ArrayList<String>();
        Cursor cursor = DBOpenHelper.queryAvgWeightMonthly(myDB.getReadableDatabase());
        int k = 0;
        cursor.moveToLast();
        while (cursor.moveToPrevious()) {
            entries.add(new BarEntry(keep2scale(cursor.getFloat(cursor.getColumnIndex("avg_weight_data")))
                    , k++));
            xvals.add(cursor.getString(cursor.getColumnIndex("record_month")));
//            if (k >= month_len)
//                break;
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
        String dateStr=MyDateFormatUtil.dateList(new Date(), 7 * week_len);
        String[] dates = dateStr.split(",");
        myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);
        ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        ArrayList<String> xvals = new ArrayList<String>();
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
        BarDataSet ds = new BarDataSet(entries,"the last "+week_len+" weeks' weight");
        ds.setBarSpacePercent(19);
        sets.add(ds);
        BarData d = new BarData(xvals, sets);
        d.setValueTypeface(tf);
        return d;
    }

    private static List<ContentValues> jsonToArray() {
        int length = 333;
        double baseWeight = 65.58D;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calNext = Calendar.getInstance();
        calNext.setTime(new Date());

        List<ContentValues> list = new ArrayList<>();
        for (int i=1; i<=length; i++){
            ContentValues content = new ContentValues();
            content.put("weight_data","0"+BigDecimal.valueOf(Math.random()*4.62 + baseWeight)
                    .setScale(1,BigDecimal.ROUND_HALF_UP).toString());
            content.put("record_date",sdf.format(calNext.getTime()));
            list.add(content);
            calNext.add(Calendar.DAY_OF_MONTH, -1);
        }
        return list;
    }

    private static float keep2scale(float f){
        return BigDecimal.valueOf(f).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }

}
