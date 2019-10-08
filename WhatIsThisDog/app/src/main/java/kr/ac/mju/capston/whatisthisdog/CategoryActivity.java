package kr.ac.mju.capston.whatisthisdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;

public class CategoryActivity extends AppCompatActivity {

    private HashMap<String, Boolean> categoryList;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //메인에서 카테고리 리스트 받음
        Intent intent = getIntent();
        categoryList = (HashMap<String, Boolean>)  intent.getExtras().getSerializable("categorylist");;

        //카테고리 클릭시 이벤트 처리
        View.OnClickListener categoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox = findViewById(view.getId());

                //선택시 카테고리 리스트의 해당 값 true / false
                if( checkBox.isChecked()){
                    categoryList.put(checkBox.getText().toString(), true);
                }
                else{
                    categoryList.put(checkBox.getText().toString(), false);
                }
            }
        };

        //xml set
        for(int i=1;i<8;i++){
            int categoryID = getResources().getIdentifier( "category" + String.valueOf(i), "id", getPackageName());
            String categoryString = getString(getResources().getIdentifier( "category" + String.valueOf(i), "string", getPackageName()));

            checkBox = findViewById(categoryID);
            checkBox.setText(categoryString);

            //카테고리 리스트에서 value 값에 따라 true/false
            if(categoryList.get(categoryString)){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            checkBox.setOnClickListener(categoryClickListener);
        }

        //액티비티 종료시 리스트를 메인에 반환
        Intent resultintent = new Intent();
        resultintent.putExtra("categorylist", categoryList);
        setResult(RESULT_OK, resultintent);
    }
}
