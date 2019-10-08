package kr.ac.mju.capston.whatisthisdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter_myAlbum;
    private ArrayList<DogInfo> albumlist;

    private int UPDATE_CODE = RESULT_OK; //메인으로 반환할 업데이트 코드, OK = 업데이트 필요없음

    private Button b_refresh;

    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        init();
    }

    private void init(){
        //메인에서 list 받는 intent
        Intent intent = getIntent();
        albumlist = (ArrayList<DogInfo>) intent.getSerializableExtra("albumlist");
        setResult(UPDATE_CODE);

        adapter_myAlbum = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.albumlistView);
        listView.setAdapter(adapter_myAlbum);

        //인텐트로 받은 정보 연결
        adapter_myAlbum.setList(albumlist);

        //리스트 클릭 이벤트 처리 (차후 구현)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                DogInfo item = (DogInfo) parent.getItemAtPosition(position) ;

                Intent intent = new Intent(AlbumActivity.this, DogInfoActivity.class);
                intent.putExtra("dogitem" , item);
                startActivity(intent);
            }
        });

        //새로고침 (수정필요)
        fm = new FileManager("album.txt");
        b_refresh = findViewById(R.id.b_refresh);
        b_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter_myAlbum.setList(fm.loadItemsFromFile());
                adapter_myAlbum.notifyDataSetChanged();

                //새로고침
                UPDATE_CODE = RESULT_CANCELED;
                Intent intent = new Intent();
                intent.putExtra("albumlist", adapter_myAlbum.getDogInfoList());
                setResult(UPDATE_CODE, intent);
            }
        });
    }
}
