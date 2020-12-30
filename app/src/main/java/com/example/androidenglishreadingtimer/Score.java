package com.example.androidenglishreadingtimer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Score extends AppCompatActivity {

    private static final String TAG = "0";
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

        verifyStoragePermission(Score.this);

        button = findViewById(R.id.button_share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                openScreenshot();
            }
        });
    }

//FUnCTIONS

    private void openScreenshot() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //Uri uri = Uri.fromFile(imageFile);
        Uri photoURI = FileProvider.getUriForFile(Score.this, getApplicationContext().getPackageName() + ".provider", takeScreenshot(findViewById(android.R.id.content).getRootView(), "filename"));
        intent.setDataAndType(photoURI, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    protected static File takeScreenshot(View view, String filename){ // DOES NOT WORK
        Date date = new Date();
        CharSequence format = DateFormat.format("yyyy-MM-dd:mm:ss", date);

        try{
            String dirPath = Environment.getExternalStorageDirectory().toString()+"/good";
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                boolean mkdir=fileDir.mkdir();
            }

            String path = dirPath+"/" + filename+ "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            int quality =100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return imageFile;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final int REQUEST_EXTERNAL_STORAGE=1;
    public static String[] PERMISSION_STORAGE={
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

}