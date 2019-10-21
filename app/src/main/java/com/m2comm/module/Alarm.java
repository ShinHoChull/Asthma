package com.m2comm.module;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class Alarm {
    Activity activity;

    public Alarm(Activity activity) {

        this.activity = activity;
        SQLiteDatabase db = new DBHelper(activity,
                "alarm.sqlite",
                null, 1).getWritableDatabase();
        String sql = "CREATE table if not exists Alarm (idx integer primary key autoincrement, msg text, sid integer, year integer, month integer, day integer, hour integer, minute integer)";
        db.execSQL(sql);
        db.close();
    }

    public int InsertAlarm(int year, int month, int day, int hour, int minute, int sid, String msg) {

        Calendar dateSelected = Calendar.getInstance();



        SQLiteDatabase db = new DBHelper(activity,"alarm.sqlite", null, 1).getWritableDatabase();
        String sql = "INSERT INTO Alarm (msg, sid, year, month, day, hour, minute) " +
                "VALUES ( '" + msg + "', '" + sid + "', '" + year + "', '" + month + "', '" + day + "', '" + hour + "', '" + minute + "')";
        db.execSQL(sql);
        db.close();


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(activity, AlarmReceive.class);
        intent.putExtra("msg", msg);
        intent.putExtra("sid", sid);
        PendingIntent pIntent = PendingIntent.getBroadcast(activity, sid, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

        return 1;
    }


    public void DelAlarm(int sid) {

        Intent intent =  new Intent(activity, AlarmReceive.class);
        PendingIntent sender
                = PendingIntent.getBroadcast(activity, sid, intent, 0);
        AlarmManager manager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(sender);

        SQLiteDatabase db = new DBHelper(activity,"alarm.sqlite", null, 1).getWritableDatabase();
        String sql = "Delete from Alarm where sid="+sid;
        db.execSQL(sql);
        db.close();
    }

    public ArrayList<AlarmItem> GetAlarm() {
        ArrayList<AlarmItem> alarmList = new ArrayList<AlarmItem>();
        SQLiteDatabase db = new DBHelper(activity, "alarm.sqlite", null, 1).getReadableDatabase();
        String sql = "select idx, year, month, day, hour, minute, msg from Alarm order by hour,minute asc";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
                Log.d("hgkim","month : " + c.getInt(2)) ;
                Log.d("hgkim","day : " + c.getInt(3)) ;
                Log.d("hgkim","hour : " + c.getInt(4)) ;
                Log.d("hgkim","minute : " + c.getInt(5)) ;
            Log.d("hgkim","msg : " + c.getString(6)) ;
            c.moveToNext();
        }
        return alarmList;
    }

    public AlarmItem GetAlarm(int sid) {
        ArrayList<AlarmItem> alarmList = new ArrayList<AlarmItem>();
        SQLiteDatabase db = new DBHelper(activity, "alarm.sqlite", null, 1).getReadableDatabase();
        String sql = "select idx, year, month, day, hour, minute, delay, msg, bab, chk, name  from Alarm where sid = "+sid;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();

        return new AlarmItem(c.getInt(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4),
                    c.getInt(5), c.getInt(6), c.getString(7), c.getInt(8), c.getInt(9), c.getString(10));

    }

    public class AlarmItem {
        public int sid;
        public int year;
        public int month;
        public int day;
        public int hour;
        public int minute;
        public int delay;
        public String msg;
        public int bab;
        public int chk;
        public String name;

        public AlarmItem(int sid, int year, int month, int day, int hour, int minute, int delay, String msg, int bab, int chk, String name) {
            this.sid = sid;
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.delay = delay;
            this.msg = msg;
            this.bab = bab;
            this.chk = chk;
            this.name = name;
        }
    }
}
