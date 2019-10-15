package com.gmail.mstudyplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Main_subject extends AppCompatActivity {

    TextView list;
    Button add;

    ListView subjectlist;
    EditText subjectcontent;
    Button addbtn;

    String subject = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subject);

        subjectlist = (ListView)findViewById(R.id.subjectlist);
        subjectcontent = (EditText)findViewById(R.id.subjectcontent);
        addbtn = (Button)findViewById(R.id.addbtn);

        addbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                DBHelper mHelper = new DBHelper(Main_subject.this);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                if (subjectcontent.getText().toString().length() <= 0){
                    Toast.makeText(Main_subject.this, "내용은 필수 입력입니다.!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (subject.length() <= 0){
                    Toast.makeText(Main_subject.this, "과목을 선택하세요!", Toast.LENGTH_LONG).show();
                    return;
                }

                String [] args1 = {subjectcontent.getText().toString(),subject};
                db.execSQL("update subject set content = ? where subject = ?;",args1);
                Toast.makeText(Main_subject.this, "내용 추가 성공!", Toast.LENGTH_LONG).show();
                mHelper.close();
            }
        });

        list = (TextView)findViewById(R.id.list);

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_subject.this, Subject_input.class);
                startActivity(intent);
            }
        });



        ImageView calendar = (ImageView)findViewById(R.id.pagecalendar);
        calendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, Main_calendar.class);
                startActivity(intent);
            }
        });

        ImageView pencil = (ImageView)findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, Main_study.class);
                startActivity(intent);
            }
        });
        ImageView home = (ImageView)findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageView my = (ImageView)findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(subjectcontent.getWindowToken(), 0);
        return false;
    }
    public void onResume(){
        super.onResume();

        final DBHelper mHelper = new DBHelper(Main_subject.this);
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM subject", null);
        final ArrayList<String>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }

        ArrayAdapter<String>adapter = new ArrayAdapter<>(Main_subject.this, android.R.layout.simple_list_item_1, list);

        subjectlist.setAdapter(adapter);
        subjectlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        subjectlist.setDivider(new ColorDrawable(Color.BLUE));
        subjectlist.setDividerHeight(2);

        subjectlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                subject = list.get(i);
                SQLiteDatabase db = mHelper.getWritableDatabase();

                Cursor cursor;
                cursor = db.rawQuery("SELECT * FROM subject where subject=?", new String[]{subject});
                while (cursor.moveToNext()) {
                    subjectcontent.setText(cursor.getString(2));
                }
            }
        });

        mHelper.close();
    }
}
