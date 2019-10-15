package com.gmail.mstudyplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.widget.ImageView;
public class MainActivity extends AppCompatActivity {

    Button DDay;
    TextView today;
    TextView study;
    TextView Wisdom;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        DDay = (Button)findViewById(R.id.DDay);
        DDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Dday_list.class);
                startActivity(intent);
            }
        });

        today = (TextView)findViewById(R.id.today);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String todayDate = sdf.format(date);
        today.setText(todayDate);

        study = (TextView)findViewById(R.id.study);

        Wisdom = (TextView)findViewById(R.id.Wisdom);
        Wisdom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Wisdom_input.class);
                startActivity(intent);
            }
        });

        ImageView calendar = (ImageView)findViewById(R.id.pagecalendar);
        calendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Main_calendar.class);
                startActivity(intent);
            }
        });

        ImageView pencil = (ImageView)findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Main_study.class);
                startActivity(intent);
            }
        });
        ImageView check = (ImageView)findViewById(R.id.pagecheck);
        check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Main_subject.class);
                startActivity(intent);
            }
        });
        ImageView my = (ImageView)findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected  void onResume(){
        super.onResume();

        setTitle(Session.school);

        if(Session.id == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        setTitle("디지털 미디어 고등학교");
        DBHelper mHelper = new DBHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM Dday order by ddaydate asc", null);
        String result = "";

        ArrayList<String> list = new ArrayList<>();
        if (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String ddaydate = cursor.getString(2);
            DDay.setText("가장 가까운 일정 - " + name + ":" + ddaydate );
        }else{
            DDay.setText("다음 일정이 없습니다.\n추가하려면 버튼을 누르세요");
        }

        cursor = db.rawQuery("SELECT * FROM Wisdom", null);
        if (cursor.moveToNext()) {
            String wisdom = cursor.getString(0);
            Wisdom.setText(wisdom);
            Wisdom.setTextColor(Color.BLUE);
            Wisdom.setTextSize(30);
        }else{
            Wisdom.setText("명언/의지를 입력해주세요!");
        }

        cursor.close();
        mHelper.close();


    }
}
