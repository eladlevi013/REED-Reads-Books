package com.example.androidenglishreadingtimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class History extends AppCompatActivity {

    public ArrayList<Result> GlobalArrayList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preference", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("ResultList", null);
        Type type = new TypeToken<ArrayList<Result>>() {}.getType();
        GlobalArrayList = gson.fromJson(json, type);
        if(GlobalArrayList == null) {
            Toast.makeText(this, "Creating A new one!", Toast.LENGTH_SHORT).show();
            GlobalArrayList = new ArrayList<>();
        }

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
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.history:
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), About.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });  // END OF NAVIGATION SETUP


        //Toast.makeText(History.this, GlobalArrayList.toString(), Toast.LENGTH_SHORT).show();

        Collections.reverse(GlobalArrayList);

        listView = findViewById(R.id.ls);
        listView.setAdapter(new MyCustomBaseAdapter(this, GlobalArrayList));
    }

//    public static ArrayList<Result> createRandomList() {
//        ArrayList<Result> arrayList = new ArrayList<>();
//        arrayList.add(new Result());
//        arrayList.add(new Result());
//        arrayList.add(new Result());
//        arrayList.add(new Result());
//        arrayList.add(new Result());
//
//        return arrayList;
//    }

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

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Date date = searchArrayList.get(position).getDate();
            //java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:ss");
            String formattedDate = sdf.format(date);

            holder.txtName.setText("Date: " + formattedDate);
            holder.txtCityState.setText("Time: " + searchArrayList.get(position).getChronmeter().toString() + " minutes");

            return convertView;
        }

        class ViewHolder {
            TextView txtName;
            TextView txtCityState;
        }
    }
}