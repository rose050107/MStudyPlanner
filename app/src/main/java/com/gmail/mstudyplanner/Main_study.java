package com.gmail.mstudyplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Main_study extends AppCompatActivity {

    ImageButton yesterday;
    ImageButton tomorrow;
    TextView today;
    Button add;

    Spinner subjectSpinner;
    ArrayList<String> subjectlist;
    ArrayAdapter<String>subjectadapter;

    Spinner contentSpinner;
    ArrayList<String> contentlist;
    ArrayAdapter<String>contentadapter;

    String subject;
    String content;
    String result;

    String studyjson;

    Thread studyThread;

    View [][] studyView;

    boolean studyFlag;

    int hour, minute;
    JSONArray ar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_study);

        studyView = new View[24][6];

        studyView[0][0] = (View)findViewById(R.id.t001);
        studyView[0][1] = (View)findViewById(R.id.t002);
        studyView[0][2] = (View)findViewById(R.id.t003);
        studyView[0][3] = (View)findViewById(R.id.t004);
        studyView[0][4] = (View)findViewById(R.id.t005);
        studyView[0][5] = (View)findViewById(R.id.t006);

        studyView[1][0] = (View)findViewById(R.id.t101);
        studyView[1][1] = (View)findViewById(R.id.t102);
        studyView[1][2] = (View)findViewById(R.id.t103);
        studyView[1][3] = (View)findViewById(R.id.t104);
        studyView[1][4] = (View)findViewById(R.id.t105);
        studyView[1][5] = (View)findViewById(R.id.t106);

        studyView[2][0] = (View)findViewById(R.id.t201);
        studyView[2][1] = (View)findViewById(R.id.t202);
        studyView[2][2] = (View)findViewById(R.id.t203);
        studyView[2][3] = (View)findViewById(R.id.t204);
        studyView[2][4] = (View)findViewById(R.id.t205);
        studyView[2][5] = (View)findViewById(R.id.t206);

        studyView[3][0] = (View)findViewById(R.id.t301);
        studyView[3][1] = (View)findViewById(R.id.t302);
        studyView[3][2] = (View)findViewById(R.id.t303);
        studyView[3][3] = (View)findViewById(R.id.t304);
        studyView[3][4] = (View)findViewById(R.id.t305);
        studyView[3][5] = (View)findViewById(R.id.t306);

        studyView[4][0] = (View)findViewById(R.id.t401);
        studyView[4][1] = (View)findViewById(R.id.t402);
        studyView[4][2] = (View)findViewById(R.id.t403);
        studyView[4][3] = (View)findViewById(R.id.t404);
        studyView[4][4] = (View)findViewById(R.id.t405);
        studyView[4][5] = (View)findViewById(R.id.t406);

        studyView[5][0] = (View)findViewById(R.id.t501);
        studyView[5][1] = (View)findViewById(R.id.t502);
        studyView[5][2] = (View)findViewById(R.id.t503);
        studyView[5][3] = (View)findViewById(R.id.t504);
        studyView[5][4] = (View)findViewById(R.id.t505);
        studyView[5][5] = (View)findViewById(R.id.t506);

        studyView[6][0] = (View)findViewById(R.id.t601);
        studyView[6][1] = (View)findViewById(R.id.t602);
        studyView[6][2] = (View)findViewById(R.id.t603);
        studyView[6][3] = (View)findViewById(R.id.t604);
        studyView[6][4] = (View)findViewById(R.id.t605);
        studyView[6][5] = (View)findViewById(R.id.t606);

        studyView[7][0] = (View)findViewById(R.id.t701);
        studyView[7][1] = (View)findViewById(R.id.t702);
        studyView[7][2] = (View)findViewById(R.id.t703);
        studyView[7][3] = (View)findViewById(R.id.t704);
        studyView[7][4] = (View)findViewById(R.id.t705);
        studyView[7][5] = (View)findViewById(R.id.t706);

        studyView[8][0] = (View)findViewById(R.id.t801);
        studyView[8][1] = (View)findViewById(R.id.t802);
        studyView[8][2] = (View)findViewById(R.id.t803);
        studyView[8][3] = (View)findViewById(R.id.t804);
        studyView[8][4] = (View)findViewById(R.id.t805);
        studyView[8][5] = (View)findViewById(R.id.t806);

        studyView[9][0] = (View)findViewById(R.id.t901);
        studyView[9][1] = (View)findViewById(R.id.t902);
        studyView[9][2] = (View)findViewById(R.id.t903);
        studyView[9][3] = (View)findViewById(R.id.t904);
        studyView[9][4] = (View)findViewById(R.id.t905);
        studyView[9][5] = (View)findViewById(R.id.t906);

        studyView[10][0] = (View)findViewById(R.id.t1001);
        studyView[10][1] = (View)findViewById(R.id.t1002);
        studyView[10][2] = (View)findViewById(R.id.t1003);
        studyView[10][3] = (View)findViewById(R.id.t1004);
        studyView[10][4] = (View)findViewById(R.id.t1005);
        studyView[10][5] = (View)findViewById(R.id.t1006);

        studyView[11][0] = (View)findViewById(R.id.t1101);
        studyView[11][1] = (View)findViewById(R.id.t1102);
        studyView[11][2] = (View)findViewById(R.id.t1103);
        studyView[11][3] = (View)findViewById(R.id.t1104);
        studyView[11][4] = (View)findViewById(R.id.t1105);
        studyView[11][5] = (View)findViewById(R.id.t1106);

        studyView[12][0] = (View)findViewById(R.id.t1201);
        studyView[12][1] = (View)findViewById(R.id.t1202);
        studyView[12][2] = (View)findViewById(R.id.t1203);
        studyView[12][3] = (View)findViewById(R.id.t1204);
        studyView[12][4] = (View)findViewById(R.id.t1205);
        studyView[12][5] = (View)findViewById(R.id.t1206);

        studyView[13][0] = (View)findViewById(R.id.t1301);
        studyView[13][1] = (View)findViewById(R.id.t1302);
        studyView[13][2] = (View)findViewById(R.id.t1303);
        studyView[13][3] = (View)findViewById(R.id.t1304);
        studyView[13][4] = (View)findViewById(R.id.t1305);
        studyView[13][5] = (View)findViewById(R.id.t1306);

        studyView[14][0] = (View)findViewById(R.id.t1401);
        studyView[14][1] = (View)findViewById(R.id.t1402);
        studyView[14][2] = (View)findViewById(R.id.t1403);
        studyView[14][3] = (View)findViewById(R.id.t1404);
        studyView[14][4] = (View)findViewById(R.id.t1405);
        studyView[14][5] = (View)findViewById(R.id.t1406);

        studyView[15][0] = (View)findViewById(R.id.t1501);
        studyView[15][1] = (View)findViewById(R.id.t1502);
        studyView[15][2] = (View)findViewById(R.id.t1503);
        studyView[15][3] = (View)findViewById(R.id.t1504);
        studyView[15][4] = (View)findViewById(R.id.t1505);
        studyView[15][5] = (View)findViewById(R.id.t1506);

        studyView[16][0] = (View)findViewById(R.id.t1601);
        studyView[16][1] = (View)findViewById(R.id.t1602);
        studyView[16][2] = (View)findViewById(R.id.t1603);
        studyView[16][3] = (View)findViewById(R.id.t1604);
        studyView[16][4] = (View)findViewById(R.id.t1605);
        studyView[16][5] = (View)findViewById(R.id.t1606);

        studyView[17][0] = (View)findViewById(R.id.t1701);
        studyView[17][1] = (View)findViewById(R.id.t1702);
        studyView[17][2] = (View)findViewById(R.id.t1703);
        studyView[17][3] = (View)findViewById(R.id.t1704);
        studyView[17][4] = (View)findViewById(R.id.t1705);
        studyView[17][5] = (View)findViewById(R.id.t1706);

        studyView[18][0] = (View)findViewById(R.id.t1801);
        studyView[18][1] = (View)findViewById(R.id.t1802);
        studyView[18][2] = (View)findViewById(R.id.t1803);
        studyView[18][3] = (View)findViewById(R.id.t1804);
        studyView[18][4] = (View)findViewById(R.id.t1805);
        studyView[18][5] = (View)findViewById(R.id.t1806);

        studyView[19][0] = (View)findViewById(R.id.t1901);
        studyView[19][1] = (View)findViewById(R.id.t1902);
        studyView[19][2] = (View)findViewById(R.id.t1903);
        studyView[19][3] = (View)findViewById(R.id.t1904);
        studyView[19][4] = (View)findViewById(R.id.t1905);
        studyView[19][5] = (View)findViewById(R.id.t1906);

        studyView[20][0] = (View)findViewById(R.id.t2001);
        studyView[20][1] = (View)findViewById(R.id.t2002);
        studyView[20][2] = (View)findViewById(R.id.t2003);
        studyView[20][3] = (View)findViewById(R.id.t2004);
        studyView[20][4] = (View)findViewById(R.id.t2005);
        studyView[20][5] = (View)findViewById(R.id.t2006);

        studyView[21][0] = (View)findViewById(R.id.t2101);
        studyView[21][1] = (View)findViewById(R.id.t2102);
        studyView[21][2] = (View)findViewById(R.id.t2103);
        studyView[21][3] = (View)findViewById(R.id.t2104);
        studyView[21][4] = (View)findViewById(R.id.t2105);
        studyView[21][5] = (View)findViewById(R.id.t2106);

        studyView[22][0] = (View)findViewById(R.id.t2201);
        studyView[22][1] = (View)findViewById(R.id.t2202);
        studyView[22][2] = (View)findViewById(R.id.t2203);
        studyView[22][3] = (View)findViewById(R.id.t2204);
        studyView[22][4] = (View)findViewById(R.id.t2205);
        studyView[22][5] = (View)findViewById(R.id.t2206);

        studyView[23][0] = (View)findViewById(R.id.t2301);
        studyView[23][1] = (View)findViewById(R.id.t2302);
        studyView[23][2] = (View)findViewById(R.id.t2303);
        studyView[23][3] = (View)findViewById(R.id.t2304);
        studyView[23][4] = (View)findViewById(R.id.t2305);
        studyView[23][5] = (View)findViewById(R.id.t2306);


        subjectlist = new ArrayList<>();
        subjectadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subjectlist);
        subjectSpinner = (Spinner)findViewById(R.id.subject);
        subjectSpinner.setAdapter(subjectadapter);

        contentlist = new ArrayList<>();
        contentadapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, contentlist);
        contentSpinner = (Spinner)findViewById(R.id.content);
        contentSpinner.setAdapter(contentadapter);

         // 뷰색깔
        final Handler whiteHandler = new Handler(){
          public void handleMessage(Message msg){
              for(View [] ar: studyView){
                  for(View imsi : ar){
                      //imsi.setBackgroundResource(R.drawable.stroke);
                      imsi.setBackground(new ColorDrawable(Color.WHITE));

                  }
              }
          }
        };

        yesterday = (ImageButton) findViewById(R.id.yesterday);
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    whiteHandler.sendEmptyMessage(0);

                    String date = today.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date curdate = sdf.parse(date);
                    //Log.e("현재 날짜", curdate.toString());

                    long curtime = curdate.getTime();
                    Date dday = new Date(curtime - 86400 * 1000);
                    String todayDate = sdf.format((dday));
                    today.setText(todayDate);
                    display();
                    // 데이터 불러오기
                } catch (Exception e) {
                    Toast.makeText(Main_study.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        tomorrow = (ImageButton) findViewById(R.id.tomorrow);
        tomorrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    whiteHandler.sendEmptyMessage(0);
                    String date = today.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date curdate = sdf.parse(date);

                    long curtime = curdate.getTime();
                    Date dday = new Date(curtime + 86400 * 1000);
                    // 1000/1=== 하루를 초로 나타낸것
                    String todayDate = sdf.format((dday));
                    today.setText(todayDate);

                    display();
                    // 데이터 가져오기
                } catch (Exception e) {
                    Toast.makeText(Main_study.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        today = (TextView) findViewById(R.id.today);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = sdf.format(date);
        today.setText(todayDate);


        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Handler stopHandler = new Handler(){
                    public void handleMessage(Message msg){
                        add.setText("공부시작");
                        Toast.makeText(Main_study.this, "공부를 중단 했습니다.", Toast.LENGTH_SHORT).show();
                        studyFlag = true;
                    }
                };

                final Handler studyHandler = new Handler(){
                  public void handleMessage(Message msg){
                      //studyView[hour][minute].setBackgroundResource(R.drawable.stroke);

                      for(View [] temp: studyView){
                          for(View imsi : temp){
                              imsi.setBackgroundResource(R.drawable.stroke);
                              //imsi.setBackground(new ColorDrawable(Color.WHITE));
                          }
                      }
                      try {
                          for (int i = 0; i < ar.length(); i++) {
                              String item = ar.getString(i);

                              int hour = Integer.parseInt(item.substring(0, 2));
                              // 00:00 중에 앞쪽 시간 부분 (0 ~ 1) -> 2전까지
                              int minute = Integer.parseInt(item.substring(3, 4));
                              // 00:00 중에 뒤쪽 분 부분 (3 ~ 4)
                              studyView[hour][minute].setBackground(new ColorDrawable(Color.RED));

                          }
                      }catch(Exception e){
                          Log.e("파싱예외", e.getMessage());
                      }

                      studyView[hour][minute].setBackground(new ColorDrawable(Color.RED));
                  }
                };

                if(subject == null || content == null){
                    Toast.makeText(Main_study.this, "과목과 내용을 선택하세요!!!", Toast.LENGTH_LONG).show();
                    return;
                }
                    if(add.getText().toString().equals("공부시작")){
                        add.setText("공부종료");
                        studyFlag = false;
                        studyThread = new Thread(){
                            public void run() {
                                try {
                                    while(true){
                                        if(studyFlag == true){
                                            break;
                                        }

                                        Calendar cal = new GregorianCalendar();
                                        Date date = new Date(cal.getTimeInMillis());
                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                        final String time = sdf.format(date);
                                        hour = Integer.parseInt(time.substring(0,2));
                                        minute = Integer.parseInt(time.substring(3,4));
                                        studyHandler.sendEmptyMessage(0);

                                        final String curdate = today.getText().toString();

                                        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                                        Thread th = new Thread() {
                                            public void run() {
                                                StringBuilder html = new StringBuilder();

                                                try {
                                                    String addr = Common.server + "/studyregister.jsp?";
                                                    addr = addr + "id=" + Session.id;
                                                    addr = addr + "&subject=" + URLEncoder.encode(subject, "utf-8");
                                                    addr = addr + "&content=" + URLEncoder.encode(content, "utf-8");
                                                    addr = addr + "&date=" + curdate;
                                                    addr = addr + "&stime=" + time;


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
                                                    return;
                                                }
                                            }
                                        };
                                        Thread.sleep(10*60*1000);
                                        // 시작 후 색은 칠해주지만 10분 후 부터 저장 시작
                                        th.start();
                                    }
                                } catch (InterruptedException e) {
                                    Log.e("예외", e.getMessage());
                                }
                            }
                        };
                        studyThread.start();
                    }else{
                        stopHandler.sendEmptyMessage(0);
                    }
            }
        });




            ImageView calendar = (ImageView) findViewById(R.id.pagecalendar);
            calendar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(Main_study.this, Main_calendar.class);
                    startActivity(intent);
                }
            });

            ImageView home = (ImageView) findViewById(R.id.pagehome);
            home.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(Main_study.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            ImageView check = (ImageView) findViewById(R.id.pagecheck);
            check.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(Main_study.this, Main_subject.class);
                    startActivity(intent);
                }
            });
            ImageView my = (ImageView) findViewById(R.id.pagemy);
            my.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(Main_study.this, MyPageActivity.class);
                    startActivity(intent);
                }
            });
        }

        public void display(){
            // 날짜 앞 뒤로 바꾸면 변하는 view
            final Handler viewHandler = new Handler(){
                public void handleMessage(Message msg){
                    try {
                        ar = new JSONArray(studyjson);

                        for(View [] temp: studyView){
                            for(View imsi : temp){
                                imsi.setBackgroundResource(R.drawable.stroke);
                            }
                        }
                        //ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < ar.length(); i++) {
                            String item = ar.getString(i);

                            int hour = Integer.parseInt(item.substring(0,2));
                            int minute = Integer.parseInt(item.substring(3,4));
                            studyView[hour][minute].setBackground(new ColorDrawable(Color.RED));

                        }
                    }catch(Exception e){
                        Log.e("item", e.getMessage());
                    }

                }
            };
            //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
            Thread th1 = new Thread() {
                public void run() {
                    StringBuilder html = new StringBuilder();

                    try {
                        // 공부한 시간들 가져옴
                        String addr = Common.server + "/studylist.jsp?";
                        addr = addr + "id=" + Session.id;
                        addr = addr + "&subject=" + URLEncoder.encode(subject, "utf-8");
                        addr = addr + "&content=" + URLEncoder.encode(content, "utf-8");
                        addr = addr + "&date=" + today.getText().toString();;

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
                                studyjson = html.toString().trim();
                                //Log.e("log", studyjson);
                            }

                            conn.disconnect();
                        }
                    } catch (Exception ex) {
                        Log.e("예외", ex.getMessage());
                    }
                    viewHandler.sendEmptyMessage(0);
                }
            };
            th1.start();
        }


    public void onResume(){
        super.onResume();
        // 그 화면에 처음으로 들어오면 가져와야 할것들

        final Handler listHandler = new Handler(){
            public void handleMessage(Message msg){
                // 좌 리스트
                try {
                    ar = new JSONArray(result.trim());

                   subjectlist.clear();
                    for (int i = 0; i < ar.length(); i++) {
                        subjectlist.add(ar.getString(i));
                    }

                    subjectadapter.notifyDataSetChanged();

                    subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView,View view, int pos, long l) {
                            subject = subjectlist.get(pos);


                            final Handler contentHandler = new Handler() {
                                // 좌 세부내용
                                public void handleMessage(Message msg) {
                                    try {
                                        ar = new JSONArray(result.trim());
                                        contentlist = new ArrayList<>();
                                        for (int i = 0; i < ar.length(); i++) {
                                            contentlist.add(ar.getString(i));
                                        }

                                        contentadapter = new ArrayAdapter<>(Main_study.this, android.R.layout.simple_list_item_1, contentlist);
                                        contentSpinner = (Spinner)findViewById(R.id.content);
                                        contentSpinner.setAdapter(contentadapter);

                                        contentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                                                content = contentlist.get(pos);

                                               final Handler viewHandler = new Handler(){
                                                    public void handleMessage(Message msg){
                                                        // 우 뷰
                                                        //Log.e("뷰 핸들러", "호출");
                                                        for(View [] ar: studyView){
                                                            for(View imsi : ar){
                                                                //Log.e("뷰 핸들러", "클리어");
                                                                imsi.setBackground(new ColorDrawable(Color.WHITE));
                                                            }
                                                        }

                                                        try {
                                                            ar = new JSONArray(studyjson);

                                                            for(View [] temp: studyView){
                                                                for(View imsi : temp){
                                                                    imsi.setBackgroundResource(R.drawable.stroke);
                                                                }
                                                            }

                                                            for (int i = 0; i < ar.length(); i++) {
                                                                String item = ar.getString(i);

                                                                int hour = Integer.parseInt(item.substring(0,2));
                                                                int minute = Integer.parseInt(item.substring(3,4));
                                                                studyView[hour][minute].setBackground(new ColorDrawable(Color.RED));
                                                            }
                                                        }catch(Exception e){
                                                            Log.e("item", e.getMessage());
                                                        }
                                                    }
                                                };

                                                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                                                Thread th1 = new Thread() {
                                                    public void run() {
                                                        // 좌 리스트에 들어갈 것들 불러오기
                                                        StringBuilder html = new StringBuilder();

                                                        try {
                                                            String addr = Common.server + "studylist.jsp?";
                                                            addr = addr + "id=" + Session.id;
                                                            addr = addr + "&subject=" + URLEncoder.encode(subject,"utf-8");
                                                            addr = addr + "&content=" + URLEncoder.encode(content,"utf-8");
                                                            addr = addr + "&date=" + today.getText().toString();;

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
                                                                    studyjson = html.toString().trim();
                                                                    Log.e("log", studyjson);
                                                                }

                                                                conn.disconnect();
                                                            }
                                                        } catch (Exception ex) {
                                                            Log.e("예외", ex.getMessage());
                                                        }
                                                        viewHandler.sendEmptyMessage(0);
                                                    }
                                                };
                                                th1.start();
                                            }
                                            public void onNothingSelected(AdapterView<?> adapterView){

                                            }
                                        });

                                    } catch (Exception ex) {
                                    }
                                }
                            };

                            //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                            Thread th = new Thread() {
                                public void run() {
                                    StringBuilder html = new StringBuilder();

                                    try {
                                        // 좌 리스트에 들어갈 content 불러오기
                                        String addr = Common.server + "/subjectlist.jsp?";
                                        addr = addr + "id=" + Session.id;
                                        addr = addr + "&subject=" + URLEncoder.encode(subject,"utf-8");

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
                                    contentHandler.sendEmptyMessage(0);
                                }
                            };
                            th.start();
                        }
                        public void onNothingSelected(AdapterView<?> adapterView){

                        }
                    });

                } catch (JSONException e) {
                    Log.e("예외", e.getMessage());
                }
            }
        };

        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
        Thread th = new Thread() {
            public void run() {
                StringBuilder html = new StringBuilder();

                try {
                    // 좌 리스트에 들어갈 주과목 불러오기
                    String addr = Common.server + "majorsubjectlist.jsp?";
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
                listHandler.sendEmptyMessage(0);
            }
        };
        th.start();
    }
}
