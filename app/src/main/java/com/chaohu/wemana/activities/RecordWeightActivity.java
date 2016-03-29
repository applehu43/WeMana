package com.chaohu.wemana.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaohu.wemana.R;

import java.math.BigDecimal;

/**
 * @Description my weight manager app
 * Created by chaohu on 2016/3/27.
 */
public class RecordWeightActivity extends Activity {
    private boolean isExit = false;
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

    /**
     * show
     */
    private int click_count = 1;
    private static final String original_value = "000.0    KG";
    private StringBuffer str_display = new StringBuffer(original_value);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wemana_one);
        bt[0] = (Button) findViewById(R.id.bt_0);
        bt[1] = (Button) findViewById(R.id.bt_1);
        bt[2] = (Button) findViewById(R.id.bt_2);
        bt[3] = (Button) findViewById(R.id.bt_3);
        bt[4] = (Button) findViewById(R.id.bt_4);
        bt[5] = (Button) findViewById(R.id.bt_5);
        bt[6] = (Button) findViewById(R.id.bt_6);
        bt[7] = (Button) findViewById(R.id.bt_7);
        bt[8] = (Button) findViewById(R.id.bt_8);
        bt[9] = (Button) findViewById(R.id.bt_9);

        bt_save = (Button) findViewById(R.id.bt_save);
        bt_reset = (Button) findViewById(R.id.bt_reset);
        et_play = (EditText) findViewById(R.id.record_weight_show_data);
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
                Intent it = new Intent(RecordWeightActivity.this,WeManaActivity.class);
//                it.putExtra("weight",getWeightData(str_display));
                startActivityForResult(it,0x123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123 && resultCode == 0x123){
            Bundle bd = data.getExtras();
            String weight = bd.getString("weight");
            showWeightOnView(new StringBuffer(weight).append("    KG"));
        }
    }

    /**
     * @param sb
     * @return 只是数字的体重值
     */
    private String getWeightData(StringBuffer sb){
        return sb.substring(0,5);
    }

    /**
     * @descrption 展示体重值
     * @param sb
     */
    private void showWeightOnView(StringBuffer sb) {
        String exception = "66";
        String data = getWeightData(sb);
        BigDecimal bd_exc = new BigDecimal(exception);
        BigDecimal bd_data = new BigDecimal(data);

        Resources resources = getBaseContext().getResources();
        if (bd_data.compareTo(bd_exc) == 1) {
            et_play.setTextColor(resources.getColorStateList(R.color.back_red));
        }else{
            et_play.setTextColor(resources.getColorStateList(R.color.back_black));
        }
        et_play.setText(sb);
    }

    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    /**
     * @description 双击退出程序
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(!isExit){
                isExit = true;
                Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(0,2000);
            }
            else{
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }
}
