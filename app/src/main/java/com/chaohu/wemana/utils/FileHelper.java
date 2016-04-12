package com.chaohu.wemana.utils;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by chaohu on 2016/4/4.
 */
public class FileHelper {
    private Context mContext;
    private FileOutputStream fos;

    public FileHelper() {

    }

    public FileHelper(Context context) {
        super();
        this.mContext = context;
    }

    /**
     * 文件保存的方法 写入到文件 输出流
     */
    public void saveDataToSD(String filename, String filecontent) throws IOException {
        // MODE_PRIVATE:私有模式创建的文件只能被本应用访问 还会覆盖原文件
        // MODE_APPEND:模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath() + File.separator + filename;
            // 往手机内存中写数据
            // FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_APPEND);
            FileOutputStream fos = new FileOutputStream(filename);
            // 将String字符串以字节流的形式写入输出流
            fos.write(filecontent.getBytes());
            fos.close();
        } else {
            Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT);
        }
    }

    public String readFromSD(String filename) throws IOException {
        StringBuffer sb = new StringBuffer("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getCanonicalPath()+File.separator+filename;

            FileInputStream fis = new FileInputStream(filename);
            byte[] temp = new byte[1024];

            int len = 0;
            while ((len = fis.read(temp)) > 0) {
                sb.append(new String(temp, 0, len));
            }
            fis.close();
        }
        return sb.toString();
    }
}
