package com.example.matches;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Matched_Activity extends AppCompatActivity {

    private static final int ACT_NUM = 1;
    private Context mContext = Matched_Activity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched);
        setupNav();

    }

    private void setupNav() {
        BottomNavigationView tvEx = findViewById(R.id.topNavViewBar);
        Navigation.setupNavigation(tvEx);
        Navigation.enableNavigation(mContext, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(ACT_NUM);
        menuItem.setChecked(true);
    }
    @Override
    public void onBackPressed() {

    }
}
