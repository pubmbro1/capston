package kr.ac.mju.capston.whatisthisdog.Util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;

public class FileManager {

    private static final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/WhatIsThisDog");
    private File file;
    private Context context;

    public FileManager(Context context, String fileName){
        this.context = context;
        setFile(fileName);
    }

    public boolean getFileExists(){
        return file.exists();
    }

    public static File getPath() {
        return path;
    }

    public void setFile(String fileName){
        file = new File(path, fileName);
    }

    //파일에 쓰기
    public void saveItemsToFile(DogInfo item) {

        FileWriter fw = null ;
        BufferedWriter bufwr = null ;

        try {
            // open file.

            //존재하면 이어쓰기
            if (file.exists())
                fw = new FileWriter(file, true);
            else
                fw = new FileWriter(file, false);

            bufwr = new BufferedWriter(fw) ;

            //임시 작성
            bufwr.write(item.getSaveData());
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
                    //리스트에 추가
                    resultList.add(new DogInfo(str));
                }

                bufrd.close() ;
                fr.close() ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }

        return resultList;
    }

    //전체 리스트와 삭제할 item을 넘겨받아 item 삭제, file 변경
    public void deleteAlbumFile(DogInfo item, ArrayList<DogInfo> albumlist){
        //이미지 삭제
        String fileName = item.getDogImage();
        File imageFile = new File(path, fileName);
        imageFile.delete();

        //리스트에서 item 삭제
        ArrayList<DogInfo> resultList = new ArrayList<>();
        resultList.addAll(albumlist);

        if( !(resultList.remove(item)))
            Log.d("deleteAlbumFile" , "아이템 삭제 오류" + item.getSaveData());

        Collections.reverse(resultList);

        //변경된 리스트 다시 쓰기
        file.delete(); // 기존 album.txt 삭제
        for( DogInfo dog : resultList){
            saveItemsToFile(dog);
        }
    }



}
