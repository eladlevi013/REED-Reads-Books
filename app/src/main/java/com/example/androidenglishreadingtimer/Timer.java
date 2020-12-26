package com.example.androidenglishreadingtimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Calendar;

public class Timer extends AppCompatActivity {

    ArrayList<Result> GlobalArrayList;
    Chronometer chron;
    Button btn, btn_rest;

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ResultList", null);
        Type type = new TypeToken<ArrayList<Result>>() {}.getType();
        GlobalArrayList = gson.fromJson(json, type);
        if(GlobalArrayList == null) {
            //Toast.makeText(this, "Creating A new one!", Toast.LENGTH_SHORT).show();
            GlobalArrayList = new ArrayList<>();
        }
//        else
//            Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class));
                        overridePendingTransition(0,0);
                        return true;

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
                    btn_rest.setEnabled(true);
                    chron.stop();
                    btn.setEnabled(false);
                    long time = SystemClock.elapsedRealtime() - chron.getBase();
                    //Toast.makeText(getContext(), (int) time, Toast.LENGTH_SHORT).show();
                    Intent activityA = new Intent(Timer.this, Score.class);
                    activityA.putExtra("time",chron.getText());
                    //long elapsedMillis = SystemClock.elapsedRealtime() - chron.getBase();
                    //Toast.makeText(getContext(), (String) (SystemClock.elapsedRealtime() - chron.getBase()), Toast.LENGTH_SHORT).show();
                    //activityA.putExtra(chron)

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
                    Toast.makeText(Timer.this, GlobalArrayList.toString(), Toast.LENGTH_SHORT).show();

                    startActivity(activityA);
                } else {
                    chron.setBase(SystemClock.elapsedRealtime());
                    chron.start();
                    btn.setText("Stop");
                }
            }
        });
    }
}