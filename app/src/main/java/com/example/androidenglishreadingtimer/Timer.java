package com.example.androidenglishreadingtimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Timer extends AppCompatActivity {

    ArrayList<Result> GlobalArrayList;
    Chronometer chron;
    Button btn, btn_rest;
    TextView quote_tv;
    boolean isRunning= false;

    @Override
    protected void onResume() {
        super.onResume();

        String[] quoteArray = new String[] {"“Once you learn to read, you will be forever free.” — Frederick Douglass", "“The more that you read, the more things you will know. The more you learn, the more places you’ll go.”— Dr. Seuss, “I Can Read With My Eyes Shut!”", "“Today a reader, tomorrow a leader.” – Margaret Fuller", "“There are worse crimes than burning books.  One of them is not reading them.” – Ray Bradbury", "“Reading is to the mind what exercise is to the body.” – Richard Steele”", "“Reading is a discount ticket to everywhere.”  – Mary Schmich", "“Reading is to the mind what exercise is to the body – Joseph Addison"};

        Random random = new Random();
        int RANDOM_QUATE_NUMBER = random.nextInt(7);
        quote_tv = findViewById(R.id.quote);
        quote_tv.setText(quoteArray[RANDOM_QUATE_NUMBER]);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ResultList", null);
        Type type = new TypeToken<ArrayList<Result>>() {}.getType();
        GlobalArrayList = gson.fromJson(json, type);
        if(GlobalArrayList == null) {
            GlobalArrayList = new ArrayList<>();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveShared();
    }

    public void saveShared(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(GlobalArrayList);
        editor.putString("ResultList", json);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        //Initialize Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Timer Selected
        bottomNavigationView.setSelectedItemId(R.id.timer);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timer:
                        return true;

                    case R.id.history:
                        if(isRunning){
                            Toast.makeText(Timer.this, "You Can't Change Activity While is Timer is Running!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), History.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }

                    case R.id.about:
                        if(isRunning){
                            Toast.makeText(Timer.this, "You Can't Change Activity While is Timer is Running!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), About.class));
                            overridePendingTransition(0,0);
                            return true;
                        }

                }
                return false;
            }
        });

        chron = findViewById(R.id.timer_chrone);
        btn = findViewById(R.id.button);
        btn_rest = findViewById(R.id.button_rest);

        btn_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chron.setBase(SystemClock.elapsedRealtime());
                btn.setEnabled(true);
                btn_rest.setEnabled(false);
                chron.stop();
                btn.setText("Start");
            }
        });

        chron.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chron = chronometer;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(btn.getText() == "Stop") {
                    isRunning = false;
                    btn_rest.setEnabled(true);
                    chron.stop();
                    btn.setEnabled(false);
                    long time = SystemClock.elapsedRealtime() - chron.getBase();

                    Intent activityA = new Intent(Timer.this, Score.class);
                    activityA.putExtra("time",chron.getText());

                    double stoppedMilliseconds = 0;
                    String array[] = chron.getText().toString().split(":");
                    if (array.length == 2) {
                        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                                + Integer.parseInt(array[1]) * 1000;
                    } else if (array.length == 3) {
                        stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                                + Integer.parseInt(array[1]) * 60 * 1000
                                + Integer.parseInt(array[2]) * 1000;
                    }

                    stoppedMilliseconds = stoppedMilliseconds/60000;
                    stoppedMilliseconds = Math.floor(stoppedMilliseconds * 100) / 100;

                    GlobalArrayList.add(new Result(stoppedMilliseconds));
                    saveShared();
                    startActivity(activityA);
                } else {
                    isRunning = true;
                    chron.setBase(SystemClock.elapsedRealtime());
                    chron.start();
                    btn.setText("Stop");
                }
            }
        });
    }
}