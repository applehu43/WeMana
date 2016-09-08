package com.chaohu.wemana.fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaohu.wemana.R;
import com.chaohu.wemana.adapter.SettingListAdapter;
import com.chaohu.wemana.model.DBColumn;
import com.chaohu.wemana.model.SettingInfo;
import com.chaohu.wemana.utils.BMIDemo;
import com.chaohu.wemana.utils.DBOpenHelper;
import com.chaohu.wemana.utils.FileHelper;
import com.chaohu.wemana.utils.MyDateFormatUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chaohu on 2016/3/30.
 */
public class SettingsFragment extends Fragment {

    private final String height_not_found = "000";
    private final String height_uit = "CM";
    private final String weight_not_found = "000.0";
    private final String weight_unit = "KG";

    private TextView bmiShow;
    private TextView bmiName;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_wemana_settings, container, false);

        ListView listView = (ListView) view.findViewById(R.id.setting_list);

        final ArrayList<SettingInfo> settingInfos = new ArrayList<>();

        FileHelper fileHelper = new FileHelper(getActivity().getApplicationContext());
        String text_val = fileHelper.readFromSD(FileHelper.TXT_NAME);
        String[] result_val = text_val.split(",");
        if (result_val.length != 2) {
            SettingInfo setInfo = new SettingInfo();
            setInfo.setName("height");
            setInfo.setText(height_not_found);
            setInfo.setUnit(height_uit);
            settingInfos.add(setInfo);

            SettingInfo setInfo1 = new SettingInfo();
            setInfo1.setName("target weight");
            setInfo1.setText(weight_not_found);
            setInfo1.setUnit(weight_unit);
            settingInfos.add(setInfo1);
        } else {
            SettingInfo setInfo = new SettingInfo();

            setInfo.setName("height");
            setInfo.setText(result_val[0]);
            setInfo.setUnit(height_uit);
            settingInfos.add(setInfo);

            SettingInfo setInfo1 = new SettingInfo();
            setInfo1.setName("target weight");
            setInfo1.setText(result_val[1]);
            setInfo1.setUnit(weight_unit);
            settingInfos.add(setInfo1);
        }

        View headView = inflater.inflate(R.layout.list_setting_head_item, null, false);

        DBOpenHelper myDB = new DBOpenHelper(getActivity().getApplicationContext(), DBColumn.db_name, null, 1);
        // last week's avg BMI
        Cursor cursor = DBOpenHelper.queryAvgWeightByDate(myDB.getReadableDatabase(),
                MyDateFormatUtil.dateList(new Date(), 8).split(","));
        String weight;
        if (cursor.moveToNext()){
            weight = cursor.getString(cursor.getColumnIndex("avg_weight_data"));
        }else {
            weight = weight_not_found;
        }

        bmiShow = (TextView) headView.findViewById(R.id.bmi_value);
        bmiName = (TextView) headView.findViewById(R.id.bmi_name);
        if (height_not_found.equals(settingInfos.get(0).getText()) ||
                weight_not_found.equals(settingInfos.get(1).getText())
                || weight_not_found.equals(weight) || null == weight) {
            bmiShow.setText("- - -");
            bmiName.setText("- - -");
        } else {
            try {
                BMIDemo bmiDemo = new BMIDemo(settingInfos.get(0).getText(),
                        weight);
                bmiShow.setText(bmiDemo.calculateBMI().toString());
                String bmistr = bmiDemo.indexOfBMI(bmiDemo.calculateBMI());
                bmiName.setText(bmistr);
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity().getApplicationContext(), "input must be numbers", Toast.LENGTH_SHORT).show();
                return view;
            }
            int color;
            switch ((String)bmiName.getText()) {
                case BMIDemo.BMI_THIN:
                    color = Color.LTGRAY;
                    break;
                case BMIDemo.BMI_NORMAL:
                    color = Color.MAGENTA;
                    break;
                case BMIDemo.BMI_FAT:
                    color = Color.RED;
                    break;
                case BMIDemo.BMI_OBESITY:
                    color = Color.CYAN;
                    break;
                default:
                    color = Color.BLUE;
                    break;
            }
            bmiShow.setTextColor(color);
            bmiName.setTextColor(color);
        }

        View footView = inflater.inflate(R.layout.list_setting_foot_item, null, false);

        Button saveButton = (Button) footView.findViewById(R.id.set_save_button);
        Button editButton = (Button) footView.findViewById(R.id.set_reset_button);
        final EditText editText = (EditText) inflater.inflate(R.layout.list_setting_item, null, false)
                .findViewById(R.id.setting_data);
        editText.setFocusable(false);
        editText.setClickable(false);
        editText.setSelected(false);

        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "you can edit now~", Toast.LENGTH_SHORT).show();
                editText.setFocusable(true);
                editText.setClickable(true);
                editText.setSelected(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (height_not_found.equals(editText.getText())
//                        || weight_not_found.equals(editText.getText())) {
//                    return;
//                } else {
                StringBuffer sb = new StringBuffer();
                for (SettingInfo info : settingInfos) {
                    sb.append(info.getText());
                    sb.append(',');
                }
                FileHelper fileHelper = new FileHelper(getActivity().getApplicationContext());
                String saveResult = fileHelper.saveDataToSD(FileHelper.TXT_NAME, sb.substring(0, sb.length() - 1));
                if ("OK".equals(saveResult)) {
                    editText.setFocusable(false);
                    editText.setClickable(false);
                    editText.setSelected(false);
                    Toast.makeText(getActivity().getApplicationContext(), "save success~", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "save fail...", Toast.LENGTH_SHORT).show();
                }
            }
//            }
        });

        listView.addHeaderView(headView);
        listView.addFooterView(footView);

        SettingListAdapter listAdapter = new SettingListAdapter(settingInfos, getActivity().getApplicationContext());
        listView.setAdapter(listAdapter);
        return view;
    }

}
