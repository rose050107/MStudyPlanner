package com.gmail.mstudyplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main_calendar extends AppCompatActivity {

    CalendarView calendarView;
    EditText content;
    Button add;

    String date;

    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);

        content = (EditText)findViewById(R.id.content);


        Calendar cal = Calendar.getInstance();
        calendarView = (CalendarView)findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                                                 public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                                                     content.setText("");
                                                     date = String.format("%4d-%02d-%02d", year, month + 1, dayOfMonth);

                                                     final Handler calRegisterHandler = new Handler() {
                                                         public void handleMessage(Message msg) {
                                                             try {
                                                                 JSONArray ar = new JSONArray(result.trim());
                                                                 String disp="";

                                                                 for (int i = 0; i < ar.length(); i++) {
                                                                     disp = disp + ar.getString(i).replace("__", "\n");
                                                                     // 데이터베이스에서 2줄로 저장
                                                                 }
                                                                 content.setText(disp);
                                                             }catch (Exception e){}
                                                         }
                                                     };

                                                     //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                                                     Thread th = new Thread() {
                                                         public void run() {
                                                             StringBuilder html = new StringBuilder();
                                                             try {
                                                                 String addr = Common.server + "/callist.jsp?";
                                                                 addr = addr + "id=" + Session.id;
                                                                 addr = addr + "&date=" + date;
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
                                                                 Log.e("예외", ex.getMessage());
                                                             }
                                                             calRegisterHandler.sendEmptyMessage(0);
                                                         }
                                                     };
                                                     th.start();
                                                 }
                                             });





        add = (Button)findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if (content.getText().toString().trim().length() <= 0) {
                    Toast.makeText(Main_calendar.this, "내용을 입력하세요!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (date == null) {
                    Toast.makeText(Main_calendar.this, "날짜를 선택하세요!", Toast.LENGTH_LONG).show();
                    return;
                }
                final Handler calRegisterHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        if(result.trim().equals("success")){
                            Toast.makeText(Main_calendar.this, "일정등록 성공", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(Main_calendar.this, "일정등록 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread() {
                    public void run() {
                        StringBuilder html = new StringBuilder();
                        try {
                            String addr = Common.server + "/calregister.jsp?";
                            addr = addr + "id=" + Session.id;
                            String c =  content.getText().toString().trim().replace("\n", "__");
                            addr = addr + "&content=" + URLEncoder.encode(c, "utf-8");
                            addr = addr + "&date=" + date;
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
                            Log.e("예외", ex.getMessage());
                        }
                        calRegisterHandler.sendEmptyMessage(0);
                    }
                };
                th.start();
            }
        });



        ImageView home = (ImageView)findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView pencil = (ImageView)findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, Main_study.class);
                startActivity(intent);
            }
        });

        ImageView check = (ImageView)findViewById(R.id.pagecheck);
        check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, Main_subject.class);
                startActivity(intent);
            }
        });

        ImageView my = (ImageView)findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_calendar.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

        final Handler calRegisterHandler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    JSONArray ar = new JSONArray(result.trim());
                    String disp="";

                    for (int i = 0; i < ar.length(); i++) {
                        disp = disp + ar.getString(i).replace("__", "\n");
                    }
                    content.setText(disp);
                }catch (Exception e){}
            }
        };

        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
        Thread th = new Thread() {
            public void run() {
                StringBuilder html = new StringBuilder();
                try {
                    String addr = Common.server + "/callist.jsp?";
                    addr = addr + "id=" + Session.id;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    addr = addr + "&date=" + sdf.format(new java.util.Date());
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
                    Log.e("예외", ex.getMessage());
                }
                calRegisterHandler.sendEmptyMessage(0);
            }
        };
        th.start();
    }

}
