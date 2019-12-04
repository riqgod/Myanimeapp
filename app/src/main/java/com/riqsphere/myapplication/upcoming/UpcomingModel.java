package com.riqsphere.myapplication.upcoming;

public class UpcomingModel {
    private int image;
    private String animeTittle,epNum,epTittle,time;


    public UpcomingModel(int image, String animeTittle, String epNum, String epTittle, String time) {
        this.image = image;
        this.animeTittle = animeTittle;
        this.epNum = epNum;
        this.epTittle = epTittle;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAnimeTittle() {
        return animeTittle;
    }

    public void setAnimeTittle(String animeTittle) {
        this.animeTittle = animeTittle;
    }

    public String getEpNum() {
        return epNum;
    }

    public void setEpNum(String epNum) {
        this.epNum = epNum;
    }

    public String getEpTittle() {
        return epTittle;
    }

    public void setEpTittle(String epTittle) {
        this.epTittle = epTittle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



}
