package com.chaohu.wemana.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by chaohu on 2016/3/31.
 */
public class SaveWeightServiceImpl extends IntentService {
    private static final String TAG = "SaveWeightServiceImpl";

    public SaveWeightServiceImpl(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        // Intent 是从Activity发过来的 携带参数
        String weightData = intent.getExtras().getString("weight_data");
        // 保存到本地文件
    }
}
