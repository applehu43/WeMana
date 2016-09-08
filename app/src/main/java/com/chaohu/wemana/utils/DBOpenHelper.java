package com.chaohu.wemana.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chaohu.wemana.model.DBColumn;

import java.util.List;

/**
 * Created by chaohu on 2016/4/5.
 */
public class DBOpenHelper extends SQLiteOpenHelper{

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table weight_record(" +
                "id integer primary key autoincrement," +
                "weight_data varchar(10),record_date DATE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE `weight_record` \n" +
//                "CHANGE COLUMN `weight_data` `weight_data` DECIMAL(3,1) NULL  ,\n" +
//                "CHANGE COLUMN `record_date` `record_date` DATE NULL ");
    }

    public static Cursor queryAllWeight(SQLiteDatabase db){
        StringBuffer sb = new StringBuffer("");
        sb.append("SELECT record_date,weight_data," +
                "strftime('%Y-%m',record_date) as record_month,avg(weight_data) as avg_weight_data");
        sb.append(DBColumn.from_sql);
        Cursor cursor = db.rawQuery(sb.toString(),null);
        return cursor;
    }
    public static Cursor queryWeightByDate(SQLiteDatabase db, String[] date){
        StringBuffer sb = new StringBuffer("");
        sb.append(DBColumn.select_sql);
        sb.append(DBColumn.from_sql);
        sb.append(" where ");
        sb.append(DBColumn.record_date);
        sb.append(" in ( ");
        for (int i=0; i<date.length; i++){
            sb.append("?").append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(" ) order by ");
        sb.append(DBColumn.record_date);
        Cursor cursor = db.rawQuery(sb.toString(),date);
        return cursor;
    }

    public static Cursor queryAvgWeightByDate(SQLiteDatabase db, String[] date){
        StringBuffer sb = new StringBuffer("");
        sb.append("select avg(weight_data) as avg_weight_data").append(DBColumn.from_sql).append(" where ");
        sb.append(DBColumn.record_date);
        sb.append(" in ( ");
        for (int i=0; i<date.length; i++){
            sb.append("?").append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" ) order by ");
        sb.append(DBColumn.record_date);
        Cursor cursor = db.rawQuery(sb.toString(),date);
        return cursor;
    }
    public static Cursor queryAvgWeightMonthly(SQLiteDatabase db){
        StringBuffer sb = new StringBuffer("");
        sb.append("SELECT strftime('%Y-%m',record_date) as record_month,avg(weight_data) as avg_weight_data");
        sb.append(DBColumn.from_sql);
        sb.append(" group by strftime('%Y-%m',record_date) ");
        sb.append(" order by record_month desc");
        Cursor cursor = db.rawQuery(sb.toString(),null);
        return cursor;
    }

    public static void insertInto(SQLiteDatabase db, List<ContentValues> lists){
        for (ContentValues values : lists)
            db.insert(DBColumn.table_name, null, values);
    }
    public static void updateWM(SQLiteDatabase db, String[] weight_date_data){
        // new String[]{weight_date, record_date};
        db.execSQL("update "+DBColumn.table_name+" set "+DBColumn.weight_data+" = ? where "+DBColumn.record_date+" = ?",
                weight_date_data);
    }
    public static void insertWM(SQLiteDatabase db, String[] weight_date_data){
        // new String[]{weight_date, record_date};
        db.execSQL("insert into weight_record("+DBColumn.sql_noid+") " +
                "values (?,?)", weight_date_data);
    }
    public static void delteValues(SQLiteDatabase db){
        db.delete(DBColumn.table_name, null, null);
    }
}
