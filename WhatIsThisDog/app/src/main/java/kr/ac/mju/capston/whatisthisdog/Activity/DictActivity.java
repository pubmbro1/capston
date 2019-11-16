package kr.ac.mju.capston.whatisthisdog.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
            if(i%5==0){
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

        dictList = new ArrayList<>();

        for(int i=0;i<getResources().getInteger(R.integer.dict_size);i++) {
            int resId = getResources().getIdentifier("dog" + String.valueOf(i), "string", getPackageName());
            String data = getResources().getString(resId);
            DogInfo newData = new DogInfo(data);
            dictList.add(newData);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("dictList", dictList);
    }
}
