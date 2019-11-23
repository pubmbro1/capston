package kr.ac.mju.capston.whatisthisdog.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

    private TextView matchRate;
    private TextView tendency;
    private TextView physical;
    private TextView desc;

    private ImageButton back;

    private String called = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(false);
        setContentView(R.layout.activity_dog_info);

        //인텐트로 선택된 강아지 받아오기
        Intent intent = getIntent();
        item = (DogInfo) intent.getExtras().getSerializable("dogitem");
        called = intent.getExtras().getString("call");

        //SetUi
        imageView = findViewById(R.id.info_dogimage);
        name = findViewById(R.id.info_name);
        matchRate =findViewById(R.id.info_matchrate);
        tendency = findViewById(R.id.info_tendency);
        physical =findViewById(R.id.info_physical);
        desc = findViewById(R.id.info_desc);
        back = findViewById(R.id.b_info_back);

        //Button set
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //경로로 이미지 불러오기
        if(called.equals("album")) {
            String photoPath = FileManager.getPath() + "/" + item.getDogImage();

            Glide.with(this)
                    .load(photoPath)
                    .placeholder(R.drawable.test_puppy_icon)
                    .error(R.drawable.icon_sadpuppy)
                    .fitCenter()
                    .into(imageView);
        }
        else if(called.equals("dict") || called.equals("match") ){
            int resId = getResources().getIdentifier(item.getDogImage(), "drawable", getPackageName());
            Log.d("resid" , item.getDogImage());

            Glide.with(this)
                    .load(resId)
                    .placeholder(R.drawable.test_puppy_icon)
                    .error(R.drawable.icon_sadpuppy)
                    .fitCenter()
                    .into(imageView);
        }
        else if(called.equals("camera")){ //카메라 액티비티에서 보낸 경우 (수정 필요)
            String photoPath = FileManager.getPath() + "/" + item.getDogImage();
            Glide.with(this)
                    .load(photoPath)
                    .placeholder(R.drawable.test_puppy_icon)
                    .error(R.drawable.icon_sadpuppy)
                    .fitCenter()
                    .into(imageView);
        }

        name.setText(item.getName());
        matchRate.setText(item.getMatchRate() + "%");
        tendency.setText(item.getTendency());
        physical.setText("수명 : " + item.getlifeSpan() + "\n체중 : " + item.getWeight() + "\n크기 : " + item.getSize());
        desc.setText(item.getDesc());

        back.bringToFront();
    }

}
