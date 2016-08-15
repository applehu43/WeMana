package com.chaohu.wemana.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.chaohu.wemana.R;
import com.chaohu.wemana.model.SettingInfo;

import java.util.ArrayList;

/**
 * Created by chaohu on 2016/4/24.
 */
public class SettingListAdapter extends ArrayAdapter<SettingInfo> {

    private ArrayList<SettingInfo> settingMsgs;
    private Context mContext;

    public SettingListAdapter(ArrayList<SettingInfo> mData, Context context) {
        super(context, R.layout.list_setting_item, mData);
        this.settingMsgs = mData;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_setting_item, parent, false);
            holder.setSettingName((TextView) convertView.findViewById(R.id.setting_name));
            holder.setSettingData((EditText) convertView.findViewById(R.id.setting_data));
            holder.setSettingUnit((TextView) convertView.findViewById(R.id.setting_data_unit));

            holder.getSettingData().setTag(position);
            holder.getSettingData().addTextChangedListener(new TextChangeListener(holder));
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
            holder.getSettingData().setTag(position);
        }

        SettingInfo sm = settingMsgs.get(position);
        holder.getSettingName().setText(sm.getName());
        holder.getSettingData().setText(sm.getText());
        holder.getSettingUnit().setText(sm.getUnit());

//        this.notifyDataSetChanged();

        return convertView;
    }

    private class ViewHolder {
        private TextView settingName;
        private EditText settingData;
        private TextView settingUnit;

        public EditText getSettingData() {
            return settingData;
        }

        public void setSettingData(EditText settingData) {
            this.settingData = settingData;
        }

        public TextView getSettingName() {
            return settingName;
        }

        public void setSettingName(TextView settingName) {
            this.settingName = settingName;
        }

        public TextView getSettingUnit() {
            return settingUnit;
        }

        public void setSettingUnit(TextView settingUnit) {
            this.settingUnit = settingUnit;
        }
    }

    private class TextChangeListener implements TextWatcher{

        private ViewHolder viewHolder;
        public TextChangeListener(ViewHolder holder){
            this.viewHolder = holder;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int position = (int) viewHolder.getSettingData().getTag();
            SettingInfo entity = settingMsgs.get(position);
            entity.setText(s.toString());
        }
    }

}
