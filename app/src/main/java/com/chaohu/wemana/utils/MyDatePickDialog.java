package com.chaohu.wemana.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.chaohu.wemana.R;

import java.util.Calendar;

/**
 * Created by chaohu on 2016/9/4.
 */
public class MyDatePickDialog implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private AlertDialog dialog;
    private Activity activity;

    private String date;
    private String initDate;

    public MyDatePickDialog(Activity act, String initDate){
        this.activity = act;
        this.initDate = initDate;
    }

    public void init(DatePicker dp, TimePicker tp){
        Calendar cal = Calendar.getInstance();
        if (initDate == null){
            cal.setTime(MyDateFormatUtil.strToDate(MyDateFormatUtil.getToday()));
        }else {
            cal.setTime(MyDateFormatUtil.strToDate(initDate));
        }
        // init datePicker
        dp.init(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), this);
        // init timePicker
        if(tp != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tp.setHour(cal.get(Calendar.HOUR_OF_DAY));
                tp.setMinute(cal.get(Calendar.MINUTE));
            }
            else {
                tp.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
                tp.setCurrentMinute(cal.get(Calendar.MINUTE));
            }
        }
    }

    public AlertDialog pickDateDialog(EditText text){
        LinearLayout dateLayout = (LinearLayout) activity.getLayoutInflater().inflate(
                R.layout.date_time_dialog,null);
        datePicker = (DatePicker) dateLayout.findViewById(R.id.date_picker);
//        timePicker = (TimePicker) dateLayout.findViewById(R.id.time_picker);
        init(datePicker,null);


        return dialog;
    }

    public AlertDialog pickDateAndTimeDialog(final EditText text){
        LinearLayout dateLayout = (LinearLayout) activity.getLayoutInflater().inflate(
                R.layout.date_time_dialog,null);
        datePicker = (DatePicker) dateLayout.findViewById(R.id.date_picker);
        timePicker = (TimePicker) dateLayout.findViewById(R.id.time_picker);
        init(datePicker,timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);

        dialog = new AlertDialog.Builder(activity)
                .setTitle(initDate)
                .setView(dateLayout)
                .setPositiveButton("choose", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                text.setText(date);
                                dialog.cancel();
                            }
                        }
                )
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        text.setText("");
                    }
                }).show();

        onDateChanged(null,0,0,0);
        return dialog;
    }
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cal.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),
                    timePicker.getHour(),timePicker.getMinute());
        }else {
            cal.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),
                    timePicker.getCurrentHour(),timePicker.getCurrentMinute());
        }
        dialog.setTitle(MyDateFormatUtil.dateToStr(cal.getTime()));
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null,0,0,0);
    }

}
