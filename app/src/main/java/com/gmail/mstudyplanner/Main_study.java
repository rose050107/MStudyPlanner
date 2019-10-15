package com.gmail.mstudyplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main_study extends AppCompatActivity {

    Button yesterday;
    Button tomorrow;
    TextView today;
    Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_study);

        yesterday = (Button)findViewById(R.id.yesterday);
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String date = today.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                    Date curdate = sdf.parse(date);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(curdate);
                    Log.e("day", cal.toString());
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    Log.e("day", cal.toString());

                    Date d = new Date(cal.getTimeInMillis());
                    String todayDate = sdf.format(d);
                    today.setText(todayDate);

                }catch (Exception e){
                    Toast.makeText(Main_study.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        tomorrow = (Button)findViewById(R.id.tomorrow);
        tomorrow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        String date = today.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
                        Date curdate = sdf.parse(date);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(curdate);
                        cal.add(Calendar.DATE, 1);

                        Date d = new Date(cal.getTimeInMillis());
                        String todayDate = sdf.format(d);
                        today.setText(todayDate);

                    }catch (Exception e){
                        Toast.makeText(Main_study.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

        });

        today = (TextView)findViewById(R.id.today);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String todayDate = sdf.format(date);
        today.setText(todayDate);

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //xml로 만든 레이아웃을 뷰로 변환하기
                final LinearLayout linear =
                        (LinearLayout)View.inflate(Main_study.this, R.layout.activity_study_input, null);
                new AlertDialog.Builder(Main_study.this)
                        .setTitle("To-Do")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setView(linear)
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText subject = (EditText)linear.findViewById(R.id.subject);
                                EditText content = (EditText)linear.findViewById(R.id.content);
                                TextView txt = (TextView)findViewById(R.id.text);
                                txt.setText("과목:" + subject.getText() +
                                        " 내용:" + content.getText());
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });





        ImageView calendar = (ImageView)findViewById(R.id.pagecalendar);
        calendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_study.this, Main_calendar.class);
                startActivity(intent);
            }
        });

        ImageView home = (ImageView)findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_study.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageView check = (ImageView)findViewById(R.id.pagecheck);
        check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_study.this, Main_subject.class);
                startActivity(intent);
            }
        });
        ImageView my = (ImageView)findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_study.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

    }
}
