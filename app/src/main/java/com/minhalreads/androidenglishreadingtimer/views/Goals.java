package com.minhalreads.androidenglishreadingtimer.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.minhalreads.androidenglishreadingtimer.helpers.BottomNavigationHelper;
import com.minhalreads.androidenglishreadingtimer.helpers.ReadingStatsHelper;
import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.helpers.SharedPreferencesManager;
import com.minhalreads.androidenglishreadingtimer.models.ReadingRecord;

import java.util.ArrayList;

public class Goals extends AppCompatActivity {
    public ArrayList<ReadingRecord> readingRecords = null;
    public TextView tvStats, tvCompliment, tvAbout, tvName, tvWeeklyGoal;
    public Button btnChangeName, btnChangeGoal;
    public double goal = 20, weeklySum = 0;
    public String fullName;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        fullName = getString(R.string.full_name_default_value);

        btnChangeName = findViewById(R.id.change_name_button);
        tvAbout = findViewById(R.id.about_button);
        tvCompliment = findViewById(R.id.compliment);
        tvStats = findViewById(R.id.reading_stats);
        btnChangeGoal = findViewById(R.id.set_goal_button);
        tvName = findViewById(R.id.name);
        tvWeeklyGoal = findViewById(R.id.weekly_goal);

        // loading data from shared preferences
        readingRecords = SharedPreferencesManager.getReadingRecords(this);
        fullName = SharedPreferencesManager.getFullName(this);
        goal = SharedPreferencesManager.getGoal(this);
        tvWeeklyGoal.setText("Weekly Goal: " + goal + " minutes");

        tvAbout.setOnClickListener(v -> {
            Intent intent = new Intent(Goals.this, About.class);
            startActivity(intent);
        });

        // set an appropriate compliment
        weeklySum = ReadingStatsHelper.getWeekTimeSum(readingRecords);
        if(weeklySum <= 15) tvCompliment.setText("Keep reading, you're doing great!");
        else if(weeklySum >= 15 && weeklySum <= 30) tvCompliment.setText("You are a great reader! KEEP IT UP");
        else if(weeklySum >= 30 && weeklySum <= 45) tvCompliment.setText("WOW! You are a fantastic reader! KEEP IT UP");
        else if(weeklySum >= 45 && weeklySum <= 60) tvCompliment.setText("You are an amazing reader! Proud of you!");
        else if(weeklySum >= 60) tvCompliment.setText("You are a reading SUPERSTAR!");

        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Goals.this);
                EditText input = new EditText(Goals.this);
                alert.setTitle(getString(R.string.enter_name_msg));
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(input.getText().toString() != null) {
                            fullName = input.getText().toString();
                            SharedPreferencesManager.setFullName(Goals.this, fullName);
                            tvName.setText(fullName);
                        }
                    }
                });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.show();
            }
        });

        btnChangeGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Goals.this);
                EditText input = new EditText(Goals.this);
                input.setInputType(2);
                alert.setView(input);
                alert.setTitle(getString(R.string.set_new_goal_text));

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(input.getText() != null) {
                            try {
                                if(Double.parseDouble(input.getText().toString()) >= weeklySum) {
                                    goal = Double.parseDouble(input.getText().toString());
                                    tvStats.setText((Math.floor(weeklySum / goal * 100) + "%"));
                                    createProgressBar();
                                    SharedPreferencesManager.setGoal(Goals.this, (float) goal);
                                    tvWeeklyGoal.setText("Weekly Goal: " + goal + " minutes");
                                }
                            } catch (Exception e) {
                                System.out.println("Error " + e.getMessage());
                            }
                        }
                        return;
                    }
                });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.show();
            }
        });

        tvName.setText(fullName);
        // bottom navbar init
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.about);
        BottomNavigationHelper.setBottomNavigationListener(bottomNavigationView, this);
        // create progress bar
        createProgressBar();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createProgressBar(){
        CircularProgressBar circularProgressBar = findViewById(R.id.progress_bar);
        circularProgressBar.setProgress((float) weeklySum);
        weeklySum = ReadingStatsHelper.getWeekTimeSum(readingRecords);
        tvStats.setText(Math.floor(weeklySum / goal * 100) + "%");
        circularProgressBar.setProgressWithAnimation((float) weeklySum, (long) 1000);
        circularProgressBar.setProgressMax((float) goal);
        circularProgressBar.setProgressBarColor(Color.BLACK);
        circularProgressBar.setProgressBarColorStart(Color.parseColor("#7928ed"));
        circularProgressBar.setProgressBarColorEnd(Color.BLUE);
        circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);
        circularProgressBar.setBackgroundProgressBarColor(Color.GRAY);
        circularProgressBar.setBackgroundProgressBarColorStart(Color.parseColor("#7928ed"));
        circularProgressBar.setBackgroundProgressBarColorEnd(Color.BLUE);
        circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);
        circularProgressBar.setProgressBarWidth(10f);
        circularProgressBar.setBackgroundProgressBarWidth(3f);
        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(180f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
    }
}
