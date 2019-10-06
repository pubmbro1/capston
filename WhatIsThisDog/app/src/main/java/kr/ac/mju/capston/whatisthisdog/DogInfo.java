package kr.ac.mju.capston.whatisthisdog;

import android.graphics.drawable.Drawable;

public class DogInfo {
    private Drawable dogImage;
    private String name;
    private String desc;


    public Drawable getDogImage() {
        return dogImage;
    }

    public void setDogImage(Drawable dogImage) {
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
