package kr.ac.mju.capston.whatisthisdog.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;
import kr.ac.mju.capston.whatisthisdog.Util.ListViewAdapter;

public class AlbumActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ListView listView;
    private ListViewAdapter adapter_myAlbum;
    private ArrayList<DogInfo> albumlist;

    private SwipeRefreshLayout refreshLayout;

    private FileManager fm;

    //delete
    private boolean del_mode = false;
    private DogInfo del_temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true, "Album");

        fm = new FileManager(this, "album.txt");

        if(savedInstanceState != null)
            albumlist = (ArrayList<DogInfo>) savedInstanceState.getSerializable("albumlist");
        else
            albumlist = loadReverseListFromFile();

        if(checkEmpty())
            setContentView(R.layout.activity_empty_album);
        else {
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

                if (del_mode) {
                    final DogInfo item = (DogInfo) parent.getItemAtPosition(position);
                    final View select = v.findViewById(R.id.select);
                    select.setVisibility(View.VISIBLE);
                    select.bringToFront();

                    AlertDialog.Builder builder = new AlertDialog.Builder(AlbumActivity.this);
                    builder.setTitle(item.getDogImage());
                    builder.setMessage("정말로 삭제할까요?");
                    builder.setPositiveButton("네",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    del_temp = item;

                                    fm.deleteAlbumFile(del_temp, albumlist);
                                    albumlist.remove(del_temp);

                                    adapter_myAlbum.setList(albumlist);
                                    adapter_myAlbum.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_LONG).show();
                                    del_temp = null;
                                    select.setVisibility(View.INVISIBLE);

                                    if(checkEmpty())
                                        setContentView(R.layout.activity_empty_album);
                                }
                            });
                    builder.setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    select.setVisibility(View.INVISIBLE);
                                    del_temp = null;
                                }
                            });
                    builder.show();


                } else {
                    DogInfo item = (DogInfo) parent.getItemAtPosition(position);
                    Intent intent = new Intent(AlbumActivity.this, DogInfoActivity.class);
                    intent.putExtra("dogitem", item);
                    intent.putExtra("call", "album");

                    startActivity(intent);
                }
            }
        });

        //새로고침
        refreshLayout = findViewById(R.id.swipelayout);
        refreshLayout.setOnRefreshListener(this);
    }

    public boolean checkEmpty(){
        if(albumlist.size() == 0 || albumlist == null)
            return true;
        return false;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete :
                if(del_mode) { //선택한 아이템 삭제
                    del_mode = false;
                    item.setIcon(getDrawable(R.drawable.icon_delete));
                }
                else { //삭제 모드로 변경
                    del_mode = true;
                    item.setIcon(getDrawable(R.drawable.icon_check));
                    del_temp = null;
                }
                return true ;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}
