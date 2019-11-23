package kr.ac.mju.capston.whatisthisdog.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.DictSectioningAdapter;

public class DictActivity extends BaseActivity {

    private ArrayList<DogInfo> dictList;

    private RecyclerView listView;
    private DictSectioningAdapter adapter_Dict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true, "Dictionary");
        setContentView(R.layout.activity_dict);

        if(savedInstanceState != null){
            dictList = (ArrayList<DogInfo>) savedInstanceState.getSerializable("dictList");
        }
        else {
            setNewDictionaryFile();
        }

        //사전 데이터 Recycler 용으로 변경 (수정 필요)
        List<DictSectioningAdapter.Item> data = new ArrayList<>();

        for(int i=0, j=0;i<dictList.size();i++) {
            if(i == 0 || i == 6 || i == 10 || i == 13 || i == 21 || i == 28 || i == 47 || i == 62 || i == 89 || i == 93 || i == 96 || i == 107 || i == 112 || i == 119){
                int tagId = getResources().getIdentifier("tag" + String.valueOf(j) , "string", getPackageName());
                String tag = getResources().getString(tagId);
                data.add(new DictSectioningAdapter.Item(DictSectioningAdapter.HEADER, tag));
                data.add(new DictSectioningAdapter.Item(DictSectioningAdapter.CHILD, dictList.get(i)));
                j++;
            }
            else
                data.add(new DictSectioningAdapter.Item(DictSectioningAdapter.CHILD, dictList.get(i)));

        }

        listView = (RecyclerView) findViewById(R.id.dictListView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter_Dict = new DictSectioningAdapter(data, this);
        listView.setAdapter(adapter_Dict);

    }

    private void setNewDictionaryFile(){
        int dictSize = getResources().getInteger(R.integer.dict_size);
        dictList = new ArrayList<>();

        ArrayList<DogInfo> temp = new ArrayList<>();
        TreeMap<String, Integer> tm = new TreeMap<>();

        for(int i=0;i<dictSize;i++) {
            int resId = getResources().getIdentifier("dog" + String.valueOf(i), "string", getPackageName());
            String data = getResources().getString(resId);
            DogInfo newData = new DogInfo(data, this);
            temp.add(i, newData);

            tm.put(newData.getName(), i);
        }

        Iterator<String> iteratorKey = tm.keySet( ).iterator( );

        while(iteratorKey.hasNext()) {
            String name = iteratorKey.next();
            dictList.add(temp.get(tm.get(name)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("dictList", dictList);
    }
}
