package kr.ac.mju.capston.whatisthisdog;

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

    public static File getPath() {
        return path;
    }

    public void setFile(String fileName){
        file = new File(path, fileName);
    }

    public void saveItemsToFile(String str) {

        FileWriter fw = null ;
        BufferedWriter bufwr = null ;

        try {
            // open file.

            //존재하면 이어쓰기모드
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


    public ArrayList<String> loadItemsFromFile() {

        FileReader fr = null ;
        BufferedReader bufrd = null ;
        String str;
        ArrayList<String> resultList = new ArrayList<>();

        if (file.exists()) {
            try {
                // open file.
                fr = new FileReader(file) ;
                bufrd = new BufferedReader(fr) ;

                while ((str = bufrd.readLine()) != null) {
                    resultList.add(str);
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
