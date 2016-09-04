package com.chaohu.wemana.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chaohu.wemana.adapter.HomeFragmentPagerAdapter;
import com.chaohu.wemana.utils.FileHelper;

/**
 * Created by chaohu on 2016/3/31.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (FileHelper.ACTION_UPDATE.equals(intent.getAction())){
//            Toast.makeText(context, "receive broadcast", Toast.LENGTH_SHORT).show();

            Intent newintent = new Intent(context, HomeFragmentPagerAdapter.class);
            newintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(newintent);
        }
    }
}
