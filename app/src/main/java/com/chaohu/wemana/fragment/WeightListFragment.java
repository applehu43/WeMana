package com.chaohu.wemana.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chaohu.wemana.R;
import com.chaohu.wemana.activities.HomeActivity;
import com.chaohu.wemana.adapter.HomeFragmentPagerAdapter;
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
    private TextView view_year;
    private TextView view_month;
    private Button bt_add;
    private Button bt_sub;
    private String init_year = MyDateFormatUtil.getToday().substring(0, 4);
    private String init_month = MyDateFormatUtil.getToday().substring(5, 7);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wemana_list, container, false);
        mContext = getActivity().getApplicationContext();
        myDB = new DBOpenHelper(mContext, DBColumn.db_name, null, 1);
        initTextAndButton(view);

        initListData(view, chooseDate());
        return view;
    }

    private Date chooseDate(){
        String now = MyDateFormatUtil.getToday();
        StringBuffer date = new StringBuffer(view_year.getText()).append("-").
                append(view_month.getText());
        date.append("-");
        if (init_month.equals(view_month.getText()) && init_year.equals(view_year.getText())){
            date.append(now.substring(8, 10));
        }else{
            StringBuffer sb = new StringBuffer(view_year.getText()).append("-").
                    append(view_month.getText());
            sb.append("-01");
            date.append(MyDateFormatUtil.totalMonthDay(MyDateFormatUtil.strToDate(sb.toString())));
        }

        return MyDateFormatUtil.strToDate(date.toString());
    }

    private int whichMonth = -1;
    private void initTextAndButton(final View view) {
        view_month = (TextView) view.findViewById(R.id.list_record_month);
        view_year = (TextView) view.findViewById(R.id.list_record_year);
        bt_add = (Button) view.findViewById(R.id.list_date_add);
        bt_sub = (Button) view.findViewById(R.id.list_date_sub);
        view_year.setText(init_year);
        view_month.setText(init_month);
        final String[] months = new String[12];
        for (int i=0; i<months.length; ){
            months[i] = i<9? "0" + (++i) : "" + (++i);
        }

        view_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setSingleChoiceItems(months, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                whichMonth = which;
                            }
                        })
                        .setNegativeButton("back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (whichMonth >= 0) {
                                    view_month.setText(months[whichMonth]);
                                    initListData(view, chooseDate());
                                }
                                dialog.cancel();
                            }
                        }).show();
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                now.setTime(chooseDate());
                now.add(Calendar.MONTH, 1);
                String datestr = MyDateFormatUtil.dateToStr(now.getTime());
                view_year.setText(datestr.substring(0, 4));
                view_month.setText(datestr.substring(5, 7));
                initListData(view, chooseDate());
            }
        });
        bt_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                now.setTime(chooseDate());
                now.add(Calendar.MONTH, -1);
                String datestr = MyDateFormatUtil.dateToStr(now.getTime());
                view_year.setText(datestr.substring(0, 4));
                view_month.setText(datestr.substring(5, 7));
                initListData(view, chooseDate());
            }
        });
    }

    private void initListData(View view,Date today) {
        int daysOfMonth = MyDateFormatUtil.totalMonthDay(today);
        int[] ids = new int[]{
                R.id.day1,R.id.day2,R.id.day3,R.id.day4,R.id.day5,R.id.day6,R.id.day7,R.id.day8,R.id.day9,
                R.id.day10,R.id.day11,R.id.day12,R.id.day13,R.id.day14,R.id.day15,R.id.day16,R.id.day17,
                R.id.day18,R.id.day19,R.id.day20,R.id.day21,R.id.day22,R.id.day23,R.id.day24,R.id.day25,
                R.id.day26,R.id.day27,R.id.day28,R.id.day29,R.id.day30,R.id.day31
        };
        int[] bdid = new int[]{
                R.id.dateday1,R.id.dateday2,R.id.dateday3,R.id.dateday4,R.id.dateday5,R.id.dateday6,
                R.id.dateday7,R.id.dateday8,R.id.dateday9,R.id.dateday10,R.id.dateday11,R.id.dateday12,
                R.id.dateday13,R.id.dateday14,R.id.dateday15,R.id.dateday16,R.id.dateday17,R.id.dateday18,
                R.id.dateday19,R.id.dateday20,R.id.dateday21,R.id.dateday22,R.id.dateday23,R.id.dateday24,
                R.id.dateday25,R.id.dateday26,R.id.dateday27,R.id.dateday28,R.id.dateday29,R.id.dateday30,
                R.id.dateday31
        };
        int length = ids.length;
        TextView[] textViews = new TextView[length];
        Button[] buttons = new Button[length];
        String[] days = new String[length];

        final HomeActivity homeAct = (HomeActivity) getActivity();
        for(int i=0; i<daysOfMonth; i++){
            textViews[i] = (TextView) view.findViewById(ids[i]);
            textViews[i].setText("");
            buttons[i] = (Button) view.findViewById(bdid[i]);
            String day = i<9 ? "0"+(i+1) : ""+(i+1);
            days[i] = day;
            final String chosenDate = MyDateFormatUtil.getTheMonth(today) + "-" + day;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(),"clicked ha",Toast.LENGTH_SHORT).show();
                    HomeFragmentPagerAdapter fpa = homeAct.getmAdapter();
                    MeasureFragment m = (MeasureFragment) fpa.getItem(0);
                    homeAct.setSelected(R.id.text0);
                    m.changeToGivenDate(chosenDate);
                }
            });
        }

        for(int i=daysOfMonth;i<length;i++){
            textViews[i] = (TextView) view.findViewById(ids[i]);
            textViews[i].setText("");
            buttons[i] = (Button) view.findViewById(bdid[i]);
            buttons[i].setOnClickListener(null);
        }

        String nowstr = MyDateFormatUtil.dateToStr(today);
        String daylen = nowstr.substring(nowstr.length() - 2, nowstr.length());
        String dateListStr = MyDateFormatUtil.dateList(today, Integer.valueOf(daylen));
        Cursor cursor = DBOpenHelper.queryWeightByDate(myDB.getReadableDatabase(), dateListStr.split(","));

        while (cursor.moveToNext()){
            String recordDate = cursor.getString(cursor.getColumnIndex(DBColumn.record_date));
            String result = cursor.getString(cursor.getColumnIndex(DBColumn.weight_data));
            // remove zero
            BigDecimal removezero = new BigDecimal(result);
            String dayStr = recordDate.substring(recordDate.length() - 2, recordDate.length());
            Integer dayInt = Integer.valueOf(dayStr) - 1;
            // underline && color
            textViews[dayInt].setTextColor(Color.argb(255, (int) (Math.random() * 255),
                    (int) (Math.random() * 255), (int) (Math.random() * 255)));
            textViews[dayInt].getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

            final String chosenDate = recordDate;
            textViews[dayInt].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(),"clicked ha",Toast.LENGTH_SHORT).show();
                    HomeFragmentPagerAdapter fpa = homeAct.getmAdapter();
                    MeasureFragment m = (MeasureFragment) fpa.getItem(0);
                    homeAct.setSelected(R.id.text0);
                    m.changeToGivenDate(chosenDate);
                }
            });

            textViews[dayInt].setText(removezero.toString());
        }
    }

}
