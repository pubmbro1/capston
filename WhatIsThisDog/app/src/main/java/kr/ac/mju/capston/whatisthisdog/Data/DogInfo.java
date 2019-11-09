package kr.ac.mju.capston.whatisthisdog.Data;

import java.io.Serializable;

public class DogInfo implements Serializable {
    private String dogImage; //사진 경로
    private String name; //품종
    private String physical; //신체정보
    private String tendency; //성격
    private String desc; //상세정보
    private String matchRate; //일치율

    private String saveData;

    public DogInfo(String saveData){

        this.saveData = saveData;
        //Data format : 사진경로#품종#신체정보#성격#상세정보#일치율

        String[] array = saveData.split("#");
        this.dogImage = array[0];
        this.name = array[1];
        this.physical = array[2];
        this.tendency = array[3];
        this.desc = array[4];
        this.matchRate = array[5];
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
        this.saveData = dogImage+"#"+name+"#"+physical+"#"+tendency+"#"+desc+"#"+matchRate;
        return saveData;
    }

    public void setSaveData(String saveData) {
        this.saveData = saveData;
    }

    public String getPhysical() {
        return physical;
    }

    public void setPhysical(String physical) {
        this.physical = physical;
    }

    public String getTendency() {
        return tendency;
    }

    public void setTendency(String tendency) {
        this.tendency = tendency;
    }

    public String getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(String matchRate) {
        this.matchRate = matchRate;
    }

    //임시데이터 생성
    public static String getRandomData(String image){
        long time = System.currentTimeMillis();

        String result = image;
        result += ("#" + "TEST DOG " + String.valueOf(time%10) );
        result += ("#" + "physical" + String.valueOf(time%10) );
        result += ("#" + "tendency" + String.valueOf(time%10) );
        result += ("#" + "desc" + String.valueOf(time));
        result += ("#" +  String.valueOf(time%100));

        return result;
    }
}
