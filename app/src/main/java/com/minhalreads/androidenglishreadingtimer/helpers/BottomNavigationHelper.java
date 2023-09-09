package com.minhalreads.androidenglishreadingtimer.helpers;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.views.Goals;
import com.minhalreads.androidenglishreadingtimer.views.History;
import com.minhalreads.androidenglishreadingtimer.views.Timer;

public class BottomNavigationHelper {
    public static void setBottomNavigationListener(BottomNavigationView bottomNavigationView, AppCompatActivity activity) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.timer:
                    if (Timer.isRunning) {
                        return false;
                    } else {
                        activity.startActivity(new Intent(activity.getApplicationContext(), Timer.class));
                        activity.overridePendingTransition(0, 0);
                    }
                    return true;                case R.id.history:
                    if (Timer.isRunning) {
                        Toast.makeText(activity, activity.getString(R.string.change_activity_timer_running),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        activity.startActivity(new Intent(activity.getApplicationContext(), History.class));
                        activity.overridePendingTransition(0, 0);
                    }
                    return true;
                case R.id.about:
                    if (Timer.isRunning) {
                        Toast.makeText(activity, activity.getString(R.string.change_activity_timer_running),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        activity.startActivity(new Intent(activity.getApplicationContext(), Goals.class));
                        activity.overridePendingTransition(0, 0);
                    }
                    return true;
            }
            return false;
        });
    }
}
