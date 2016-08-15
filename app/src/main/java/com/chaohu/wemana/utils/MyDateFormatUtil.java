package com.chaohu.wemana.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chaohu on 2016/4/7.
 */
public class MyDateFormatUtil {
    public static final String y_m_d = "yyyy-MM-dd";
    public static final String y_m = "yyyy-MM";
    public final static int TIME_DAY_MILLISECOND = 24 * 60 * 60 * 1000;
    private static SimpleDateFormat s_d_f = new SimpleDateFormat(y_m_d);
    private static SimpleDateFormat s_d = new SimpleDateFormat(y_m);
    /**
     * get yyyy_MM_dd type date
     *
     * @return
     */
    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        return s_d_f.format(cal.getTime());
    }

    /**
     * string to date
     * @param dateStr
     * @return
     */
    public static Date strToDate(String dateStr){
        try {
            return s_d_f.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * date to string
     * @param date
     * @return
     */
    public static String dateToStr(Date date){
        return s_d_f.format(date);
    }

    /**
     * 给定日期前len天的天数集合
     * @param begin
     * @param len
     * @return
     */
    public static String dateList(Date begin, int len){
        StringBuffer datelist = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begin);
        for (int i=0; i<len; i++){
            datelist.append(s_d_f.format(calendar.getTime()));
            datelist.append(",");
            calendar.add(Calendar.DAY_OF_MONTH,-1);
        }
        return datelist.substring(0, datelist.length() - 1);
    }

    /**
     * 给定日期前len天的天数集合
     * @param begin
     * @param len
     * @return
     */
    public static String monthList(Date begin, int len){
        StringBuffer datelist = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begin);
        for (int i=0; i<len; i++){
            datelist.append(s_d.format(calendar.getTime()));
            datelist.append(",");
            calendar.add(Calendar.MONTH,-1);
        }
        return datelist.substring(0,datelist.length()-1);
    }
    /**
     * get the total days of that month according to the given time
     */
    public static int totalMonthDay(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return lastDay;
    }

    /**
     * 获得两个Date型日期之间相差的天数（第2个减第1个）
     *
     * @param first,second
     * @return int 相差的天数
     */
    public static int getDaysBetweenDates(Date first, Date second) {
        Long mils = (second.getTime() - first.getTime()) / (TIME_DAY_MILLISECOND);

        return mils.intValue();
    }

    /**
     * 判断日期是否在当前周内
     *
     * @param curDate
     * @param compareDate
     * @return
     */
    public static boolean isSameWeek(Date curDate, Date compareDate) {
        if (curDate == null || compareDate == null) {
            return false;
        }

        Calendar calSun = Calendar.getInstance();
        calSun.setTime(curDate);
        calSun.set(Calendar.DAY_OF_WEEK, 1);

        Calendar calNext = Calendar.getInstance();
        calNext.setTime(calSun.getTime());
        calNext.add(Calendar.DATE, 7);

        Calendar calComp = Calendar.getInstance();
        calComp.setTime(compareDate);
        if (calComp.after(calSun) && calComp.before(calNext)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 改进精确计算相隔天数的方法
     */
    public int getDaysBetween(Date begin, Date end) {
        Calendar d1 = Calendar.getInstance();
        Calendar d2 = Calendar.getInstance();
        d1.setTime(begin);
        d2.setTime(end);
        if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    public static void main(String[] args){
        System.out.print(monthList(new Date(),12));
    }
}
