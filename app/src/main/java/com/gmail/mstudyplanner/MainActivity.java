package com.gmail.mstudyplanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button DDay;
    ArrayList<String> list;
    TextView today;
    TextView study;

    TextView Wisdom;
    String result;

    String studytime;

    String wisdom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DDay = (Button) findViewById(R.id.DDay);
        DDay.setOnClickListener(new View.OnClickListener() {
            @Override
            //public void onClick(View v) {
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Dday_list.class);
                    startActivity(intent);
                }
            //}
        });



        today = (TextView) findViewById(R.id.today);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(date);
        today.setText(todayDate);

        study = (TextView) findViewById(R.id.study);
        //공부합계 시간을 가져와서 TextViet에 세팅하기

        Wisdom = (TextView) findViewById(R.id.Wisdom);
        Wisdom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Wisdom_input.class);
                startActivity(intent);

            }
        });

        ImageView calendar = (ImageView) findViewById(R.id.pagecalendar);
        calendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main_calendar.class);
                startActivity(intent);
            }
        });

        ImageView pencil = (ImageView) findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main_study.class);
                startActivity(intent);
            }
        });

        ImageView home = (ImageView) findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView check = (ImageView) findViewById(R.id.pagecheck);
        check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main_subject.class);
                startActivity(intent);
            }
        });

        ImageView my = (ImageView) findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle(Session.school);

        DBHelper mHelper = new DBHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM member", null);
        // 로그인 후 4개 정보를 가져옴
        if (cursor.moveToNext()) {
            Session.id = cursor.getString(0);
            Session.name = cursor.getString(1);
            Session.school = cursor.getString(2);
            Session.category= cursor.getString(3);
        }
        cursor.close();
        mHelper.close();

        if (Session.id == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        final Handler ddayHandler = new Handler() {
            public void handleMessage(Message msg) {
                try {

                    if (result != null) {//&& result.equals("success")
                        JSONObject jsonObject = new JSONObject(result.trim());
                        String resultTitle = jsonObject.getString("title");
                        String resultDdaydate = jsonObject.getString("ddaydate");

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date today = new Date();
                        try {
                            Date dday = formatter.parse(resultDdaydate);
                            long diff = dday.getTime() - today.getTime();
                            long diffDays = (diff / (24 * 60 * 60 * 1000));

                            if(diffDays == 0){
                                if(dday.getDay() != today.getDay())
                                    diffDays = 1;
                            }
                            DDay.setText(resultTitle + " - " + diffDays + "일 남음");
                            if(dday.getDay() == today.getDay()){
                                DDay.setText(resultTitle + " - " +"오늘");
                            }
                        }
                        catch (Exception e){
                            Log.e("파싱 예외", e.getMessage());
                        }

                        JSONArray ar = new JSONArray(result.trim());
                        list = new ArrayList<>();
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject item = ar.getJSONObject(i);
                            String title = item.getString("title");
                            String ddaydate = item.getString("ddaydate");
                            list.add(title + ":" + ddaydate);
                        }
                    } else {
                        DDay.setText("일정이 없습니다."+"추가하려면 버튼을 누르세요");
                    }

                } catch (JSONException e) { }
            }

        };

        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
        Thread th = new Thread() {
            public void run() {
                StringBuilder html = new StringBuilder();
                try {
                    String addr = Common.server + "/dday.jsp?";
                    addr = addr + "id=" + Session.id;

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
                ddayHandler.sendEmptyMessage(0);
            }
        };
        th.start();

        final Handler studyTimeHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (studytime != null) {//&& result.equals("success")
                    try {
                        JSONArray ar = new JSONArray(studytime);
                        int time = ar.getInt(0) * 10;
                        int h = time/60;
                        int m = time%60;
                        String disp = String.format("오늘 %02d:%02d 분 공부함", h, m);
                        study.setText(disp);
                    } catch (Exception e) {}
                }
            }
        };

        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
        Thread th1 = new Thread() {
            public void run() {
                StringBuilder html = new StringBuilder();
                try {
                    String addr = Common.server + "/studytime.jsp?";
                    addr = addr + "id=" + Session.id;
                    addr = addr + "&date=" + today.getText().toString();

                    URL url = new URL(addr);
                    HttpURLConnection conn =
                            (HttpURLConnection) url.openConnection();
                    if (conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                            while (true) {
                                String line =
                                        br.readLine();
                                if (line == null) break;
                                html.append(line + '\n');
                            }
                            br.close();
                            studytime = html.toString().trim();
                        }
                        conn.disconnect();
                    }
                } catch (Exception ex) {
                    Log.e("예외", ex.getMessage());
                }
                studyTimeHandler.sendEmptyMessage(0);
            }
        };
        th1.start();

        final Handler wisdomHandler = new Handler() {
            public void handleMessage(Message msg) {
                wisdom = wisdom.replace("--", "\n");
                Wisdom.setText(wisdom);
                Wisdom.setTextColor(Color.parseColor("#990099"));
                            }
        };

        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
        Thread th2 = new Thread() {
            public void run() {
                StringBuilder html = new StringBuilder();
                try {
                    String addr = Common.server + "/wisdomdetail.jsp?";
                    addr = addr + "id=" + Session.id;

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
                            wisdom = html.toString().trim();
                        }
                        conn.disconnect();
                    }
                } catch (Exception ex) {
                    Log.e("예외", ex.getMessage());
                }
                wisdomHandler.sendEmptyMessage(0);
            }
        };
        th2.start();
    }
}