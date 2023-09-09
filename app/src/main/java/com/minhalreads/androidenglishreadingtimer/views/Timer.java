package com.minhalreads.androidenglishreadingtimer.views;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhalreads.androidenglishreadingtimer.helpers.BottomNavigationHelper;
import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.helpers.SharedPreferencesManager;
import com.minhalreads.androidenglishreadingtimer.models.ReadingRecord;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class Timer extends AppCompatActivity {
    public String bookName, fullName;
    public ArrayList<ReadingRecord> readingRecordList;
    public Chronometer chron;
    public Button btnTimer, btnReset;
    public TextView tvQuote;
    public static boolean isRunning = false;
    String[] quotes = new String[] {"“Today a reader, tomorrow a leader.” – Margaret Fuller",
            "“A word after a word after a word is power.” – Margaret Atwood",
            "“Show me a family of readers, and I will show you the people who move the world.” – Napoleon Bonaparte",
            "“A book is a garden, an orchard, a storehouse, a party, a company by the way, a counselor, a multitude of counselors.” – Charles Baudelaire",
            "“If I were a young person today, trying to gain a sense of myself in the world, I would do that by reading” – Maya Angelou",
            "“Reading should not be presented to children as a chore, a duty. It should be offered as a gift.” – Kate DiCamillo",
            "“I think books are like people, in the sense that they’ll turn up in your life when you most need them.” – Emma Thompson",
            "“Books are a uniquely portable magic.” – Stephen King",
            "“Books are mirrors: You only see in them what you already have inside you.” – Carlos Ruiz Zafón",
            "Think before you speak. Read before you think. – Fran Lebowitz", "“If you don’t like to read, you haven’t found the right book.” – J.K. Rowling",
            "“I can feel infinitely alive curled up on the sofa reading a book.” – Benedict Cumberbatch",
            "“Some books leave us free and some books make us free.” – Ralph Waldo Emerson",
            "“Writing and reading decrease our sense of isolation. They feed the soul.” – Anne Lamott",
            "“Books and doors are the same thing. You open them, and you go through into another world.” – Jeanette Winterson",
            "“Books are, let’s face it, better than everything else.” – Nick Hornby", "We read to know we are not alone. – C.S. Lewis",
            "“Read a lot. Expect something big from a book.” – Susan Sontag", "“Once you learn to read, you will be forever free.” – Frederick Douglass",
            "“Books save lives.” – Laurie Anderson",
            "A room without books is like a body without a soul. – Cicero",
            "“The reading of all good books is like a conversation with the finest minds of past centuries.” – Rene Descartes",
            "“That’s the thing about books. They let you travel without moving your feet.” – Jhumpa Lahiri",
            "“I love the way that each book — any book — is its own journey. You open it, and off you go…” – Sharon Creech",
            "“Reading is an exercise in empathy; an exercise in walking in someone else’s shoes for a while.” – Malorie Blackman",
            "“Reading is escape.” – Nora Ephron",
            "“Reading is important. If you know how to read, then the whole world opens up to you.” – Barack Obama",
            "“Books may well be the only true magic.” – Alice Hoffman",
            "The more that you read, the more things you will know. The more that you learn, the more places you’ll go. —Dr. Seuss",
            "Reading is a discount ticket to everywhere. —Mary Schmich",
            "A book is a dream you hold in your hands. —Neil Gaiman", "Reading is an active, imaginative act; it takes work. —Khaled Hosseini",
            "Reading is departure and arrival. —Terri Guillemets",
            " Books are the plane, and the train, and the road. They are the destination, and the journey. They are home. —Anna Quindlen"
    };

    @Override
    protected void onResume() {
        super.onResume();
        readingRecordList = SharedPreferencesManager.getReadingRecords(Timer.this);
        fullName = SharedPreferencesManager.getFullName(Timer.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesManager.saveReadingRecords(Timer.this, readingRecordList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        tvQuote = findViewById(R.id.quote);
        chron = findViewById(R.id.timer_chrone);
        chron.setText("00:00:00");
        btnTimer = findViewById(R.id.button);
        btnReset = findViewById(R.id.button_rest);

        // set random quote
        Random random = new Random();
        tvQuote.setText(quotes[random.nextInt(quotes.length-1)]);
        // bottom navigation set
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.timer);
        BottomNavigationHelper.setBottomNavigationListener(bottomNavigationView, this);

        // on reset button click
        btnReset.setOnClickListener(v -> {
                chron.setBase(SystemClock.elapsedRealtime());
                chron.stop();
                btnTimer.setEnabled(true);
                btnTimer.setText(getString(R.string.timer_start_button_text));
                btnReset.setEnabled(false);
        });

        chron.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                chron = chronometer;
                long time = SystemClock.elapsedRealtime() - chronometer.getBase();
                int h = (int)(time / 3600000);
                int m = (int)(time - h * 3600000) / 60000;
                int s= (int)(time - h * 3600000- m * 60000) / 1000 ;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                chronometer.setText(hh + ":" + mm + ":" + ss);
            }
        });

        btnTimer.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                // timer running
                if(btnTimer.getText() == getString(R.string.timer_stop_button_text)) {
                    isRunning = false;
                    btnReset.setEnabled(true);
                    chron.stop();
                    btnTimer.setEnabled(false);
                    popupBookName();
                } else {
                    isRunning = true;
                    chron.setBase(SystemClock.elapsedRealtime());
                    chron.start();
                    btnTimer.setText(getString(R.string.timer_stop_button_text));
                }
            }
        });
    }

    public void popupBookName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Timer.this);
        EditText input = new EditText(Timer.this);
        alert.setTitle(getString(R.string.enter_book_title_popup_text));
        alert.setCancelable(false);
        alert.setView(input);
        input.setText("");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(input.getText().toString().matches("")) {
                    bookName = getString(R.string.default_book_title);
                    afterPopup();
                }
                else {
                    bookName = input.getText().toString();
                    afterPopup();
                }
            }
        });
        alert.show();
    }

    public void afterPopup() {
        double stoppedMilliseconds = 0;
        Intent activityA = new Intent(Timer.this, Score.class);
        activityA.putExtra("time", chron.getText());
        activityA.putExtra("book_name", bookName);

        // calculating time for reading list
        String array[] = chron.getText().toString().split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                + Integer.parseInt(array[1]) * 60 * 1000
                + Integer.parseInt(array[2]) * 1000;
        }

        stoppedMilliseconds = stoppedMilliseconds / 60000;
        stoppedMilliseconds = Math.floor(stoppedMilliseconds * 100) / 100;
        readingRecordList.add(new ReadingRecord(stoppedMilliseconds, bookName));
        SharedPreferencesManager.saveReadingRecords(Timer.this, readingRecordList);
        startActivity(activityA);
    }
}
