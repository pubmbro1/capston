package kr.ac.mju.capston.whatisthisdog.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.ListViewAdapter;

public class MatchingActivity extends BaseActivity {

    private ListView listView;
    private ListViewAdapter adapter_rank;
    private SharedPreferences pref;
    private ArrayList<DogInfo> ranklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true, "Matching");
        setContentView(R.layout.activity_matching);

        pref = getSharedPreferences("category", MODE_PRIVATE);

        /* 매칭 알고리즘 구현시 수정
        fm = new FileManager(this, "rank.txt");

        if(savedInstanceState != null)
            ranklist = (ArrayList<DogInfo>) savedInstanceState.getSerializable("albumlist");
        else
            ranklist = loadReverseListFromFile();
         */

        //임시코드
        ranklist = new ArrayList<>();
        int [] score = new int[120];
        int [] BestScoreIndex = {0, 1, 2};

        for(int i=0;i<120;i++) {
            score[i] = pref.getInt(("score" + String.valueOf(i)), 0);
            if(i > 2) {
                if ((score[BestScoreIndex[0]] / 100.0) < (score[i] / 100.0)) {
                    BestScoreIndex[2] = BestScoreIndex[1];
                    BestScoreIndex[1] = BestScoreIndex[0];
                    BestScoreIndex[0] = i;
                } else if ((score[BestScoreIndex[1]] / 100.0) < (score[i] / 100.0)) {
                    BestScoreIndex[2] = BestScoreIndex[1];
                    BestScoreIndex[1] = i;
                } else if ((score[BestScoreIndex[2]] / 100.0) < (score[i] / 100.0)) {
                    BestScoreIndex[2] = i;
                }
            }
        }

        for(int i=0;i<3;i++) {
            int resId = getResources().getIdentifier("dog" + String.valueOf(BestScoreIndex[i]), "string", getPackageName());
            String data = getResources().getString(resId);
            DogInfo newData = new DogInfo(data, this);

            ranklist.add(newData);
        }

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
