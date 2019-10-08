package kr.ac.mju.capston.whatisthisdog;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

public class DataSet {
    private FileManager fm;

    private ArrayList<DogInfo> albumlist;
    private HashMap<String,Boolean> categorylist;

    //
    public DataSet(Context context){
        fm = new FileManager(context);

        albumlist = loadAlbum();
        categorylist = loadcategory();
    }

    //앨범 목록 load, get, set
    private ArrayList<DogInfo> loadAlbum(){
        fm.setFile("album.txt");
        return fm.loadItemsFromFile();
    }

    public ArrayList<DogInfo> getAlbumlist() {
        return albumlist;
    }

    public void setAlbumlist(ArrayList<DogInfo> albumlist) {
        this.albumlist = albumlist;
    }


    //카테고리 목록 load, get, set
    private HashMap<String,Boolean> loadcategory(){
        fm.setFile("category.txt");
        return fm.loadCategory();
    }

    public HashMap<String, Boolean> getCategorylist() {
        return categorylist;
    }

    public void setCategorylist(HashMap<String, Boolean> categorylist) {
        this.categorylist = categorylist;
        fm.saveCategory(categorylist);
    }
}
