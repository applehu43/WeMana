package com.chaohu.wemana.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaohu.wemana.R;
import com.chaohu.wemana.model.DBColumn;
import com.chaohu.wemana.utils.DBOpenHelper;
import com.chaohu.wemana.utils.MyDateFormatUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chaohu on 2016/3/30.
 */
public class WeightListFragment extends Fragment {

    private Context mContext;
    private DBOpenHelper myDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wemana_list, container, false);
        mContext = getActivity().getApplicationContext();
        myDB = new DBOpenHelper(mContext, DBColumn.db_name, null, 1);
        initListData(view);
        return view;
    }

    private void initListData(View view) {
        Date today = new Date();
        int daysOfMonth = MyDateFormatUtil.totalMonthDay(today);
        TextView[] textViews = new TextView[daysOfMonth];
        int[] ids = new int[]{
                R.id.day1,R.id.day2,R.id.day3,R.id.day4,R.id.day5,R.id.day6,R.id.day7,R.id.day8,R.id.day9,
                R.id.day10,R.id.day11,R.id.day12,R.id.day13,R.id.day14,R.id.day15,R.id.day16,R.id.day17,
                R.id.day18,R.id.day19,R.id.day20,R.id.day21,R.id.day22,R.id.day23,R.id.day24,R.id.day25,
                R.id.day26,R.id.day27,R.id.day28,R.id.day29,R.id.day30,R.id.day31
        };
        for(int i=0; i<daysOfMonth; i++){
            textViews[i] = (TextView) view.findViewById(ids[i]);
        }
        String nowstr = MyDateFormatUtil.dateToStr(today);
        String daylen = nowstr.substring(nowstr.length() - 2, nowstr.length());
        String daystr = MyDateFormatUtil.dateList(today, Integer.valueOf(daylen));
        Cursor cursor = DBOpenHelper.queryWeightByDate(myDB.getReadableDatabase(), daystr.split(","));
        int k = 0;
        Date intervalDay = MyDateFormatUtil.strToDate(nowstr.substring(0,nowstr.length()-2)+"01");
        Calendar cal = Calendar.getInstance();
        cal.setTime(intervalDay);
        while (cursor.moveToNext()){
            String recordDate = cursor.getString(cursor.getColumnIndex(DBColumn.record_date));
            String result = cursor.getString(cursor.getColumnIndex(DBColumn.weight_data));
            BigDecimal removezero = new BigDecimal(result);
            textViews[k].setTextColor(Color.argb(255, (int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255)));
            Date rdate = MyDateFormatUtil.strToDate(recordDate);
            Calendar rcal = Calendar.getInstance();
            rcal.setTime(rdate);
            if (cal.compareTo(rcal) == 0){
                textViews[k++].setText(removezero.toString());
            }else{
                textViews[k++].setText("0.00");
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

}
