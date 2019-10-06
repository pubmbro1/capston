package kr.ac.mju.capston.whatisthisdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ListView;

public class DictActivity extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter adapter_Dict;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        adapter_Dict = new ListViewAdapter();
        listView = (ListView) findViewById(R.id.dictlistView);
        listView.setAdapter(adapter_Dict);


        //테스트 데이터 입력
        adapter_Dict.addItem(ContextCompat.getDrawable(this,R.drawable.test_puppy_icon),
                "dog1", "dog1 info");
        adapter_Dict.addItem(ContextCompat.getDrawable(this,R.drawable.test_puppy_icon),
                "dog2", "dog2 info");
        adapter_Dict.addItem(ContextCompat.getDrawable(this,R.drawable.test_puppy_icon),
                "dog3", "dog3 info");
        adapter_Dict.addItem(ContextCompat.getDrawable(this,R.drawable.test_puppy_icon),
                "dog4", "dog4 info");



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
