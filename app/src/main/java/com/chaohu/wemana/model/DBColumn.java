package com.chaohu.wemana.model;

/**
 * Created by chaohu on 2016/6/19.
 */
public interface DBColumn {
    String id = "id";
    String weight_data = "weight_data";
    String record_date = "record_date";
    String table_name = "weight_record";
    String db_name = "weight.db";
    String select_sql = "select id,weight_data,record_date ";
    String sql_noid = "weight_data,record_date ";
    String from_sql = " from weight_record ";
}
