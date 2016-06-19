package com.chaohu.wemana.model;

/**
 * Created by chaohu on 2016/4/24.
 */
public class UserData {
    private String height;
    private String weight;
    /** 0==male 1==female*/
    private int sex;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

}
