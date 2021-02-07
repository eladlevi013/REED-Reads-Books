package com.minhalreads.androidenglishreadingtimer;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class ClassHelper {

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static double getWeekSum(ArrayList<Result> globalArrayList) {
//        double sum = 0;
//        int i = 0;
//        final LocalDate date = LocalDate.now();
//        final LocalDate dateMinus7Days = date.minusDays(6);
//
//        long date7beforemilli = dateMinus7Days.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
//        if (globalArrayList != null) {
//            while (globalArrayList.size() > i && date7beforemilli < globalArrayList.get(i).getDate().toInstant().toEpochMilli()) {
//                sum += globalArrayList.get(i).getChronmeter();
//                i++;
//            }
//        }
//        //return (Math.floor(sum * 100) / 100);
//        return sum;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static double getWeekSum(ArrayList<Result> globalArrayList) {
        double sum =0;

        if(globalArrayList != null && globalArrayList.size() > 0) {
            final LocalDate date = LocalDate.now();

            int j=0;
            while(date.minusDays(j).getDayOfWeek().toString() != "SUNDAY"){
                j++;
            }

            long firstDayOfWeekUnix = date.minusDays(j).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

            int i = 0;
                long as = globalArrayList.get(i).getDate().toInstant().toEpochMilli();
                while (globalArrayList.size() > i && firstDayOfWeekUnix < globalArrayList.get(i).getDate().toInstant().toEpochMilli()) {
                    sum += globalArrayList.get(i).getChronmeter();
                    i++;
                }
        }

        return sum;
    }
}
