package com.chaohu.wemana.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chaohu on 2016/4/5.
 */
public class DBOpenHelper extends SQLiteOpenHelper{

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table weight_record(" +
                "id integer primary key autoincrement," +
                "weight_data varchar(10),record_date varchar(8))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("alter table weight_record");
    }

    public static Cursor queryWeightByDate(SQLiteDatabase db, String[] date){
        // final String table_name = "weight_record";
        // final String[] colName = new String[]{"id", "weight_data", "record_date"};
        StringBuffer sb = new StringBuffer("");
        sb.append("select id, weight_data, record_date");
        sb.append(" from weight_record");
        sb.append(" where record_date = ?");
        Cursor cursor = db.rawQuery(sb.toString(),date);
        return cursor;
    }
}
