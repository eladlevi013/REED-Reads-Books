package com.minhalreads.androidenglishreadingtimer.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.helpers.SharedPreferencesManager;

public class Goodbye extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);

        TextView tvMessage = findViewById(R.id.text);
        String fullName = SharedPreferencesManager.getFullName(this);
        tvMessage.setText(fullName + ", " + getString(R.string.goodbye_page_text));
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAffinity();
                System.exit(0);
            }
        }, SPLASH_TIME_OUT);
    }
}
