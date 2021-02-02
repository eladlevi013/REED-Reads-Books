package com.minhalreads.androidenglishreadingtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class ExitActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        String FULL_NAME = sharedPreferences.getString("FULL_NAME", "Default Name");
        TextView message = findViewById(R.id.text);
        message.setText(FULL_NAME + ", Come back soon to read even more!");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                finishAffinity();
                System.exit(0);
            }
        }, SPLASH_TIME_OUT);
    }


}