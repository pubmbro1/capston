package kr.ac.mju.capston.whatisthisdog.Activity;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import kr.ac.mju.capston.whatisthisdog.R;

public class CategoryActivity extends BaseActivity {

    private final int DEFAULT_VALUE = 3;

    private int catSize;

    private TextView textView;
    private SeekBar seekBar;
    private Button b_save;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(false);
        setContentView(R.layout.activity_category);

        catSize = getResources().getInteger(R.integer.category_size);
        pref = getSharedPreferences("category", MODE_PRIVATE);

        //check data
        if(!pref.contains("init")) {
            initCategory();
            loadCategory();
        }
        else
            loadCategory();

        //set title, save button
        textView = findViewById(R.id.category_title);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        b_save = findViewById(R.id.b_save);
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCategory();
                Toast.makeText(CategoryActivity.this,"저장 완료",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void initCategory(){
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("init", true);
        for(int i=0;i<catSize;i++)
            editor.putInt(("category" + String.valueOf(i)), DEFAULT_VALUE);
        for(int i=0;i<120;i++)
            editor.putInt(("score"+String.valueOf(i)), 0);
        editor.commit();
    }

    private void loadCategory(){
        for(int i=0;i<catSize;i++) {
            int catValue = pref.getInt(("category" + String.valueOf(i)), DEFAULT_VALUE);
            int categoryID = getResources().getIdentifier( "category" + String.valueOf(i), "id", getPackageName());
            String categoryString = getString(getResources().getIdentifier( "category" + String.valueOf(i), "string", getPackageName()));

            textView = findViewById(categoryID);
            textView.setText(categoryString);

            //카테고리 리스트에서 value 값에 따라 seekbar 조절
            int seekBarID = getResources().getIdentifier( "seekBar" + String.valueOf(i), "id", getPackageName());
            seekBar = findViewById(seekBarID);
            seekBar.setProgress(catValue-1);
        }
    }

    private void saveCategory(){
        SharedPreferences.Editor editor = pref.edit();

        int[] catValues = new int[catSize];
        double[] prefSims = new double[120];

        for(int i=0;i<catSize;i++){
            int seekBarID = getResources().getIdentifier( "seekBar" + String.valueOf(i), "id", getPackageName());
            seekBar = findViewById(seekBarID);
            editor.putInt(("category" + String.valueOf(i)), seekBar.getProgress()+1);
            editor.commit();

            //사용자 점수 읽기
            catValues[i] = pref.getInt(("category" + String.valueOf(i)), DEFAULT_VALUE);
        }



        for(int i=0;i<120-3;i++){
            String scoreString = getString(getResources().getIdentifier( "score" + String.valueOf(i), "string", getPackageName()));
            String[] score = scoreString.split("#");

            // 사용자 점수 읽기 => 위에서 수행
            // 알고리즘 생성
            double prefSim = 0, gap, bias;
            for(int j=0;j<catSize;j++){
                bias = 0;
                gap = catValues[j] - Integer.parseInt(score[j]);

                if(gap > 0)
                    bias = 0.25*gap;
                else if(gap < 0)
                    bias = 0.25*(-gap);

                prefSim += (1 - bias);
            }
            // ex) 0.1234 => 1234로 저장 => 이후 사용시 /100.0하여 12.34(%)로 사용
            // SharedPreferences에 저장
            prefSims[i] = (int)(prefSim/6*100);
            editor.putInt(("score"+String.valueOf(i)),(int)prefSims[i]);
        }
        for(int i=120-3;i<120;i++){
            editor.putInt(("score" + String.valueOf(i)), 0);
        }
        editor.commit();
    }

}