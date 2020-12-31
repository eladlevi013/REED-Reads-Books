    package com.example.androidenglishreadingtimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class Timer extends AppCompatActivity {

    public String BookName = "BOOK_NAME";
    ArrayList<Result> GlobalArrayList;
    Chronometer chron;
    Button btn, btn_rest;
    TextView quote_tv;
    boolean isRunning= false;
    public String FULL_NAME;

    @Override
    protected void onResume() {
        super.onResume();

        String[] quoteArray = new String[] {"“Today a reader, tomorrow a leader.” – Margaret Fuller", "“A word after a word after a word is power.” – Margaret Atwood", "“Show me a family of readers, and I will show you the people who move the world.” – Napoleon Bonaparte", "“A book is a garden, an orchard, a storehouse, a party, a company by the way, a counselor, a multitude of counselors.” – Charles Baudelaire", "“If I were a young person today, trying to gain a sense of myself in the world, I would do that by reading” – Maya Angelou", "“Reading should not be presented to children as a chore, a duty. It should be offered as a gift.” – Kate DiCamillo", "“I think books are like people, in the sense that they’ll turn up in your life when you most need them.” – Emma Thompson", "“Books are a uniquely portable magic.” – Stephen King", "“Books are mirrors: You only see in them what you already have inside you.” – Carlos Ruiz Zafón", "Think before you speak. Read before you think. – Fran Lebowitz", "“If you don’t like to read, you haven’t found the right book.” – J.K. Rowling", "“I can feel infinitely alive curled up on the sofa reading a book.” – Benedict Cumberbatch", "“Some books leave us free and some books make us free.” – Ralph Waldo Emerson", "“Writing and reading decrease our sense of isolation. They feed the soul.” – Anne Lamott", "“Books and doors are the same thing. You open them, and you go through into another world.” – Jeanette Winterson", "“Books are, let’s face it, better than everything else.” – Nick Hornby", "We read to know we are not alone. – C.S. Lewis", "“Read a lot. Expect something big from a book.” – Susan Sontag", "“Once you learn to read, you will be forever free.” – Frederick Douglass", "“Books save lives.” – Laurie Anderson", "A room without books is like a body without a soul. – Cicero", "“The reading of all good books is like a conversation with the finest minds of past centuries.” – Rene Descartes", "“That’s the thing about books. They let you travel without moving your feet.” – Jhumpa Lahiri", "“I love the way that each book — any book — is its own journey. You open it, and off you go…” – Sharon Creech", "“Reading is an exercise in empathy; an exercise in walking in someone else’s shoes for a while.” – Malorie Blackman", "“Reading is escape.” – Nora Ephron", "“Reading is important. If you know how to read, then the whole world opens up to you.” – Barack Obama", "“Books may well be the only true magic.” – Alice Hoffman", "The more that you read, the more things you will know. The more that you learn, the more places you’ll go. —Dr. Seuss", "Reading is a discount ticket to everywhere. —Mary Schmich", "A book is a dream you hold in your hands. —Neil Gaiman", "Reading is an active, imaginative act; it takes work. —Khaled Hosseini", "Reading is departure and arrival. —Terri Guillemets", " Books are the plane, and the train, and the road. They are the destination, and the journey. They are home. —Anna Quindlen"};
        Random random = new Random();
        int RANDOM_QUATE_NUMBER = random.nextInt(quoteArray.length-1);
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
//        if(sharedPreferences.getString("FULL_NAME", "Default Name") == "Default Name") {
//            //DIALOG
//            AlertDialog.Builder alert = new AlertDialog.Builder(Timer.this);
//            alert.setTitle("Enter Your full name");
//
//            // Set an EditText view to get user input
//            final EditText input = new EditText(Timer.this);
//            alert.setView(input);
//
//            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                    if(input.getText().toString() != null) {
//                        FULL_NAME = input.getText().toString();
//
//                        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("FULL_NAME", FULL_NAME);
//                        editor.apply();
//
//                        String asd = sharedPreferences.getString("FULL_NAME", "Default Name");
//                    }
//                }
//            });
//
//            alert.setNegativeButton("Cancel",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO Auto-generated method stub
//                            return;
//                        }
//                    });
//            alert.show();
//        }
        else {
            FULL_NAME = sharedPreferences.getString("FULL_NAME", "Default Name");
            //Toast.makeText(this, "Hey," + FULL_NAME, Toast.LENGTH_SHORT).show();
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
        //Collections.reverse(GlobalArrayList);
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
                            Toast.makeText(Timer.this, "You Can't Change Activity While The Timer is Running!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), History.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }

                    case R.id.about:
                        if(isRunning){
                            Toast.makeText(Timer.this, "You Can't Change Activity While The Timer is Running!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            startActivity(new Intent(getApplicationContext(), Goals.class));
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
                    popupBookName();

                } else {
                    isRunning = true;
                    chron.setBase(SystemClock.elapsedRealtime());
                    chron.start();
                    btn.setText("Stop");
                }
            }
        });
    }

    public void popupBookName() {
        //DIALOG
        AlertDialog.Builder alert = new AlertDialog.Builder(Timer.this);
        alert.setTitle("Enter the Title of your Book: ");
        alert.setCancelable(false);

        // Set an EditText view to get user input
        final EditText input = new EditText(Timer.this);
        alert.setView(input);
        input.setText("");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(input.getText().toString().matches("")) {
                    BookName = "Default Value";
                    afterPopup();
                }
                else {
                    BookName = input.getText().toString();
                    afterPopup();
                }
            }
        });

//        alert.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // TODO Auto-generated method stub
//                        BookName = "Default Value";
//                        afterPopup();
//                        return;
//                    }
//                });
        alert.show();
    }

    public void afterPopup() {
        long time = SystemClock.elapsedRealtime() - chron.getBase();

        Intent activityA = new Intent(Timer.this, Score.class);
        activityA.putExtra("time",chron.getText());
        activityA.putExtra("book_name", BookName);

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

        GlobalArrayList.add(new Result(stoppedMilliseconds, BookName));
        saveShared();
        startActivity(activityA);
    }
}