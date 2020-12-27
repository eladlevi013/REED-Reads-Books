package com.example.androidenglishreadingtimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Score extends AppCompatActivity {

    ArrayList<Result> GlobalArrayList;
    TextView fullname_tv, weekSum_tv, BookName_tv;
    Button button;
    String time, BOOK_NAME;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ResultList", null);
        Type type = new TypeToken<ArrayList<Result>>() {}.getType();
        GlobalArrayList = gson.fromJson(json, type);
        if(GlobalArrayList == null) {
            GlobalArrayList = new ArrayList<>();
        }

        Collections.reverse(GlobalArrayList);

        fullname_tv = findViewById(R.id.name);
        fullname_tv.setText(sharedPreferences.getString("FULL_NAME", "Default Name"));

        weekSum_tv = findViewById(R.id.weekSum);
        float weekSum = (float) ClassHelper.getWeekSum(GlobalArrayList);
        String weekString = String.valueOf(weekSum);
        weekSum_tv.setText("Total this Week: " + weekString + " min");

        BOOK_NAME = getIntent().getStringExtra("book_name");
        BookName_tv = findViewById(R.id.book_name_tv);
        BookName_tv.setText("Book: " + BOOK_NAME);

        time = getIntent().getStringExtra("time");
        TextView textView = findViewById(R.id.time_tv);
        textView.setText(time);
        final KonfettiView konfettiView = findViewById(R.id.viewKonfetti);

        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(10.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(0, (float) konfettiView.getWidth() + 1000f, -50f, -50f)
                .streamFor(100, 5000L);

        button = findViewById(R.id.button_share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");

                double stoppedMilliseconds = 0;
                String array[] = time.toString().split(":");
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

                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Read, " + stoppedMilliseconds + " minutes.");
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Score.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}