package kr.ac.mju.capston.whatisthisdog.Data;

import java.io.Serializable;

public class DogInfo implements Serializable {
    private String dogImage;
    private String name;
    private String desc;

    private String saveData;

    public DogInfo(String saveData){
        String[] array = saveData.split("#");
        String image = array[0];
        String name = array[1];
        String desc = array[2];

        this.dogImage = image;
        this.name = name;
        this.desc = desc;
    }
    public DogInfo(String dogImage, String name, String desc){
        this.dogImage = dogImage;
        this.name = name;
        this.desc = desc;

        saveData = getDogImage() + "#" + getName() + "#" + getDesc();
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

    public String getSaveData() {
        return saveData;
    }

    public void setSaveData(String saveData) {
        this.saveData = saveData;
    }
}
