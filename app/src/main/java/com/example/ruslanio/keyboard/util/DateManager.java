package com.example.ruslanio.keyboard.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Ruslanio on 06.12.2017.
 */

public class DateManager {
    //01 34 67 910
    //dd.mm hh.mm
    public static DateObject parseDate(String date){
        String day = date.substring(0,2);
        String month = date.substring(3,5);

        String hour = date.substring(6,8);
        String min = date.substring(9,11);

        DateObject dateObject = new DateObject();
        dateObject.setDay(Integer.parseInt(day));
        dateObject.setMonth(Integer.parseInt(month));
        dateObject.setHour(Integer.parseInt(hour));
        dateObject.setMinute(Integer.parseInt(min));
        return dateObject;
    }

    public static boolean isToday(String date){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DateObject dateObject = parseDate(date);
        if (dateObject.getDay() == day)
            return true;
        else
            return false;
    }

    public static boolean isYesterday(String date){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DateObject dateObject = parseDate(date);
        if (dateObject.getDay() == day - 1)
            return true;
        else
            return false;
    }

    public static int compareDate(String first, String second){
        DateObject firstObject = parseDate(first);
        DateObject secondObject = parseDate(second);

        if (firstObject.getMonth() > secondObject.getMonth())
            return 1;
        else if (firstObject.getMonth() < secondObject.getMonth())
            return -1;

        if (firstObject.getDay() > secondObject.getDay())
            return 1;
        else if (firstObject.getDay() < secondObject.getDay())
            return -1;

        if (firstObject.getHour() > secondObject.getHour())
            return 1;
        else if (firstObject.getHour() < secondObject.getHour())
            return -1;

        if (firstObject.getMinute() > secondObject.getMinute())
            return 1;
        else if (firstObject.getMinute() < secondObject.getMinute())
            return -1;

        return 0;
    }

    public static class DateObject{
        private int day = 0;
        private int month = 0;

        private int hour = 0;
        private int minute = 0;

        public DateObject() {
        }

        public DateObject(int day, int month, int hour, int minute) {
            this.day = day;
            this.month = month;
            this.hour = hour;
            this.minute = minute;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }
    }
}
