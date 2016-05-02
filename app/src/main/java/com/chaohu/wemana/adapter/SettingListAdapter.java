package com.chaohu.wemana.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chaohu.wemana.R;
import com.chaohu.wemana.model.SettingMsg;

import java.util.ArrayList;

/**
 * Created by chaohu on 2016/4/24.
 */
public class SettingListAdapter extends ArrayAdapter<SettingMsg> {

    private ArrayList<SettingMsg> settingMsgs;
    private Context mContext;

    public SettingListAdapter(ArrayList<SettingMsg> mData, Context context) {
        super(context, 0, mData);
        this.settingMsgs = mData;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingMsg sm = getItem(position);
        ViewHolder holder;

        holder = new ViewHolder();
        convertView = LayoutInflater.from(mContext).inflate(R.layout.list_setting_item, parent, false);
        holder.setSettingName((TextView) convertView.findViewById(R.id.setting_name));
        holder.setSettingData((TextView) convertView.findViewById(R.id.setting_data));

        convertView.setTag(holder);
        holder.getSettingName().setText(sm.getName());
        holder.getSettingData().setText(sm.getText());

        return convertView;
    }

    private class ViewHolder {
        private TextView settingName;
        private TextView settingData;

        public TextView getSettingData() {
            return settingData;
        }

        public void setSettingData(TextView settingData) {
            this.settingData = settingData;
        }

        public TextView getSettingName() {
            return settingName;
        }

        public void setSettingName(TextView settingName) {
            this.settingName = settingName;
        }
    }

}
