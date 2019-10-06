package kr.ac.mju.capston.whatisthisdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import java.util.Iterator;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter_myAlbum;

    private Button b_add;
    private Button b_refresh;

    private int temporaryNum = 0;

    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        adapter_myAlbum = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.albumlistView);
        listView.setAdapter(adapter_myAlbum);

        fm = new FileManager();
        fm.setFile("dogInfo.txt");

        b_add = findViewById(R.id.b_add);
        b_refresh = findViewById(R.id.b_refresh);

        //불러오기
        Iterator it = fm.loadItemsFromFile().iterator();
        while(it.hasNext()){
            adapter_myAlbum.addItem(ContextCompat.getDrawable(AlbumActivity.this, R.drawable.test_puppy_icon),
                    "dog" + String.valueOf(++temporaryNum), (String)it.next());
        }

        //앨범 강아지 추가
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String testStr = "test dog info" + String.valueOf(++temporaryNum);

                fm.saveItemsToFile(testStr) ;
                adapter_myAlbum.addItem(ContextCompat.getDrawable(AlbumActivity.this,R.drawable.test_puppy_icon),
                        "dog" + String.valueOf(temporaryNum), testStr);
            }
        });
        b_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter_myAlbum.notifyDataSetChanged();
            }
        });


        /*
        //리스트 클릭 이벤트 처리 (차후 구현)

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                DogInfo item = (DogInfo) parent.getItemAtPosition(position) ;

                String titleStr = item.getName() ;
                String descStr = item.getDesc() ;
                Drawable iconDrawable = item.getDogImage() ;

                // TODO : use item data.
            }
        }) ;
        */


    }

}
