package com.gmail.mstudyplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Date;
import java.util.Calendar;

public class Main_calendar extends AppCompatActivity {

    CalendarView calendarView;
    EditText content;
    Button add;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);
        Calendar cal = Calendar.getInstance();
        date = new Date(cal.getTimeInMillis()).toString();

        calendarView = (CalendarView)findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
               date=String.format("%4d-%02d-%02d", year, month+1, dayOfMonth);


            }
        });

        content = (EditText)findViewById(R.id.schedul);
        add = (Button)findViewById(R.id.add);

        DBHelper mHelper = new DBHelper(Main_calendar.this);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String [] args = {date};
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM cal where date=?", args);

        if (cursor.moveToNext()) {
            content.setText(cursor.getString(1));
        }else{
            content.setText("");
        }
        mHelper.close();

        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(content.length() <= 0){
                    Toast.makeText(Main_calendar.this, "내용을 입력하세요!", Toast.LENGTH_LONG).show();
                    return;
                }
                DBHelper mHelper = new DBHelper(Main_calendar.this);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                String [] args = {date};
                Cursor cursor;
                cursor = db.rawQuery("SELECT * FROM cal where date=?", args);
                if (cursor.moveToNext()) {
                    Object [] arg1 = {content.getText().toString(), date};
                    db.execSQL("update cal set content = ? where date=?;",arg1);
                }else{
                    Object [] arg2 = {date, content.getText().toString()};
                    db.execSQL("INSERT INTO cal VALUES(?, ?);",arg2);
                }
                mHelper.close();
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(content.getWindowToken(), 0);
            }
        });

        ImageView home = (ImageView)findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView pencil = (ImageView)findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, Main_study.class);
                startActivity(intent);
            }
        });
        ImageView check = (ImageView)findViewById(R.id.pagecheck);
        check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, Main_subject.class);
                startActivity(intent);
            }
        });
        ImageView my = (ImageView)findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }
}
