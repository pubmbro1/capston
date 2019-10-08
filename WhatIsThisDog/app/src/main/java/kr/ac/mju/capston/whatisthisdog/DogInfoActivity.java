package kr.ac.mju.capston.whatisthisdog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DogInfoActivity extends AppCompatActivity {

    private DogInfo item;

    private ImageView imageView;
    private TextView name;
    private TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog_info);

        //인텐트로 선택된 강아지 받아오기
        Intent intent = getIntent();
        item = (DogInfo) intent.getExtras().getSerializable("dogitem");

        imageView = findViewById(R.id.info_dogimage);
        name = findViewById(R.id.info_name);
        desc = findViewById(R.id.info_desc);

        //경로로 이미지 불러오기, 사전에서 호출시 변경해야함 (예정)
        String photoPath = FileManager.getPath() + "/" + item.getDogImage();
        Glide.with(this)
                .load(photoPath)
                .into(imageView);
        name.setText(item.getName());
        desc.setText(item.getDesc());
    }
}
