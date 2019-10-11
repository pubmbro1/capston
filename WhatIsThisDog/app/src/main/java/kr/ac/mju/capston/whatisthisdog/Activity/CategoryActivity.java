package kr.ac.mju.capston.whatisthisdog.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import java.util.HashMap;

import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;

public class CategoryActivity extends BaseActivity {

    private HashMap<String, Boolean> categoryList;
    private CheckBox checkBox;

    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true);
        setContentView(R.layout.activity_category);

        fm = new FileManager(this, "category.txt");

        if(savedInstanceState != null)
            categoryList = (HashMap<String, Boolean>) savedInstanceState.getSerializable("categorylist");
        else
            categoryList = fm.loadCategory();

        //카테고리 클릭시 이벤트 처리
        View.OnClickListener categoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox = findViewById(view.getId());

                //선택시 카테고리 리스트의 해당 값 true / false
                if( checkBox.isChecked()){
                    checkBox.setBackgroundResource(R.drawable.round_color_res);
                    categoryList.put(checkBox.getText().toString(), true);
                }
                else{
                    checkBox.setBackgroundResource(0);
                    categoryList.put(checkBox.getText().toString(), false);
                }
            }
        };

        //ui set
        for(int i=1;i<8;i++){
            int categoryID = getResources().getIdentifier( "category" + String.valueOf(i), "id", getPackageName());
            String categoryString = getString(getResources().getIdentifier( "category" + String.valueOf(i), "string", getPackageName()));

            checkBox = findViewById(categoryID);
            checkBox.setText(categoryString);

            //카테고리 리스트에서 value 값에 따라 true/false
            if(categoryList.get(categoryString)){
                checkBox.setBackgroundResource(R.drawable.round_color_res);
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            checkBox.setOnClickListener(categoryClickListener);
        }
    }

    @Override
    protected void onDestroy() {
        fm.saveCategory(categoryList);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable("categorylist", categoryList);
    }
}
