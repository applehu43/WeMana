package com.chaohu.wemana.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chaohu.wemana.model.DBColumn;

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
        StringBuffer sb = new StringBuffer("");
        sb.append(DBColumn.select_sql);
        sb.append(DBColumn.from_sql);
        sb.append(" where ");
        sb.append(DBColumn.record_date);
        sb.append(" = ?");
        Cursor cursor = db.rawQuery(sb.toString(),date);
        return cursor;
    }
}
