package com.chaohu.wemana.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.chaohu.wemana.model.UserData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chaohu on 2016/4/4.
 */
public class FileHelper {
    public static final String ACTION_UPDATE = "update_user_all";
    // 文件保存数据顺序是 身高(CM),体重(KG)
    public static final String TXT_NAME = "expectWeightHeight.txt";
    private Context mContext;

    public FileHelper() {
    }

    public FileHelper(Context context) {
        super();
        this.mContext = context;
    }

    /**
     * 文件保存的方法 写入到文件 输出流
     */
    public String saveDataToDataFiles(String filename, String filecontent) {
        // MODE_PRIVATE:私有模式创建的文件只能被本应用访问 还会覆盖原文件
        // MODE_APPEND:模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件
        // 往手机内存中写数据
        // FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_APPEND);
        FileOutputStream fos;
        try {
            try {
                fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                // 将String字符串以字节流的形式写入输出流
                fos.write(filecontent.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                return "fileNotFound";
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "读取文件异常", Toast.LENGTH_SHORT);
            return e.getLocalizedMessage();

        }
        return "OK";
    }

    /**
     * 文件保存的方法 写入到文件 输出流
     */
    public String saveDataToSD(String filename, String filecontent) {
        FileOutputStream fos;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                filename = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + filename;
                try {
                    fos = new FileOutputStream(filename);
                    // 将String字符串以字节流的形式写入输出流
                    fos.write(filecontent.getBytes());
                    fos.close();
                } catch (FileNotFoundException e) {
                    return "fileNotFound";
                }

            } else {
                Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT);
                return "SDNotFound";
            }

        } catch (IOException e) {
            return e.getLocalizedMessage();

        }
        return "OK";
    }

    public String readFromDataFiles(String filename) {
        StringBuffer sb = new StringBuffer("");
        FileInputStream fis;
        try {
            fis = new FileInputStream(filename);
            byte[] temp = new byte[1024];

            int len;
            while ((len = fis.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            fis.close();
        } catch (FileNotFoundException e) {
            return "fileNotFound";
        } catch (IOException e) {
            return e.getLocalizedMessage();
        }
        return sb.toString();
    }

    public String readFromSD(String filename) {
        StringBuffer sb = new StringBuffer("");
        FileInputStream fis;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                filename = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + filename;

                fis = new FileInputStream(filename);
                byte[] temp = new byte[1024];

                int len = 0;
                while ((len = fis.read(temp)) > 0) {
                    sb.append(new String(temp, 0, len));
                }
                fis.close();
            } catch (FileNotFoundException e) {
                return "fileNotFound";
            } catch (IOException e) {
                return e.getLocalizedMessage();
            }
        }
        return sb.toString();
    }

    public UserData heightAndWeight(){
        String txt_value = readFromSD(TXT_NAME);
        String[] values = txt_value.split(",");
        if (values.length != 2){
            return null;
        }
        UserData info = new UserData();
        info.setHeight(values[0]);
        info.setWeight(values[1]);
        return info;
    }
}
