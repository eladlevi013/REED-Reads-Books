package com.minhalreads.androidenglishreadingtimer.models;

import com.minhalreads.androidenglishreadingtimer.R;

import java.util.Calendar;
import java.util.Date;

public class ReadingRecord {
    private String bookName;
    private Date date;
    private Double chronmeter;

    public ReadingRecord() {
        this.date = Calendar.getInstance().getTime();
        this.chronmeter= 0.0;
        this.bookName = String.valueOf(R.string.no_book_name);
    }

    public ReadingRecord(Double duration) {
        this.date = Calendar.getInstance().getTime();
        this.chronmeter = duration;
        this.bookName = String.valueOf(R.string.no_book_name);
    }

    public ReadingRecord(Double duration, String bookName) {
        this.date = Calendar.getInstance().getTime();
        this.chronmeter = duration;
        this.bookName = bookName;
    }

    public ReadingRecord(Date date, Double time) {
        this.date = date;
        this.chronmeter = time;
    }

    public Date getDate() {
        return date;
    }
    public String getBookName() {
        return bookName;
    }
    public Double getDuration() {
        return chronmeter;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public void setDuration(Double duration) {
        this.chronmeter = duration;
    }
}
