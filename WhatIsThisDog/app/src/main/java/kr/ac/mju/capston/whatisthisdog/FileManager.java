package kr.ac.mju.capston.whatisthisdog;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class FileManager {

    private static final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/WhatIsThisDog");
    private File file;
    private Context context;

    public FileManager(Context context){
        this.context = context;
    }
    public FileManager(Context context, String fileName){
        this.context = context;
        setFile(fileName);
    }

    public static File getPath() {
        return path;
    }

    public void setFile(String fileName){
        file = new File(path, fileName);
    }


    //파일에 쓰기
    public void saveItemsToFile(String str) {
        FileWriter fw = null ;
        BufferedWriter bufwr = null ;

        try {
            // open file.

            //존재하면 이어쓰기
            if(file.exists())
                fw = new FileWriter(file,true) ;
            else
                fw = new FileWriter(file,false);

            bufwr = new BufferedWriter(fw) ;

            //임시 작성
            bufwr.write(str);
            bufwr.newLine();
            bufwr.flush() ;

        } catch (Exception e) {
            e.printStackTrace() ;
        }

        try {
            // close file.
            if (bufwr != null) {
                bufwr.close();
            }

            if (fw != null) {
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace() ;
        }
    }


    //파일로 부터 읽어와서 정보리스트들을 반환
    public ArrayList<DogInfo> loadItemsFromFile() {

        FileReader fr = null ;
        BufferedReader bufrd = null ;
        String str;
        ArrayList<DogInfo> resultList = new ArrayList<>();

        if (file.exists()) {
            try {
                // open file.
                fr = new FileReader(file) ;
                bufrd = new BufferedReader(fr) ;

                while ((str = bufrd.readLine()) != null) {
                    //가져온 데이터 파싱
                    String[] array = str.split("#");
                    String image = array[0];
                    String name = array[1];
                    String desc = array[2];

                    //리스트에 추가
                    resultList.add(new DogInfo(image,name, desc));
                }

                bufrd.close() ;
                fr.close() ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }

        return resultList;
    }

    //카테고리 저장 : [카테고리이름]#[선택여부]
    public void saveCategory(HashMap<String,Boolean> categorylist){
        FileWriter fw = null ;
        BufferedWriter bufwr = null ;

        try {
            // open file.
            fw = new FileWriter(file,false);
            bufwr = new BufferedWriter(fw) ;

            for(String key : categorylist.keySet()){
                bufwr.write(key + "#" + String.valueOf(categorylist.get(key)));
                bufwr.newLine();
            }
            bufwr.flush() ;

        } catch (Exception e) {
            e.printStackTrace() ;
        }

        try {
            // close file.
            if (bufwr != null) {
                bufwr.close();
            }

            if (fw != null) {
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace() ;
        }
    }

    //카테고리 불러오기
    public HashMap<String, Boolean> loadCategory(){
        FileReader fr = null ;
        BufferedReader bufrd = null ;
        String str;
        HashMap<String, Boolean> categoryList = new HashMap<>();

        //첫 실행 - category.txt 없는 경우
        if(!file.exists()){
            for(int i=1;i<8;i++) {  //카테고리 갯수만큼
                //categoryList를 카테고리,false로 채움 (카테고리 이름 string.xml 저장 및 변경)
                int categoryID = context.getResources().getIdentifier( "category" + String.valueOf(i), "string", context.getPackageName());
                categoryList.put(context.getString(categoryID), false);
            }
            saveCategory(categoryList);

            return categoryList;
        }

        try {
            // open file.
            fr = new FileReader(file) ;
            bufrd = new BufferedReader(fr) ;

            while ((str = bufrd.readLine()) != null) {
                //가져온 데이터 파싱
                String[] array = str.split("#");
                String category = array[0];
                String check = array[1];

                //리스트에 추가
                categoryList.put(category, Boolean.valueOf(check));
            }

            bufrd.close() ;
            fr.close() ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        return categoryList;
    }
}
