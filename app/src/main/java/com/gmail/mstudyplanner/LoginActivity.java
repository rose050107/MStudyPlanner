package com.gmail.mstudyplanner;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText id, pw;
    Button login, register;

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("로그인");
        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);

        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String idinput = id.getText().toString().trim();
                final String pwinput = pw.getText().toString().trim();

                if(idinput.length() < 1){
                    Toast.makeText(LoginActivity.this, "아이디는 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(pwinput.length() < 1){
                    Toast.makeText(LoginActivity.this, "비밀번호는 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }

                final Handler loginHandler = new Handler(){
                    @Override
                   public void handleMessage(Message msg){

                       if(result != null && !result.trim().equals("fail")){
                           Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_LONG).show();
                           Session.id = idinput;

                           String [] ar = result.trim().split(",");
                           Session.name = ar[0].trim();
                           Session.category = ar[1].trim();
                           Session.school = ar[2].trim();

                           DBHelper mHelper = new DBHelper(LoginActivity.this);
                           SQLiteDatabase db = mHelper.getWritableDatabase();

                           ContentValues row = new ContentValues();
                           row.put("id", Session.id);
                           row.put("name", Session.name);
                           row.put("school", Session.school);
                           row.put("category", Session.category);

                           db.insert("member", null, row);

                           mHelper.close();

                           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           // 로그인 화면이 2번 안만들어지게 함
                           startActivity(intent);
                       }else{
                           Toast.makeText(LoginActivity.this, "없는 아이디이거나 틀린 비밀번호입니다.", Toast.LENGTH_LONG).show();
                       }
                   }
                } ;

                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread(){
                    public void run(){
                        StringBuilder html = new StringBuilder();
                        try {
                            String addr = Common.server + "/login.jsp?";
                            addr = addr +"id=" + idinput;
                            addr = addr + "&pw=" + pwinput;

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
                        loginHandler.sendEmptyMessage(0);
                    }
                };
                th.start();
            }
        });

        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
