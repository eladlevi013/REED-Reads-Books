package com.minhalreads.androidenglishreadingtimer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class ClassHelper {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static double getWeekSum(ArrayList<Result> globalArrayList) {
        double sum = 0;
        int i = 0;
        final LocalDate date = LocalDate.now();
        final LocalDate dateMinus7Days = date.minusDays(6);

        long date7beforemilli = dateMinus7Days.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        if(globalArrayList != null) {
            while (globalArrayList.size() > i && date7beforemilli < globalArrayList.get(i).getDate().toInstant().toEpochMilli()) {
                sum += globalArrayList.get(i).getChronmeter();
                i++;
            }
        }
        //return (Math.floor(sum * 100) / 100);
        return sum;
    }

}
