package kr.ac.mju.capston.whatisthisdog.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.ac.mju.capston.whatisthisdog.R;

public class BaseActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private boolean menu_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    public void initActionBar(boolean show){
        setCustomActionBar();
        if(show)
            actionBar.show();
        else
            actionBar.hide();
    }
    public void initActionBar(boolean show, String title){
        if(title.equals("Album"))
            menu_state = true;

        setCustomActionBar();
        if(show) {
            actionBar.setTitle(title);
            actionBar.show();
        }
        else
            actionBar.hide();
    }

    private void setCustomActionBar(){
        actionBar = getSupportActionBar();

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return menu_state ;
    }
}
