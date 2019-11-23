package kr.ac.mju.capston.whatisthisdog.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

import java.io.Serializable;

import static android.content.Context.MODE_PRIVATE;

public class DogInfo implements Serializable {
    private String dogImage; //사진 경로
    private String name; //품종
    private String lifeSpan; //수명
    private String weight; //무게
    private String size; //크기
    private String tendency; //성격
    private String desc; //상세정보
    private String matchRate; //일치율

    private String saveData;

    public DogInfo(String saveData, Context context){

        this.saveData = saveData;
        //Data format : 사진경로#품종#수명#무게#크기#성격#상세정보#일치율

        String[] array = saveData.split("#");
        this.dogImage = array[0];
        this.name = array[1];
        this.lifeSpan = array[2];
        this.weight = array[3];
        this.size = array[4];
        this.tendency = array[5];
        this.desc = array[6];


        this.matchRate = "0";
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
        this.saveData = dogImage+"#"+name+"#"+lifeSpan+"#"+weight+"#"+size+"#"+tendency+"#"+desc+"#"+matchRate;
        return saveData;
    }

    public void setSaveData(String saveData) {
        this.saveData = saveData;
    }

    public String getlifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(String lifeSpan) { this.lifeSpan = lifeSpan; }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) { this.weight = weight; }

    public String getSize() {
        return size;
    }

    public void setSize(String lifeSpan) { this.size = size; }

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
        result += ("#" + "lifeSpan" + String.valueOf(time%10) );
        result += ("#" + "weight" + String.valueOf(time%10) );
        result += ("#" + "size" + String.valueOf(time%10) );
        result += ("#" + "tendency" + String.valueOf(time%10) );
        result += ("#" + "desc" + String.valueOf(time));
        result += ("#" +  String.valueOf(time%100));

        return result;
    }
}
