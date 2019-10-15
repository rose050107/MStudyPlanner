package com.gmail.mstudyplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText name, id, pw, pw1, school;
    RadioButton radioButton1, radioButton2;
    Button register, delete;

    String result;
    boolean idcheck;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText)findViewById(R.id.name);
        id = (EditText)findViewById(R.id.id);

        id.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @Override
            public void onFocusChange(View view, boolean focusable) {
                if(focusable == false) {
                    final Handler idcheckHandler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (result != null && result.equals("success")) {
                                Toast.makeText(RegisterActivity.this, "사용 가능한 아이디", Toast.LENGTH_LONG).show();
                                idcheck = true;
                            } else {
                                Toast.makeText(RegisterActivity.this, "사용이 불가능한 아이디", Toast.LENGTH_LONG).show();
                                idcheck = false;
                            }
                        }
                    };

                    //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                    Thread th = new Thread() {
                        public void run() {
                            StringBuilder html = new StringBuilder();
                            final String idinput = id.getText().toString().trim();
                            try {
                                String addr = "http://172.30.1.16:9080/StudyPlanner/idcheck.jsp?";
                                addr = addr + "id=" + idinput;

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
                            idcheckHandler.sendEmptyMessage(0);
                        }
                    };
                    th.start();
                }

            }
        });


        pw = (EditText)findViewById(R.id.pw);
        pw1 = (EditText)findViewById(R.id.pw1);
        school = (EditText)findViewById(R.id.school);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);

        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nameinput = name.getText().toString().trim();
                if(nameinput.length() < 1){
                    Toast.makeText(RegisterActivity.this, "이름은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                final String idinput = id.getText().toString().trim();
                if(idinput.length()<1){
                    Toast.makeText(RegisterActivity.this, "아이디는 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pwinput = pw.getText().toString().trim();
                if(pwinput.length()<1){
                    Toast.makeText(RegisterActivity.this, "비밀번호는 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                final String pw1input = pw1.getText().toString().trim();
                if(pw1input.length()<1){
                    Toast.makeText(RegisterActivity.this, "비밀번호 재입력은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!pwinput.equals(pw1input)){
                    Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(idcheck == false){
                    Toast.makeText(RegisterActivity.this, "아이디 중복 체크를 하셔야 합니다.", Toast.LENGTH_LONG).show();
                    return;
                }


                final String schoolinput = school.getText().toString().trim();

                final Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (result != null && result.equals("success")) {
                            Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "회원가입 실패", Toast.LENGTH_LONG).show();
                            idcheck = false;
                        }
                    }
                };

                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread() {
                    public void run() {
                        StringBuilder html = new StringBuilder();

                        try {
                            String addr = "http://172.30.1.16:9080/StudyPlanner/memberregister.jsp?";
                            addr = addr + "name=" + nameinput;
                            addr = addr + "&id=" + idinput;
                            addr = addr + "&pw=" + pwinput;
                            String categoryinput = "고입";
                            if(radioButton1.isChecked() == false) {
                                categoryinput = "대입";
                            }
                            addr = addr + "&category=" + categoryinput;
                            addr = addr + "&school=" + schoolinput;

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

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
