package com.gmail.mstudyplanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "login.db", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE member(id text, name text, school text, category text);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS member");
        onCreate(db);
    }
}