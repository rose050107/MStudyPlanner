package com.gmail.mstudyplanner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MyPageActivity extends AppCompatActivity {

    Button edit;

    TextView memberid;

    EditText name;
    RadioButton radioButton1;
    RadioButton radioButton2;
    EditText school;

    Button logout;

    String result;
    @Override
    public void onResume(){
        super.onResume();

        memberid.setText(Session.id);

        name.setText(Session.name);
        if(Session.category.equals("고입")){
            radioButton1.setChecked(true);
        }else{
            radioButton2.setChecked(true);
        }

        school.setText(Session.school);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        memberid = (TextView)findViewById(R.id.memberid);

        name = (EditText)findViewById(R.id.name);

        radioButton1 = (RadioButton)findViewById(R.id.radioButton1);

        radioButton2 = (RadioButton)findViewById(R.id.radioButton2);

        school = (EditText)findViewById(R.id.school);

        edit = (Button)findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 데이터 있는지 확인
                String n = name.getText().toString().trim();
                if(n.length() < 1){
                    Toast.makeText(MyPageActivity.this, "이름은 필수 입력입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String n1 = school.getText().toString().trim();
                if(n1.length() < 1) {
                    Toast.makeText(MyPageActivity.this, "목표 학교는 필수 입력입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String n3 = "고입";
                if(radioButton1.isChecked() == false){
                    n3 = "대입";
                }


                final Handler updateHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        if(result != null && result.equals("success")){
                            Toast.makeText(MyPageActivity.this, "회원정보 수정 성공", Toast.LENGTH_LONG).show();
                            DBHelper mHelper = new DBHelper(MyPageActivity.this);
                            SQLiteDatabase db = mHelper.getWritableDatabase();
                            String [] args={Session.id};
                            db.delete("member", "id=?", args);

                            mHelper.close();

                            Session.id=null;
                            Session.name = null;
                            Session.school=null;
                            Session.category=null;

                            Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MyPageActivity.this, "회원정보 수정 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                } ;

                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread(){
                    public void run(){
                        StringBuilder html = new StringBuilder();
                        try {
                            String addr = Common.server + "/memberupdate.jsp?";
                            addr = addr +"id=" + Session.id;
                            String n = name.getText().toString().trim();
                            addr = addr +"&name=" + URLEncoder.encode(n, "utf-8");
                            //Log.e("addr", addr);

                            String n3 = "고입";
                            if(radioButton1.isChecked() == false){
                                n3 = "대입";
                            }
                            addr = addr +"&category=" + URLEncoder.encode(n3,"utf-8");

                            String n1 = school.getText().toString().trim();
                            addr = addr +"&school=" + URLEncoder.encode(n1,"utf-8");

                            URL url = new URL(addr);
                            HttpURLConnection conn =
                                    (HttpURLConnection)url.openConnection();
                            if (conn != null) {
                                conn.setConnectTimeout(10000);
                                conn.setUseCaches(false);
                                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    BufferedReader br = new BufferedReader(
                                            new
                                                    InputStreamReader(conn.getInputStream()));
                                    while(true){
                                        String line =
                                                br.readLine();
                                        if (line == null) break;
                                        html.append(line + '\n');
                                    }
                                    br.close();
                                    result = html.toString().trim();
                                }
                                conn.disconnect();
                            }
                        }
                        catch (Exception ex) {;}
                        updateHandler.sendEmptyMessage(0);
                    }
                };
                th.start();
            }
        });


        logout = (Button)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                DBHelper mHelper = new DBHelper(MyPageActivity.this);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                String [] args={Session.id};
                db.delete("member", "id=?", args);

                mHelper.close();
                Session.id=null;
                Session.name = null;
                Session.school=null;
                Session.category=null;
                Toast.makeText(MyPageActivity.this, "로그 아웃 하셨습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        ImageView calendar = (ImageView)findViewById(R.id.pagecalendar);
        calendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MyPageActivity.this, Main_calendar.class);
                startActivity(intent);
            }
        });

        ImageView pencil = (ImageView)findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MyPageActivity.this, Main_study.class);
                startActivity(intent);
            }
        });

        ImageView check = (ImageView)findViewById(R.id.pagecheck);
        check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MyPageActivity.this, Main_subject.class);
                startActivity(intent);
            }
        });

        ImageView home = (ImageView)findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MyPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
