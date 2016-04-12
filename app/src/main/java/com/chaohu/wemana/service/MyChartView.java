package com.chaohu.wemana.service;

/**
 * Created by chaohu on 2016/4/7.
 */


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.chaohu.wemana.utils.MyDateFormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyChartView extends View {

    public MyChartView(Context context) {
        super(context);
    }

    public MyChartView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private Date userChooseDate = new Date(); //  用户选择的日期 默认为当前时间
    //  选择日期的月份 共有的天数
    private int dayOfMonth = MyDateFormatUtil.totalMonthDay(userChooseDate);
    private int XPoint = 60;    //原点的X坐标
    private int YPoint = 860;     //原点的Y坐标
    private int XScale = 8;     //X的刻度长度
    private int YScale = 10;     //Y的刻度长度

    private int XLength = XScale * dayOfMonth;        //X轴的长度
    private int YLength = 110;        //Y轴的长度
    private List<Integer> weightData = new ArrayList<Integer>();    // 存放纵坐标值

    private String[] XLabel = new String[dayOfMonth];    //X的刻度
    private String[] YLabel = new String[11];    //Y的刻度
    private String[] Data = {"077.0", "077.1", "077.8", "077.4", "077.6", "077.0", "076.2"};      //数据
    private String Title = "Daily weight";    //显示的标题

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // XLabel 循环赋值
        for (int i = 1; i <= dayOfMonth; i++) {
            String dateStr = String.valueOf(i);
            XLabel[i-1] = i < 10 ? "0" + dateStr : dateStr;
        }
/*
        canvas.drawColor(Color.BLACK);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        // 去锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.GREEN);
        // 设置轴文字大小
        paint.setTextSize(20);
        //设置Y轴
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);
        for (int i = 0; i * YScale < YLength; i++) {
            canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i * YScale, paint);
            canvas.drawText(YLabel[i], XPoint - 22, YPoint - i * YScale + 5, paint);
        }
        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength + 6, paint);
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength + 6, paint);

        // 设置X轴
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);
        for (int i = 0; i * XScale < XLength; i++) {
            canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale, YPoint - 5, paint);
            canvas.drawText(XLabel[i], XPoint + i * XScale - 10, YPoint + 20, paint);
            if (i > 0 && Data[i - 1] != "") {
                canvas.drawLine(XPoint + (i - 1) * XScale, Float.parseFloat(Data[i - 1]), XPoint + i * XScale, Float.parseFloat(Data[i]), paint);
                canvas.drawCircle(XPoint + i * XScale, Float.parseFloat(Data[i]), 2, paint);
            }
        }
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint - 3, paint);
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint + 3, paint);
        paint.setTextSize(26);

        canvas.drawText(Title, 250, 50, paint);
*/    }

    public Date getUserChooseDate() {
        return this.userChooseDate;
    }

    public void setUserChooseDate(Date clickDate) {
        this.userChooseDate = clickDate;
    }

}
