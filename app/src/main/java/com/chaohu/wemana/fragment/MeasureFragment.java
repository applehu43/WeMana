package com.chaohu.wemana.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.chaohu.wemana.R;
import com.chaohu.wemana.model.DBColumn;
import com.chaohu.wemana.utils.DBOpenHelper;
import com.chaohu.wemana.utils.FileHelper;
import com.chaohu.wemana.utils.MyDateFormatUtil;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by chaohu on 2016/3/30.
 */
public class MeasureFragment extends Fragment implements View.OnTouchListener {


    /**
     * show weight
     */
    private EditText et_play;
    /**
     * data button
     */
    private Button[] bt = new Button[10];
    private EditText date_picker;
    /**
     * function button
     */
    private Button bt_save;
    private Button bt_reset;
    private Button bt_add_date;
    private Button bt_sub_date;

    private Context mContext;
    /**
     * database
     */
    private DBOpenHelper myDB;
    /**
     * show
     */
    private int click_count = 1;
    private static final String original_value = "000.0    KG";
    private StringBuffer str_display = new StringBuffer(original_value);
    private String record_date = MyDateFormatUtil.getToday();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wemana_measure, container, false);
        mContext = getActivity().getApplicationContext();
        myDB = new DBOpenHelper(mContext, DBColumn.db_name, null, 1);
        InitButtons(view);
        return view;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = View.inflate(mContext, R.layout.date_time_dialog, null);
            final DatePicker picker = (DatePicker) view.findViewById(R.id.date_picker);
            builder.setView(view);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            picker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

            int inType = date_picker.getInputType();
            date_picker.setInputType(InputType.TYPE_NULL);
            date_picker.onTouchEvent(event);
            date_picker.setInputType(inType);
            date_picker.setSelection(date_picker.getText().length());

            builder.setTitle("choose the date");
            builder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    record_date = String.format("%d-%02d-%02d", picker.getYear(), picker.getMonth() + 1, picker.getDayOfMonth());
                    showWeightOnView(queryWeightByDate(record_date));
                    date_picker.setText(record_date);
                    dialog.cancel();
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

    private void InitButtons(View view) {
        bt[0] = (Button) view.findViewById(R.id.bt_0);
        bt[1] = (Button) view.findViewById(R.id.bt_1);
        bt[2] = (Button) view.findViewById(R.id.bt_2);
        bt[3] = (Button) view.findViewById(R.id.bt_3);
        bt[4] = (Button) view.findViewById(R.id.bt_4);
        bt[5] = (Button) view.findViewById(R.id.bt_5);
        bt[6] = (Button) view.findViewById(R.id.bt_6);
        bt[7] = (Button) view.findViewById(R.id.bt_7);
        bt[8] = (Button) view.findViewById(R.id.bt_8);
        bt[9] = (Button) view.findViewById(R.id.bt_9);

        bt_save = (Button) view.findViewById(R.id.bt_save);
        bt_reset = (Button) view.findViewById(R.id.bt_reset);
        bt_add_date = (Button) view.findViewById(R.id.record_date_add);
        bt_sub_date = (Button) view.findViewById(R.id.record_date_sub);

        date_picker = (EditText) view.findViewById(R.id.record_date);
        date_picker.setOnTouchListener(this);
        et_play = (EditText) view.findViewById(R.id.record_weight_show_data);
        // 展示日期 数值
        date_picker.setText(record_date);
        String queryRes = queryWeightByDate(record_date);
        showWeightOnView(queryRes);
        et_play.setSelection(click_count);

        // 曾经保存过的数据 必须通过reset按钮修改
        if (getWeightData(original_value).equals(queryRes)) {
            initNumButton();
        }

        bt_add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Calendar now = Calendar.getInstance();
                cal.setTime(MyDateFormatUtil.strToDate(record_date));
                now.setTimeInMillis(System.currentTimeMillis());
                cal.add(Calendar.DAY_OF_MONTH, 1);
                if (cal.after(now)) {
                    Toast.makeText(mContext, "活在当下~", Toast.LENGTH_SHORT).show();
                    record_date = MyDateFormatUtil.getToday();
                }else{
                    record_date = MyDateFormatUtil.dateToStr(cal.getTime());
                }
                date_picker.setText(record_date);
                String queryRes = queryWeightByDate(record_date);
                showWeightOnView(queryRes);
                // 曾经保存过的数据 必须通过reset按钮修改
                if (getWeightData(original_value).equals(queryRes)) {
                    initNumButton();
                }else{
                    disableOnclickNumButton();
                }
            }
        });
        bt_sub_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(MyDateFormatUtil.strToDate(record_date));
                cal.add(Calendar.DAY_OF_MONTH, -1);
                record_date = MyDateFormatUtil.dateToStr(cal.getTime());
                date_picker.setText(record_date);
                String queryRes = queryWeightByDate(record_date);
                showWeightOnView(queryRes);
                // 曾经保存过的数据 必须通过reset按钮修改
                if (getWeightData(original_value).equals(queryRes)) {
                    initNumButton();
                }else {
                    disableOnclickNumButton();
                }
            }
        });

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_display = new StringBuffer(original_value);
                showWeightOnView(str_display.toString());
                et_play.setSelection(click_count = 1);
                initNumButton();
            }
        });

        // save the weight data
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDB.getWritableDatabase();
                String[] weight_date_data = new String[]{getWeightData(str_display.toString()), record_date};

                // 1.先查询有没有某一天的数据
                Cursor cursor = DBOpenHelper.queryWeightByDate(db, new String[]{record_date});
                if (cursor == null || cursor.moveToFirst()) {
                    // 2.有则更新の
                    DBOpenHelper.updateWM(db,weight_date_data);
                } else {
                    // 3.没有就直接插入
                    DBOpenHelper.insertWM(db,weight_date_data);
                }
                str_display = new StringBuffer(original_value);
                showWeightOnView(queryWeightByDate(record_date));
                // 保存完后 取消按键的输入
                disableOnclickNumButton();
                Toast.makeText(mContext, "保存成功~", Toast.LENGTH_SHORT).show();

            }

        });

    }

    /**
     * 指定日期查数据
     * @param givenDay
     * @return
     */
    private String queryWeightByDate(String givenDay){
        String weight = getWeightData(original_value);
        Cursor result = DBOpenHelper.queryWeightByDate(myDB.getReadableDatabase(), new String[]{givenDay});
        if(result != null && result.moveToFirst()){
            weight = result.getString(result.getColumnIndex(DBColumn.weight_data));
        }
        return weight;
    }
    /**
     * @param sb
     * @return 只是数字的体重值
     */

    private String getWeightData(String sb) {
        return sb.substring(0, 5);
    }

    /**
     * @param sb
     * @descrption 展示体重值
     */
    private void showWeightOnView(String sb) {
        FileHelper file = new FileHelper();
        String target = file.heightAndWeight() == null ? "60" : file.heightAndWeight().getWeight();
        String data = getWeightData(sb);
        BigDecimal bd_exc = new BigDecimal(target);
        BigDecimal bd_data = new BigDecimal(data);

        Resources resources = getActivity().getBaseContext().getResources();
        if (bd_data.compareTo(bd_exc) == 1) {
            et_play.setTextColor(resources.getColorStateList(R.color.back_red));
        } else {
            et_play.setTextColor(resources.getColorStateList(R.color.light_blue));
        }
        et_play.setText(data+"    KG");
    }

    private void initNumButton(){
        for (int i = 0; i < 10; i++) {
            final String index = String.valueOf(i);
            bt[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int cursor_num = et_play.getSelectionStart();
                    if ('.' == str_display.charAt(cursor_num)) {
                        et_play.setSelection(++cursor_num);
                    }
                    str_display.replace(cursor_num, cursor_num + 1, index);
                    showWeightOnView(str_display.toString());
                    if (click_count >= 3) {
                        click_count = -1;
                    }
                    et_play.setSelection(++click_count);
                }
            });
        }
    }
    private void disableOnclickNumButton(){
        for (int i = 0; i < 10; i++) {
            bt[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    return;
                }
            });
        }
    }
}
