package com.gmail.mstudyplanner;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Subject_input extends AppCompatActivity {

    TextView title;
    EditText subject;

    Button plus;
    ImageButton back;

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_input);

        title = (TextView)findViewById(R.id.title);

        subject = (EditText)findViewById(R.id.subject);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        plus = (Button) findViewById(R.id.plus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sub = subject.getText().toString();
                if(subject.getText().toString().length() <= 0 ){
                    Toast.makeText(Subject_input.this, "과목 이름은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                final Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (result != null && result.equals("success")) {
                            Toast.makeText(Subject_input.this, "과목 삽입 성공", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else if (result != null && result.equals("exist")) {
                            Toast.makeText(Subject_input.this, "과목이 이미 존재", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(Subject_input.this, "과목 삽입 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread() {
                    public void run() {
                        StringBuilder html = new StringBuilder();

                        try {
                            String addr = Common.server + "/majorsubjectregister.jsp?";
                            addr = addr + "id=" + Session.id;
                            addr = addr + "&subject=" + URLEncoder.encode(sub, "utf-8");


                            URL url = new URL(addr);
                            HttpURLConnection conn =
                                    (HttpURLConnection) url.openConnection();
                            if (conn != null) {
                                conn.setConnectTimeout(10000);
                                conn.setUseCaches(false);
                                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    BufferedReader br = new BufferedReader(
                                            new
                                                    InputStreamReader(conn.getInputStream()));
                                    while (true) {
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
                        } catch (Exception ex) {
                            ;
                        }
                        registerHandler.sendEmptyMessage(0);
                    }
                };
                th.start();


            }
        });


    }
}
