package com.chaohu.wemana.utils;

import java.math.BigDecimal;

/**
 * Created by chaohu on 2016/4/11.
 */
public class BMIDemo {
    public BMIDemo(BigDecimal weight, BigDecimal height) {
        this.targetWeight = weight;
        this.bodyHeight = height;
    }

    public BMIDemo(double weight, double height) {
        this.targetWeight = new BigDecimal(weight);
        this.bodyHeight = new BigDecimal(height);
    }

    /**
     * from user's settings
     */
    private BigDecimal targetWeight = new BigDecimal("66.6");
    private BigDecimal bodyHeight = new BigDecimal("1.72");

    /*
     * the top of the separator from the as-known standard
     */
    /**
     * if weight is less 18.5 then thin
     */
    public static final BigDecimal BMI_THIN = new BigDecimal("18.5");
    /**
     * if 18.5<= weight <23.9 then normal
     */
    public static final BigDecimal BMI_NORMAL = new BigDecimal("23.9");
    /**
     * if 23.9<= weight <27.9 then fat
     */
    public static final BigDecimal BMI_FAT = new BigDecimal("27.9");
    /**
     * if weight is over 27.9 then obesity
     */
    public static final BigDecimal BMI_OBESITY = new BigDecimal("27.9");

    /**
     * 体质指数（BMI）=体重（kg）÷身高^2（m）
     *
     * @return
     */
    public BigDecimal calculateBMI() {
        return getTargetWeight().divide(
                getBodyHeight().multiply(getBodyHeight()))
                .setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public String indexOfBMI(BigDecimal bmi) {
        String index;
        if (bmi.compareTo(BMI_THIN) == -1) {
            index = "thin";
        } else if (bmi.compareTo(BMI_NORMAL) == -1) {
            index = "normal";
        } else if (bmi.compareTo(BMI_FAT) == -1) {
            index = "fat";
        } else {
            index = "obesity";
        }
        return index;
    }

    public BigDecimal calculateTopWeight() {
        return BMI_NORMAL.multiply(getBodyHeight().multiply(getBodyHeight())).setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateBottomWeight() {
        return BMI_THIN.multiply(getBodyHeight().multiply(getBodyHeight())).setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getTargetWeight() {
        return targetWeight.setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public void setTargetWeight(BigDecimal targetWeight) {
        this.targetWeight = targetWeight;
    }

    public BigDecimal getBodyHeight() {
        return bodyHeight.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setBodyHeight(BigDecimal bodyHeight) {
        this.bodyHeight = bodyHeight;
    }
}
