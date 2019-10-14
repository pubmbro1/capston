package kr.ac.mju.capston.whatisthisdog.Activity;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.Util.ListViewAdapter;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;
import kr.ac.mju.capston.whatisthisdog.R;

public class AlbumActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ListView listView;
    private ListViewAdapter adapter_myAlbum;
    private ArrayList<DogInfo> albumlist;

    private SwipeRefreshLayout refreshLayout;

    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true, "Album");

        fm = new FileManager(this, "album.txt");

        if(savedInstanceState != null)
            albumlist = (ArrayList<DogInfo>) savedInstanceState.getSerializable("albumlist");
        else
            albumlist = loadReverseListFromFile();

        if(albumlist.size() == 0 || albumlist == null){
            setContentView(R.layout.activity_empty_album);
        }else {
            setContentView(R.layout.activity_album);
            init();
        }
    }

    private void init(){
        adapter_myAlbum = new ListViewAdapter(R.layout.listview_album_item);
        listView = (ListView) findViewById(R.id.albumlistView);
        listView.setAdapter(adapter_myAlbum);

        adapter_myAlbum.setList(albumlist);

        //리스트 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                DogInfo item = (DogInfo) parent.getItemAtPosition(position) ;

                Intent intent = new Intent(AlbumActivity.this, DogInfoActivity.class);
                intent.putExtra("dogitem" , item);
                intent.putExtra("call", "album");

                startActivity(intent);
            }
        });

        //새로고침
        refreshLayout = findViewById(R.id.swipelayout);
        refreshLayout.setOnRefreshListener(this);
    }

    //새로고침
    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter_myAlbum.setList(loadReverseListFromFile());
                adapter_myAlbum.notifyDataSetChanged();

                refreshLayout.setRefreshing(false);
            }
        },1000);
    }

    public ArrayList<DogInfo> loadReverseListFromFile(){
        ArrayList<DogInfo> reverseList = fm.loadItemsFromFile();
        Collections.reverse(reverseList);

        return reverseList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable("albumlist", albumlist);
    }
}
