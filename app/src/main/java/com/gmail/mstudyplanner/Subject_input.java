package com.gmail.mstudyplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Subject_input extends AppCompatActivity {

    TextView title;
    EditText subject;
    Button add;
    Button plus;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_input);

        title = (TextView)findViewById(R.id.title);

        subject = (EditText)findViewById(R.id.subject);

        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        plus = (Button) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper mHelper = new DBHelper(Subject_input.this);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                if(subject.getText().toString().length() <= 0 ){
                    Toast.makeText(Subject_input.this, "과목 이름은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                String temp = subject.getText().toString();
                String [] args = {temp};

                Cursor cursor;
                cursor = db.rawQuery("SELECT * FROM subject where subject = ?", args);
                if (cursor.moveToNext()) {
                    Toast.makeText(Subject_input.this, "이미 존재하는 과목이름입니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                String [] args1 = {temp, ""};
                db.execSQL("INSERT INTO subject(subject, content) VALUES (?, ?);",args1);
                Log.e("과목이름", temp);
                mHelper.close();
                finish();
            }
        });

        ImageView calendar = (ImageView)findViewById(R.id.pagecalendar);
        calendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Subject_input.this, Main_calendar.class);
                startActivity(intent);
            }
        });

        ImageView home = (ImageView)findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Subject_input.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageView check = (ImageView)findViewById(R.id.pagecheck);
        check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Subject_input.this, Main_subject.class);
                startActivity(intent);
            }
        });
        ImageView my = (ImageView)findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Subject_input.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
        ImageView pencil = (ImageView)findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Subject_input.this, Main_study.class);
                startActivity(intent);
            }
        });

    }
}
