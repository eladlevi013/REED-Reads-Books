package com.minhalreads.androidenglishreadingtimer.helpers;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.minhalreads.androidenglishreadingtimer.models.ReadingRecord;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;

public class ReadingStatsHelper {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static double getWeekTimeSum(ArrayList<ReadingRecord> globalArrayList) {
        final LocalDate date = LocalDate.now();
        double sum = 0;

        if(globalArrayList != null && globalArrayList.size() > 0) {
            int j = 0;

            // getting the first day of this week
            while(date.minusDays(j).getDayOfWeek().toString() != "SUNDAY") {
                j++;
            }

            // calculating sum
            long firstDayOfWeekUnix = date.minusDays(j).atStartOfDay(ZoneOffset.UTC)
                    .toInstant().toEpochMilli();
            int i = 0;
            while (globalArrayList.size() > i && firstDayOfWeekUnix < globalArrayList
                    .get(i).getDate().toInstant().toEpochMilli()) {
                sum += globalArrayList.get(i).getDuration();
                i++;
            }
        }

        return getFormattedTime(sum);
    }

    public static double getMonthTimeSum(ArrayList<ReadingRecord> arrayList){
        double sum = 0;
        int i=0;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        while(i < arrayList.size()){
            int arrayMonth = arrayList.get(i).getDate().getMonth() + 1;
            int arrayYear = arrayList.get(i).getDate().getYear() + 1900;
            if (arrayMonth == month && arrayYear == year) {
                sum += arrayList.get(i).getDuration();
            }
            i++;
        }

        return getFormattedTime(sum);
    }

    public static double getTotalTimeSum(ArrayList<ReadingRecord> arrayList){
        double sum=0;
        for (int i=0; i<arrayList.size(); i++) {
            sum += arrayList.get(i).getDuration();
        }

        return getFormattedTime(sum);
    }

    private static double getFormattedTime(double time) {
        return Math.floor(time * 100) / 100;
    }
}
