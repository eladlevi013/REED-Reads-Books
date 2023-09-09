package com.minhalreads.androidenglishreadingtimer.views;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.minhalreads.androidenglishreadingtimer.helpers.BottomNavigationHelper;
import com.minhalreads.androidenglishreadingtimer.helpers.ReadingStatsHelper;
import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.helpers.SharedPreferencesManager;
import com.minhalreads.androidenglishreadingtimer.models.ReadingRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class History extends AppCompatActivity {
    public ArrayList<ReadingRecord> readingRecords = null;
    public ListView readingRecordsListView;
    public TextView tvTotalTime, tvMouthTime, tvWeekTime, tvReadingLogMsg;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tvReadingLogMsg = findViewById(R.id.reading_log_msg_tv);
        tvTotalTime = findViewById(R.id.sum_tv);
        tvWeekTime = findViewById(R.id.week_sum_tv);
        tvMouthTime = findViewById(R.id.month_sum_tv);
        readingRecordsListView = findViewById(R.id.ls);

        // get reading records from shared preferences
        readingRecords = SharedPreferencesManager.getReadingRecords(this);
        tvReadingLogMsg.setText(SharedPreferencesManager.getFullName(this) + "'s Reading Log");

        // calculate reading time and display it
        double totalSum = Math.floor(ReadingStatsHelper.getTotalTimeSum(readingRecords) * 100) / 100;
        double weekSum = Math.floor(ReadingStatsHelper.getWeekTimeSum(readingRecords) * 100) / 100;
        double monthSum = Math.floor(ReadingStatsHelper.getMonthTimeSum(readingRecords) * 100) / 100;

        if(totalSum >= 60) tvTotalTime.setText("Total reading time: " + Math.floor
                (totalSum/60 * 100) / 100 + " hours, and: " + Math.floor(totalSum % 60 * 100) / 100 +  " minutes");
        else tvTotalTime.setText("Total reading time: " + totalSum + " minutes");
        tvMouthTime.setText("Total This month: " + monthSum + " min");
        tvWeekTime.setText("Total This Week: " + weekSum + " min");

        // attaching data adapter to reading records list view
        readingRecordsListView.setAdapter(new ReadingRecordsAdapter(this, readingRecords));

        // init bottom navbar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.history);
        BottomNavigationHelper.setBottomNavigationListener(bottomNavigationView, this);
    }

    public class ReadingRecordsAdapter extends BaseAdapter {
        private ArrayList<ReadingRecord> recordsArrayList;
        private LayoutInflater mInflater;

        public ReadingRecordsAdapter(Context context, ArrayList<ReadingRecord> results) {
            recordsArrayList = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return recordsArrayList.size();
        }

        public Object getItem(int position) {
            return recordsArrayList.get(position);
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
                holder.txtDuration = (TextView) convertView.findViewById(R.id.date_tv);
                holder.txBookName = (TextView) convertView.findViewById(R.id.book_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Date date = recordsArrayList.get(position).getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
            String formattedDate = sdf.format(date);
            holder.txBookName.setText("Book Title: " + recordsArrayList.get(position).getBookName());
            holder.txtName.setText("Date: " + formattedDate);
            if(recordsArrayList.get(position).getDuration() > 60)
                holder.txtDuration.setText("Time: " + recordsArrayList.get(position).getDuration() / 60
                        + " hours, and " + recordsArrayList.get(position).getDuration() % 60 + "minutes");
            else
                holder.txtDuration.setText("Time: " + recordsArrayList.get(position).getDuration() + " minutes");
            return convertView;
        }
        class ViewHolder {
            TextView txtName;
            TextView txtDuration;
            TextView txBookName;
        }
    }
}
