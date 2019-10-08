package kr.ac.mju.capston.whatisthisdog;

import java.util.ArrayList;

public class DataSet {
    private FileManager fm;

    private ArrayList<DogInfo> albumlist;

    public DataSet(){
        fm = new FileManager();

        albumlist = loadAlbum();
    }

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
}
