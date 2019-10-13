package kr.ac.mju.capston.whatisthisdog.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;

public class CategoryActivity extends BaseActivity {

    private ArrayList<Integer> categoryList;
    private TextView textView;
    private SeekBar seekBar;

    private Button b_save;

    private FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(false);
        setContentView(R.layout.activity_category);

        fm = new FileManager(this, "category.txt");

        if(savedInstanceState != null)
            categoryList = (ArrayList<Integer>) savedInstanceState.getSerializable("categorylist");
        else
            categoryList = fm.loadCategory();

        //ui set
        textView = findViewById(R.id.category_title);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        for(int i=0;i<FileManager.C_SIZE;i++){
            int categoryID = getResources().getIdentifier( "category" + String.valueOf(i), "id", getPackageName());
            String categoryString = getString(getResources().getIdentifier( "category" + String.valueOf(i), "string", getPackageName()));

            textView = findViewById(categoryID);
            textView.setText(categoryString);

            //카테고리 리스트에서 value 값에 따라 seekbar 조절
            int seekBarID = getResources().getIdentifier( "seekBar" + String.valueOf(i), "id", getPackageName());

            seekBar = findViewById(seekBarID);
            seekBar.setProgress(categoryList.get(i));
        }

        b_save = findViewById(R.id.b_save);
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<FileManager.C_SIZE;i++){
                    String categoryString = getString(getResources().getIdentifier( "category" + String.valueOf(i), "string", getPackageName()));
                    int seekBarID = getResources().getIdentifier( "seekBar" + String.valueOf(i), "id", getPackageName());

                    seekBar = findViewById(seekBarID);

                    categoryList.set(i, seekBar.getProgress());
                }

                fm.saveCategory(categoryList);
                Toast.makeText(CategoryActivity.this,"저장 완료",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putSerializable("categorylist", categoryList);
    }
}
