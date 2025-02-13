package com.example.foodapp.data.local.model;


public class CalendarDay {
    private String dayName;
    private int dayNum;
    private String monthName;

    public CalendarDay(String dayName, int dayNum, String monthName) {
        this.dayName = dayName;
        this.dayNum = dayNum;
        this.monthName = monthName;
    }

    public String getDayName() {
        return dayName;
    }

    public int getDayNum() {
        return dayNum;
    }

    public String getMonthName() {
        return monthName;
    }
}
