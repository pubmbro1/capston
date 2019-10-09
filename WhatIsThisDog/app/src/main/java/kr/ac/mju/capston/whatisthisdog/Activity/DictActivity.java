package kr.ac.mju.capston.whatisthisdog.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import kr.ac.mju.capston.whatisthisdog.Util.DictSectioningAdapter;
import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;

public class DictActivity extends BaseActivity {

    private ArrayList<DogInfo> dictList;

    private RecyclerView listView;
    private DictSectioningAdapter adapter_Dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true);
        setContentView(R.layout.activity_dict);

        //테스트 사전 데이터 입력
        dictList = new ArrayList<>();
        for(int i=0;i<30;i++) {
            if(i%10==0)
                dictList.add(new DogInfo("", "Tag " + String.valueOf(i/10), ""));
            else
                dictList.add(new DogInfo("test_puppy_icon", "Dog " + String.valueOf(i), "Dog info"));
        }

        //사전 데이터 Recycler 용으로 추가
        List<DictSectioningAdapter.Item> data = new ArrayList<>();
        for(int i=0;i<30;i++) {
            if(i%10==0)
                data.add(new DictSectioningAdapter.Item(DictSectioningAdapter.HEADER, dictList.get(i)));
            else
                data.add(new DictSectioningAdapter.Item(DictSectioningAdapter.CHILD, dictList.get(i)));
        }


        listView = (RecyclerView) findViewById(R.id.dictListView);

        listView.setLayoutManager(new LinearLayoutManager(this));

        adapter_Dict = new DictSectioningAdapter(data, this);
        listView.setAdapter(adapter_Dict);




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
