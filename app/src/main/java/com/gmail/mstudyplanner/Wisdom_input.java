package com.gmail.mstudyplanner;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Wisdom_input extends AppCompatActivity {

    ImageButton back;
    TextView wisdomPlus;
    EditText comment;
    Button wisdomin;

    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisdom_input);

        back = (ImageButton)findViewById(R.id.back);
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
                final String content = comment.getText().toString().trim();
                if(content.length() <= 0 ){
                    Toast.makeText(Wisdom_input.this, "명언은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                final Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (result != null && result.equals("success")) {
                            Toast.makeText(Wisdom_input.this, "명언 삽입 성공", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Wisdom_input.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Wisdom_input.this, "명언 삽입 실패", Toast.LENGTH_LONG).show();

                        }

                    }
                };

                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread() {
                    public void run() {
                        StringBuilder html = new StringBuilder();

                        try {
                            String addr = Common.server + "/wisdomregister.jsp?";
                            addr = addr + "id=" + Session.id;

                            addr = addr + "&content=" + URLEncoder.encode(content.replace("\n", "--"),"utf-8");

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);
        return false;
    }
}
