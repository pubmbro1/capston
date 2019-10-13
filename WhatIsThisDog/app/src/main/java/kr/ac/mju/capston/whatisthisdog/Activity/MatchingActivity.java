package kr.ac.mju.capston.whatisthisdog.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;
import kr.ac.mju.capston.whatisthisdog.Util.ListViewAdapter;

public class MatchingActivity extends BaseActivity {

    private ListView listView;
    private ListViewAdapter adapter_rank;
    private ArrayList<DogInfo> ranklist;

    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true);
        setContentView(R.layout.activity_matching);


        /* 매칭 알고리즘 구현시 수정
        fm = new FileManager(this, "rank.txt");

        if(savedInstanceState != null)
            ranklist = (ArrayList<DogInfo>) savedInstanceState.getSerializable("albumlist");
        else
            ranklist = loadReverseListFromFile();
         */

        //임시코드
        ranklist = new ArrayList<>();
        ranklist.add(new DogInfo(DogInfo.getRandomData("dict_test_puppy")));
        ranklist.add(new DogInfo(DogInfo.getRandomData("dict_test_puppy")));
        ranklist.add(new DogInfo(DogInfo.getRandomData("dict_test_puppy")));

        init();
    }

    private void init(){
        adapter_rank = new ListViewAdapter(R.layout.listview_rank_item);
        adapter_rank.setList(ranklist);

        listView = (ListView) findViewById(R.id.rankListView);
        listView.setAdapter(adapter_rank);

        //리스트 클릭 이벤트 처리
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                DogInfo item = (DogInfo) parent.getItemAtPosition(position) ;

                Intent intent = new Intent(MatchingActivity.this, DogInfoActivity.class);
                intent.putExtra("dogitem" , item);
                intent.putExtra("call", "match");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable("ranklist", ranklist);
    }
}
