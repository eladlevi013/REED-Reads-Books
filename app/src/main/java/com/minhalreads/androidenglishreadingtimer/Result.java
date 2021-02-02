package com.minhalreads.androidenglishreadingtimer;

import java.util.Calendar;
import java.util.Date;

public class Result {

    private String bookName;
    private Date date;
    private Double chronmeter;

    public Result() {
        this.date = Calendar.getInstance().getTime();
        this.chronmeter= 0.0;
        bookName = "No Book Name";
    }

    public Result(Double chronmeter) {
        this.date = Calendar.getInstance().getTime();
        this.chronmeter = chronmeter;
        bookName = "No Book Name";
    }

    public Result(Double chronmeter, String GivenbookName) {
        this.date = Calendar.getInstance().getTime();
        this.chronmeter = chronmeter;
        bookName = GivenbookName;
    }

    public Result(Date date, Double chronmeter) {
        this.date = date;
        this.chronmeter = chronmeter;
    }

    public Date getDate() {
        return date;
    }

    public String getBookName() {
        return bookName;
    }


    public Double getChronmeter() {
        return chronmeter;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setChronmeter(Double chronmeter) {
        this.chronmeter = chronmeter;
    }
}
