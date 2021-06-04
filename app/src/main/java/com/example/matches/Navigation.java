package com.example.matches;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;

import com.example.matches.Activity.MainActivity;
import com.example.matches.Activity.Matched_Activity;
import com.example.matches.Activity.Profile_Activity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class Navigation {

    public static void setupNavigation(BottomNavigationView tv) {
    }
    public static void enableNavigation(final Context context, BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_main:
                        Intent intent1 = new Intent(context, MainActivity.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.ic_matched:
                        Intent intent2 = new Intent(context, Matched_Activity.class);
                        context.startActivity(intent2);
                        break;
                    case R.id.ic_profile:
                        Intent intent3 = new Intent(context, Profile_Activity.class);
                        context.startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }
}
