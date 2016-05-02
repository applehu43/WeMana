package com.chaohu.wemana.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaohu.wemana.R;
import com.chaohu.wemana.utils.DBOpenHelper;
import com.chaohu.wemana.utils.MyDateFormatUtil;

import java.math.BigDecimal;

/**
 * Created by chaohu on 2016/3/30.
 */
public class MeasureFragment extends Fragment {


    /**
     * show weight
     */
    private EditText et_play;
    /**
     * data button
     */
    private Button[] bt = new Button[10];
    /**
     * function button
     */
    private Button bt_save;
    private Button bt_reset;

    private Context mContext;
    /**
     * database
     */
    private SQLiteDatabase db;
    private DBOpenHelper myDB;
    /**
     * show
     */
    private int click_count = 1;
    private static final String original_value = "000.0    KG";
    private StringBuffer str_display = new StringBuffer(original_value);
    private final String record_date = MyDateFormatUtil.getToday();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wemana_measure, container, false);
        mContext = getActivity().getApplicationContext();
        myDB = new DBOpenHelper(mContext, "weight.db", null, 1);
        InitButtons(view);
        return view;
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
        et_play = (EditText) view.findViewById(R.id.record_weight_show_data);
        showWeightOnView(str_display);
        et_play.setSelection(click_count);

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
                    showWeightOnView(str_display);
                    if (click_count >= 3) {
                        click_count = -1;
                    }
                    et_play.setSelection(++click_count);
                }
            });
        }

        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_display = new StringBuffer(original_value);
                showWeightOnView(str_display);
                et_play.setSelection(click_count = 0);
            }
        });

        // save the weight data
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = myDB.getWritableDatabase();
                String[] weight_date_data = new String[]{getWeightData(str_display), record_date};

                // 1.先查询有没有某一天的数据
                Cursor cursor = DBOpenHelper.queryWeightByDate(db, new String[]{record_date});
                if (cursor ==null || cursor.moveToFirst()) {
                    // 2.有则更新の
                    db.execSQL("update weight_record set weight_data = ? where record_date = ?",
                            weight_date_data);
                } else {
                    // 3.没有就直接插入
                    db.execSQL("insert into weight_record(weight_data,record_date) " +
                            "values (?,?)", weight_date_data);
                }
                Toast.makeText(mContext, "保存成功~", Toast.LENGTH_SHORT).show();
            }

        });

    }

    /**
     * @param sb
     * @return 只是数字的体重值
     */

    private String getWeightData(StringBuffer sb) {
        return sb.substring(0, 5);
    }

    /**
     * @param sb
     * @descrption 展示体重值
     */
    private void showWeightOnView(StringBuffer sb) {
        String target = "66";
        String data = getWeightData(sb);
        BigDecimal bd_exc = new BigDecimal(target);
        BigDecimal bd_data = new BigDecimal(data);

        Resources resources = getActivity().getBaseContext().getResources();
        if (bd_data.compareTo(bd_exc) == 1) {
            et_play.setTextColor(resources.getColorStateList(R.color.back_red));
        } else {
            et_play.setTextColor(resources.getColorStateList(R.color.back_black));
        }
        et_play.setText(sb);
    }
}
