package kr.ac.mju.capston.whatisthisdog;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class DogInfo implements Serializable {
    private String dogImage;
    private String name;
    private String desc;

    public DogInfo(){

    }
    public DogInfo(String dogImage, String name, String desc){
        this.dogImage = dogImage;
        this.name = name;
        this.desc = desc;
    }

    public String getDogImage() {
        return dogImage;
    }

    public void setDogImage(String dogImage) {
        this.dogImage = dogImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
