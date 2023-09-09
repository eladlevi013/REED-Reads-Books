package com.minhalreads.androidenglishreadingtimer.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhalreads.androidenglishreadingtimer.R;
import com.minhalreads.androidenglishreadingtimer.models.ReadingRecord;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class SharedPreferencesManager {
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(context.getString
                (R.string.shared_preference_name), Context.MODE_PRIVATE);
    }

    public static void setGoal(Context context, float goal) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putFloat(context.getString(R.string.goal_prefs_title), goal);
        editor.apply();
    }

    public static float getGoal(Context context) {
        return getSharedPreferences(context).getFloat(
                context.getString(R.string.goal_prefs_title), 120);
    }

    public static void setFullName(Context context, String fullName) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(context.getString
                (R.string.full_name_prefs_title), fullName);
        editor.apply();
    }

    public static String getFullName(Context context) {
        return getSharedPreferences(context).getString(context.getString
                (R.string.full_name_prefs_title), context.getString
                (R.string.full_name_default_value));
    }

    public static ArrayList<ReadingRecord> getReadingRecords(Context context) {
        String json = getSharedPreferences(context).getString
                (context.getString(R.string.reading_records_prefs_title), null);
        Type type = new TypeToken<ArrayList<ReadingRecord>>() {}.getType();
        ArrayList<ReadingRecord> readingRecords = (new Gson()).fromJson(json, type);
        if(readingRecords == null) readingRecords = new ArrayList<>();
        Collections.reverse(readingRecords);
        return readingRecords;
    }

    public static void saveReadingRecords(Context context, ArrayList<ReadingRecord> readingRecords) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        String json = (new Gson()).toJson(readingRecords);
        editor.putString(context.getString
                (R.string.reading_records_prefs_title), json);
        editor.apply();
    }
}
