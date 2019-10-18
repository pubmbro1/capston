package kr.ac.mju.capston.whatisthisdog.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import kr.ac.mju.capston.whatisthisdog.R;
import kr.ac.mju.capston.whatisthisdog.Util.FileManager;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ImageButton b_camera;
    private ImageButton b_matching;
    private ImageButton b_dictionary;
    private ImageButton b_album;
    private ImageButton b_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(false);
        setContentView(R.layout.activity_main);

        //버전 권한 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        init();
    }

    private void init(){

        //Button Set
        b_camera = findViewById(R.id.b_camera);
        b_matching = findViewById(R.id.b_matching);
        b_dictionary = findViewById(R.id.b_dictionary);
        b_album = findViewById(R.id.b_album);
        b_category = findViewById(R.id.b_category);

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Change Activity
                Intent intent;
                switch(view.getId()){
                    case R.id.b_camera :
                        intent = new Intent(MainActivity.this, CameraActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.b_matching :
                        intent = new Intent(MainActivity.this, MatchingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.b_dictionary :
                        intent = new Intent(MainActivity.this, DictActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.b_album :
                        intent = new Intent(MainActivity.this, AlbumActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.b_category :
                        intent = new Intent(MainActivity.this, CategoryActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        };

        b_camera.setOnClickListener(onClickListener);
        b_matching.setOnClickListener(onClickListener);
        b_dictionary.setOnClickListener(onClickListener);
        b_album.setOnClickListener(onClickListener);
        b_category.setOnClickListener(onClickListener);

        if(!FileManager.getPath().mkdir()){
            Log.d("dir create" , "fail");
        }
        SharedPreferences pref = getSharedPreferences("category", MODE_PRIVATE);
        if(!pref.contains("init")){
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        }
    }

    /* 권한 처리 */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
        else{
            Toast.makeText(this,"강아지 촬영 및 저장을 위해 권한이 필요합니다",Toast.LENGTH_LONG).show();
            finish();
        }
    }
}














