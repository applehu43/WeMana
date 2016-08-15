package com.chaohu.wemana.utils;

import com.chaohu.wemana.model.UserData;

import java.math.BigDecimal;

/**
 * Created by chaohu on 2016/4/11.
 */
public class BMIDemo {
    public BMIDemo(BigDecimal height, BigDecimal weight) {
        this.targetWeight = weight;
        this.bodyHeight = height;
    }

    public BMIDemo(String height, String weight) {
        this.targetWeight = new BigDecimal(weight);
        this.bodyHeight = new BigDecimal(height);
    }

    public BMIDemo(UserData data){
        if(data == null){
            this.targetWeight = null;
            this.bodyHeight = null;
        }else {
            this.targetWeight = new BigDecimal(data.getWeight());
            this.bodyHeight = new BigDecimal(data.getHeight());
        }
    }

    /**
     * from user's settings
     */
    private BigDecimal targetWeight;
    private BigDecimal bodyHeight;

    /*
     * the top of the separator from the as-known standard
     */
    /**
     * if weight is less 18.5 then thin
     */
    public static final BigDecimal BMI_THIN_MAX = new BigDecimal("18.5");
    /**
     * if 18.5<= weight <23.9 then normal
     */
    public static final BigDecimal BMI_NORMAL_MAX = new BigDecimal("23.9");
    /**
     * if 23.9<= weight <27.9 then fat
     */
    public static final BigDecimal BMI_FAT_MAX = new BigDecimal("27.9");
    /**
     * if weight is over 27.9 then obesity
     */
    public static final BigDecimal BMI_OBESITY_MIN = new BigDecimal("27.9");
    /**
     * if weight is less 18.5 then thin
     */
    public static final String BMI_THIN = "thin";
    /**
     * if 18.5<= weight <23.9 then normal
     */
    public static final String BMI_NORMAL = "normal";
    /**
     * if 23.9<= weight <27.9 then fat
     */
    public static final String BMI_FAT = "fat";
    /**
     * if weight is over 27.9 then obesity
     */
    public static final String BMI_OBESITY = "obesity";

    /**
     * 体质指数（BMI）=体重（kg）÷身高^2（m）
     *
     * @return
     */
    public BigDecimal calculateBMI() {
        return getTargetWeight().divide(
                getBodyHeight().divide(BigDecimal.valueOf(100), 2)
                        .multiply(
                                getBodyHeight().divide(BigDecimal.valueOf(100), 2)),
                1, BigDecimal.ROUND_HALF_UP);
    }

    public String indexOfBMI(BigDecimal bmi) {
        String index;
        if (bmi.compareTo(BMI_THIN_MAX) == -1) {
            index = BMI_THIN;
        } else if (bmi.compareTo(BMI_NORMAL_MAX) == -1) {
            index = BMI_NORMAL;
        } else if (bmi.compareTo(BMI_FAT_MAX) == -1) {
            index = BMI_FAT;
        } else {
            index = BMI_OBESITY;
        }
        return index;
    }

    /**
     * 23.9*身高(米)的平方
     * @return
     */
    public BigDecimal calculateUpperWeight() {
        return BMI_NORMAL_MAX.multiply(getBodyHeight().divide(BigDecimal.valueOf(100), 2)
                .multiply(getBodyHeight().divide(BigDecimal.valueOf(100), 2))
        ).setScale(1, BigDecimal.ROUND_UP);
    }

    /**
     * 18.5*身高(米)的平方
     * @return
     */
    public BigDecimal calculateLowerWeight() {
        return BMI_THIN_MAX.multiply(getBodyHeight().divide(BigDecimal.valueOf(100), 2)
                .multiply(getBodyHeight().divide(BigDecimal.valueOf(100), 2))
        ).setScale(1, BigDecimal.ROUND_DOWN);
    }

    public BigDecimal getTargetWeight() {
        return targetWeight.setScale(1, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getBodyHeight() {
        return bodyHeight.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
