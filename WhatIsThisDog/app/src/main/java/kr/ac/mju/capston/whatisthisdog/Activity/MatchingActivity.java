package kr.ac.mju.capston.whatisthisdog.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import kr.ac.mju.capston.whatisthisdog.R;

public class MatchingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar(true);
        setContentView(R.layout.activity_matching);
    }
}
