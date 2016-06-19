package com.chaohu.wemana.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chaohu.wemana.R;
import com.chaohu.wemana.adapter.HomeFragmentPagerAdapter;
import com.chaohu.wemana.broadcast.MyBroadcastReceiver;
import com.chaohu.wemana.utils.FileHelper;

/**
 * Created by chaohu on 2016/3/29.
 */
public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    // 页签内容
    private ViewPager mPager;
    // 适配器
    private HomeFragmentPagerAdapter mAdapter;
    // 广播
    private MyBroadcastReceiver broadcastReceiver;
    // 页签头标
    private RadioButton[] radioButtons = new RadioButton[5];
    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTitle("Weight Manager");
        setContentView(R.layout.activity_wemana_main);

        mAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        InitTextView();

        mPager = (ViewPager) findViewById(R.id.vPager);
        mPager.setOffscreenPageLimit(4);
        mPager.setAdapter(mAdapter);

        mPager.addOnPageChangeListener(this);

        FileHelper fileHelper = new FileHelper(this.getApplicationContext());
        String text_val = fileHelper.readFromSD("exceptWeightHeight.txt");
//        String text_val = fileHelper.readFromDataFiles("exceptWeightHeight.txt");
        if("fileNotFound".equals(text_val)){
            mPager.setCurrentItem(4);
            radioButtons[4].setSelected(true);
        }else{
//            String[] result_val = text_val.split(",");
//            new BMIDemo(new BigDecimal(result_val[0]),new BigDecimal(result_val[1]));
            mPager.setCurrentItem(0);
            radioButtons[0].setSelected(true);
        }
//        InitBroadcast();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.text0:
                mPager.setCurrentItem(0);
                break;
            case R.id.text1:
                mPager.setCurrentItem(1);
//                Intent intent = new Intent(this, WeightGraphFragment.class);
//                startActivity(intent);
                break;
            case R.id.text2:
                mPager.setCurrentItem(2);
                break;
            case R.id.text3:
                mPager.setCurrentItem(3);
                break;
            case R.id.text4:
                mPager.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == 2) {
            int curItem = mPager.getCurrentItem();
            for (int i = 0; i < 5; i++) {
                if (curItem == i) {
                    radioButtons[i].setSelected(true);
                } else {
                    radioButtons[i].setSelected(false);
                }
            }
        }
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {

        radioGroup = (RadioGroup) findViewById(R.id.texts);

        radioButtons[0] = (RadioButton) findViewById(R.id.text0);
        radioButtons[1] = (RadioButton) findViewById(R.id.text1);
        radioButtons[2] = (RadioButton) findViewById(R.id.text2);
        radioButtons[3] = (RadioButton) findViewById(R.id.text3);
        radioButtons[4] = (RadioButton) findViewById(R.id.text4);

        radioGroup.setOnCheckedChangeListener(this);
    }

    /**
     * 广播
     */
    private void InitBroadcast() {
        broadcastReceiver = new MyBroadcastReceiver();
        IntentFilter itf = new IntentFilter();
        itf.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver, itf);
    }

    private long lastedTime = 0;

    /**
     * @param keyCode
     * @param event
     * @return
     * @description 双击退出程序
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long firstTime = System.currentTimeMillis();

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((firstTime - lastedTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                lastedTime = firstTime;
            } else {
                this.finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }
}
