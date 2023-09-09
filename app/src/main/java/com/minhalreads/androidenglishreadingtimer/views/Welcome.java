package com.minhalreads.androidenglishreadingtimer.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.helpers.SharedPreferencesManager;

public class Welcome extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1200;
    TextView tvWelcome;
    String fullName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvWelcome = findViewById(R.id.welcome_tv);
        fullName = SharedPreferencesManager.getFullName(this);

        if (fullName != getString(R.string.full_name_default_value)) {
            tvWelcome.setText("Welcome Back, " + fullName);
            waitToActivity();
        }
        else {
            enterNamePopup();
        }
    }

    public void waitToActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(Welcome.this, Timer.class);
                startActivity(homeIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void enterNamePopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(Welcome.this);
        EditText input = new EditText(Welcome.this);
        alert.setTitle(getString(R.string.enter_name_msg));
        alert.setCancelable(false);
        alert.setView(input);
        input.setText("");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(input.getText().toString().matches("")) {
                    Toast.makeText(Welcome.this, getString(R.string.change_name_location_reminder),
                            Toast.LENGTH_SHORT).show();
                    fullName = input.getText().toString();
                }
                else {
                    fullName = input.getText().toString();
                    Toast.makeText(Welcome.this, "Welcome, " + fullName, Toast.LENGTH_SHORT).show();
                }

                // save selected name to shared preferences
                SharedPreferencesManager.setFullName(Welcome.this, fullName);
                waitToActivity();
            }
        });
        alert.show();
    }
}
