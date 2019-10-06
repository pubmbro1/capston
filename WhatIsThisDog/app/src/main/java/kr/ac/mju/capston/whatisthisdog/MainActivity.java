/*  (1.0)   - 10/06 : 카메라 캡쳐 후 이미지 저장, 강아지 정보 class 및 리스트뷰 UI 구현
*           - 10/07 : 파일 정보 txt 저장
* */
package kr.ac.mju.capston.whatisthisdog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button b_camera;
    private Button b_matching;
    private Button b_dictionary;
    private Button b_album;
    private Button b_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //버전에 따라 권한 체크
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
                Intent intent = new Intent();
                switch(view.getId()){
                    case R.id.b_camera :
                        intent = new Intent(MainActivity.this, CameraActivity.class);
                        break;
                    case R.id.b_matching :
                        intent = new Intent(MainActivity.this, MatchingActivity.class);
                        break;
                    case R.id.b_dictionary :
                        intent = new Intent(MainActivity.this, DictActivity.class);
                        break;
                    case R.id.b_album :
                        intent = new Intent(MainActivity.this, AlbumActivity.class);
                        break;
                    case R.id.b_category :
                        intent = new Intent(MainActivity.this, CategoryActivity.class);
                        break;
                }
                startActivity(intent);
            }
        };

        b_camera.setOnClickListener(onClickListener);
        b_matching.setOnClickListener(onClickListener);
        b_dictionary.setOnClickListener(onClickListener);
        b_album.setOnClickListener(onClickListener);
        b_category.setOnClickListener(onClickListener);

        //이개뭐개 디렉토리 생성
        FileManager fm = new FileManager();
        if(!fm.getPath().mkdir()){
            Log.d("dir create" , "fail");
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














