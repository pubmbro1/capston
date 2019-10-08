package kr.ac.mju.capston.whatisthisdog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class FileManager {

    private static final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/WhatIsThisDog");
    private File file;

    public FileManager(){}
    public FileManager(String fileName){
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

            /*
            for (String str : items) {
                bufwr.write(str) ;
                bufwr.newLine() ;
            }
            */

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
}
