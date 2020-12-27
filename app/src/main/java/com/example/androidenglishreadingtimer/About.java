package com.example.androidenglishreadingtimer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class About extends AppCompatActivity {

    public ArrayList<Result> GlobalArrayList = null;
    TextView statsView;
    public double GOAL = 120;
    public double WEEKLY_SUM=0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private double getWeekSum(ArrayList<Result> globalArrayList) {
        double sum = 0;
        int i = 0;
        final LocalDate date = LocalDate.now();
        final LocalDate dateMinus7Days = date.minusDays(7);

        long date7beforemilli = dateMinus7Days.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        if(globalArrayList != null) {
            while (globalArrayList.size() > i && date7beforemilli < globalArrayList.get(i).getDate().toInstant().toEpochMilli()) {
                sum += globalArrayList.get(i).getChronmeter();
                i++;
            }
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getSum(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        float weekly = sharedPreferences.getFloat("Weekly", 0f);
        WEEKLY_SUM = weekly;
    }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ResultList", null);
        Type type = new TypeToken<ArrayList<Result>>() {
        }.getType();
        GlobalArrayList = gson.fromJson(json, type);
        if (GlobalArrayList == null) {
            Toast.makeText(this, "Creating A new one!", Toast.LENGTH_SHORT).show();
            GlobalArrayList = new ArrayList<>();
        }

        WEEKLY_SUM = getWeekSum(GlobalArrayList);

        statsView = findViewById(R.id.statsView);
        Button button = findViewById(R.id.setButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(About.this);
                alert.setTitle("Set A New Reading Minutes Goal For This Week");

                // Set an EditText view to get user input
                final EditText input = new EditText(About.this);
                input.setInputType(2);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if(input.getText() != null) {
                            try {

                                if(Double.parseDouble(input.getText().toString()) >= WEEKLY_SUM) {
                                    GOAL = Double.parseDouble(input.getText().toString());
                                    statsView.setText((float) (Math.floor(WEEKLY_SUM * 100) / 100) + "/ " + GOAL);
                                    createPiChart();
                                }
                            } catch (Exception e) {
                                // This will catch any exception, because they are all descended from Exception
                                System.out.println("Error1123123123123 " + e.getMessage());
                            }
                        }
                        return;
                    }
                });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                return;
                            }
                        });
                alert.show();
            }
        });

        //Initialize Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Timer Selected
        bottomNavigationView.setSelectedItemId(R.id.about);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timer:
                        startActivity(new Intent(getApplicationContext(), Timer.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(), History.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        return true;

                }
                return false;
            }
        });

        createPiChart();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPiChart(){
        CircularProgressBar circularProgressBar = findViewById(R.id.circularProgressBar);
        // Set Progress
        circularProgressBar.setProgress((float) WEEKLY_SUM);
        //getSum();
         WEEKLY_SUM = getWeekSum(GlobalArrayList);
        statsView.setText((float) (Math.floor(WEEKLY_SUM * 100) / 100) + "/ " + GOAL);

        // or with animation
        circularProgressBar.setProgressWithAnimation((float) (Math.floor(WEEKLY_SUM * 100) / 100), (long) 1000); // =1s

        // Set Progress Max
        circularProgressBar.setProgressMax((float) GOAL);
        // Set ProgressBar Color
        circularProgressBar.setProgressBarColor(Color.BLACK);
        // or with gradient
        circularProgressBar.setProgressBarColorStart(Color.GRAY);
        circularProgressBar.setProgressBarColorEnd(Color.BLUE);
        circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

        // Set background ProgressBar Color
        circularProgressBar.setBackgroundProgressBarColor(Color.GRAY);
        // or with gradient
        circularProgressBar.setBackgroundProgressBarColorStart(Color.WHITE);
        circularProgressBar.setBackgroundProgressBarColorEnd(Color.BLUE);
        circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

        // Set Width
        circularProgressBar.setProgressBarWidth(7f); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(3f); // in DP

        // Other
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(180f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
    }
}