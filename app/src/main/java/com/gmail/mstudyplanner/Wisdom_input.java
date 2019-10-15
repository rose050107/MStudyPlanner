package com.gmail.mstudyplanner;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Wisdom_input extends AppCompatActivity {

    Button back;
    TextView wisdomPlus;
    EditText comment;
    Button wisdomin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisdom_input);

        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wisdomPlus = (TextView)findViewById(R.id.wisdomPlus);

        comment = (EditText)findViewById(R.id.comment);

        wisdomin = (Button)findViewById(R.id.wisdomin);
        wisdomin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBHelper mHelper = new DBHelper(Wisdom_input.this);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                if(comment.getText().toString().length() <= 0){
                    Toast.makeText(Wisdom_input.this, "내용은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                String temp = comment.getText().toString();
                Object [] arg = {temp};
                db.execSQL("delete from Wisdom;");
                db.execSQL("INSERT INTO Wisdom VALUES (?);", arg);
                mHelper.close();
                finish();
                Intent intent = new Intent(Wisdom_input.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
        return false;
    }

}
