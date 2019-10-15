package com.gmail.mstudyplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "EngWord.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Dday ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title text," +
                " ddaydate text);");

        db.execSQL("CREATE TABLE Wisdom (content text);");

        db.execSQL("CREATE TABLE cal(date text, content text);");

        db.execSQL("CREATE TABLE  subject( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject text, content text);");

        db.execSQL("CREATE TABLE mypage( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name text, category text, schhool text, goal text);");

        db.execSQL("CREATE TABLE mypage( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name text, category text, schhool text);");

        db.execSQL("CREATE TABLE study(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date text, subject text, content text, stime text, ftime text);");
                /*
        db.execSQL("CREATE TABLE  kor( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id, title, ddaydate, content);");

        db.execSQL("CREATE TABLE  memo( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id, num, content);");
        db.execSQL("CREATE TABLE  tasks( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id, lseb, sseb, regisDate);");
        db.execSQL("CREATE TABLE  today( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id, num, today, subject, task);");
        */



    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Dday");
        db.execSQL("DROP TABLE IF EXISTS Wisdom");
        //db.execSQL("DROP TABLE IF EXISTS Dday");
        //db.execSQL("DROP TABLE IF EXISTS Dday");
        //db.execSQL("DROP TABLE IF EXISTS Dday");

        onCreate(db);
    }
}


