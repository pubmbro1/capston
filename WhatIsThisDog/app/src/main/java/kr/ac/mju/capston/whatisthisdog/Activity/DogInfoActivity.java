package kr.ac.mju.capston.whatisthisdog.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.ac.mju.capston.whatisthisdog.Data.DogInfo;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;
import kr.ac.mju.capston.whatisthisdog.R;

public class DogInfoActivity extends BaseActivity {

    private DogInfo item;

    private ImageView imageView;
    private TextView name;
    private TextView desc;

    private String called = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true);
        setContentView(R.layout.activity_dog_info);

        //인텐트로 선택된 강아지 받아오기
        Intent intent = getIntent();
        item = (DogInfo) intent.getExtras().getSerializable("dogitem");
        called = intent.getExtras().getString("call");

        imageView = findViewById(R.id.info_dogimage);
        name = findViewById(R.id.info_name);
        desc = findViewById(R.id.info_desc);

        //경로로 이미지 불러오기
        if(called.equals("album")) {
            String photoPath = FileManager.getPath() + "/" + item.getDogImage();
            Glide.with(this)
                    .load(photoPath)
                    .into(imageView);
        }
        if(called.equals("dict")){
            int resId = getResources().getIdentifier(item.getDogImage(), "drawable", getPackageName());
            Log.d("resid" , item.getDogImage());

            Glide.with(this)
                    .load(resId)
                    .into(imageView);
        }
        name.setText(item.getName());
        desc.setText(item.getDesc());
    }
}
