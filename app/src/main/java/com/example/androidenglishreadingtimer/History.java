package com.example.androidenglishreadingtimer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class History extends AppCompatActivity {

    public String FULL_NAME= "My Reading List";
    public ArrayList<Result> GlobalArrayList = null;
    public ListView listView;
    public TextView totalTime, averageTime, monthSum_tv, weekSum_tv, myReadinList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Duplicated Code
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
        FULL_NAME = sharedPreferences.getString("FULL_NAME" , "Default Name");

        myReadinList = findViewById(R.id.readingLog);
        if(!FULL_NAME.toString().matches("Default Name")) {
            if(!FULL_NAME.matches(".*[^a-zA-Z ].*")) {
                myReadinList.setText(FULL_NAME + "'s Reading List");
            }
        }

        Collections.reverse(GlobalArrayList);

        //Initialize Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Timer Selected
        bottomNavigationView.setSelectedItemId(R.id.history);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.timer:
                        startActivity(new Intent(getApplicationContext(), Timer.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.history:
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });  // END OF NAVIGATION SETUP

        double totalSum = Math.floor(arraySum(GlobalArrayList) * 100) / 100;
        totalTime = findViewById(R.id.sum_tv);
        double totalWeek = ClassHelper.getWeekSum(GlobalArrayList);

        if(totalSum >= 60) {
            totalTime.setText("Total Time: " + Math.floor(totalSum/60 * 100) / 100 + " hours, and: " + Math.floor(totalSum%60 * 100) / 100 +  " minutes");
        }
//        if(totalSum >= 60) {
//            totalTime.setText("Total Time: " + Math.floor(totalSum/60 * 100) / 100 + " hours, and: " + Math.floor(totalSum%60 * 100) / 100 +  " minutes");
//        }
//        else if()
//        {
//            totalTime.setText("Total Time: " + totalSum + " minutes");
//        }



        averageTime = findViewById(R.id.average_tv);
        if(totalSum != 0) {
            double averageTimePerRead = totalSum / GlobalArrayList.size();
            averageTimePerRead = Math.floor(averageTimePerRead * 100) / 100;
            averageTime.setText("Average reading time: " + averageTimePerRead + " min");
        }
        else {
            averageTime.setText("Average reading time: 0.0 min");
        }

        double monthSum = monthSumFunction(GlobalArrayList);
        monthSum_tv = findViewById(R.id.sumMonth_tv);
        monthSum = Math.floor(monthSum * 100) / 100;
        monthSum_tv.setText("Total This month: " + monthSum + " min");

        ClassHelper.getWeekSum(GlobalArrayList);
        double weekSum = ClassHelper.getWeekSum(GlobalArrayList);
        weekSum = Math.floor(weekSum * 100) / 100;
        weekSum_tv = findViewById(R.id.sumWeek_tv);
        weekSum_tv.setText("Total This Week: " + weekSum + " min");

        listView = findViewById(R.id.ls);
        listView.setAdapter(new MyCustomBaseAdapter(this, GlobalArrayList));
    }

    public static double monthSumFunction(ArrayList<Result> arrayList){
        double sum = 0;
        int i=0;

        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        while(i < arrayList.size()){
            int arrayMonth = arrayList.get(i).getDate().getMonth() + 1;
            int arrayYear = arrayList.get(i).getDate().getYear() + 1900;

            if (arrayMonth == month && arrayYear == year){
                sum += arrayList.get(i).getChronmeter();
            }

            i++;
        }
        return sum;
    }

    public static double arraySum(ArrayList<Result> arrayList){
        double sum=0;

        for (int i=0; i<arrayList.size(); i++) {
            sum += arrayList.get(i).getChronmeter();
        }

        return sum;
    }

    public class MyCustomBaseAdapter extends BaseAdapter {
        private ArrayList<Result> searchArrayList;
        private LayoutInflater mInflater;

        public MyCustomBaseAdapter(Context context, ArrayList<Result> results) {
            searchArrayList = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return searchArrayList.size();
        }

        public Object getItem(int position) {
            return searchArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row, null);
                holder = new ViewHolder();
                holder.txtName = (TextView) convertView.findViewById(R.id.time_tv);
                holder.txtCityState = (TextView) convertView.findViewById(R.id.date_tv);
                holder.txBookName = (TextView) convertView.findViewById(R.id.book_tv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Date date = searchArrayList.get(position).getDate();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
            String formattedDate = sdf.format(date);

            holder.txBookName.setText("Book Title: " + searchArrayList.get(position).getBookName());
            holder.txtName.setText("Date: " + formattedDate);
            holder.txtCityState.setText("Time: " + searchArrayList.get(position).getChronmeter().toString() + " minutes");

            return convertView;
        }

        class ViewHolder {
            TextView txtName;
            TextView txtCityState;
            TextView txBookName;
        }
    }
}