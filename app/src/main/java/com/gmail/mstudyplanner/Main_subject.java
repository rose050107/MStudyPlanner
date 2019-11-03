package com.gmail.mstudyplanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Main_subject extends AppCompatActivity {

    TextView list;
    Button add;

    ListView subjectlist;
    ArrayList <String> majorsubject;
    ArrayAdapter<String> majoradapter;
    String result;

    EditText subjectcontent;
    Button addbtn;
    Button delete;
    String delcontent = "";

    String subject = "";
    String content;

    ListView contentlistview;
    ArrayList <String> contentlist;
    ArrayAdapter<String> contentadapter;
    String jsoncontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_subject);

        subjectlist = (ListView)findViewById(R.id.subjectlist);

        contentlistview = (ListView)findViewById(R.id.contentlist);
        contentlist = new ArrayList<>();
        contentadapter = new ArrayAdapter<>(Main_subject.this, android.R.layout.simple_list_item_1, contentlist);

        subjectcontent = (EditText)findViewById(R.id.subjectcontent);

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_subject.this, Subject_input.class);
                startActivity(intent);
            }
        });


        addbtn = (Button)findViewById(R.id.addbtn);

        addbtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(subject == null || subject.length() <= 0 ){
                    Toast.makeText(Main_subject.this, "과목을 먼저 선택하세요!!!", Toast.LENGTH_LONG).show();
                    return;
                }
                content = subjectcontent.getText().toString().trim();
                if(content.length() <= 0 ){
                    Toast.makeText(Main_subject.this, "내용은 필수 입력입니다.!!!", Toast.LENGTH_LONG).show();
                    return;
                }

                final Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (result != null && result.equals("success")) {
                            Toast.makeText(Main_subject.this, "내용 입력 성공", Toast.LENGTH_LONG).show();
                            contentlist.add(content);
                            contentadapter.notifyDataSetChanged();
                            subjectcontent.setText("");
                        }
                       else if (result != null && result.equals("exist")) {
                            Toast.makeText(Main_subject.this, "이미 존재하는 내용입니다.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Main_subject.this, "내용 입력 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                };


                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread() {
                    public void run() {
                        StringBuilder html = new StringBuilder();

                        try {
                            String addr = Common.server + "/subjectregister.jsp?";
                            addr = addr + "id=" + Session.id;
                            addr = addr + "&subject=" + URLEncoder.encode(subject,"utf-8");
                            addr = addr + "&content=" + URLEncoder.encode(content, "utf-8");


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

        list = (TextView)findViewById(R.id.list);



        ImageView calendar = (ImageView)findViewById(R.id.pagecalendar);
        calendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, Main_calendar.class);
                startActivity(intent);
            }
        });

        ImageView pencil = (ImageView)findViewById(R.id.pagepencil);
        pencil.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, Main_study.class);
                startActivity(intent);
            }
        });
        ImageView home = (ImageView)findViewById(R.id.pagehome);
        home.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageView my = (ImageView)findViewById(R.id.pagemy);
        my.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Main_subject.this, MyPageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(subjectcontent.getWindowToken(), 0);
        return false;
    }

    public void onResume(){
        super.onResume();


        majorsubject = new ArrayList<>();

        final Handler listHandler = new Handler(){
            public void handleMessage(Message msg){
                try {
                    JSONArray ar = new JSONArray(result.trim());

                    //ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < ar.length(); i++) {
                        majorsubject.add(ar.getString(i));
                    }

                    majoradapter = new ArrayAdapter<>(Main_subject.this, android.R.layout.simple_list_item_1, majorsubject);
                    subjectlist.setAdapter(majoradapter);
                    subjectlist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    subjectlist.setDivider(new ColorDrawable(Color.BLUE));
                    subjectlist.setDividerHeight(2);

                    subjectlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            subject = majorsubject.get(i);

                            final Handler contentHandler = new Handler() {
                                public void handleMessage(Message msg) {
                                    try {
                                        JSONArray ar = new JSONArray(jsoncontent.trim());
                                        contentlist.clear();
                                        //ArrayList<String> list = new ArrayList<>();
                                        for (int i = 0; i < ar.length(); i++) {
                                            contentlist.add(ar.getString(i));
                                        }

                                        //어댑터 생성
                                        contentadapter =new ArrayAdapter<String>(Main_subject.this,android.R.layout.simple_list_item_multiple_choice, contentlist);
                                        //어댑터 설정
                                        contentlistview.setAdapter(contentadapter);
                                        //listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                        contentlistview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                                        contentlistview.setDivider(new ColorDrawable(Color.MAGENTA));
                                        contentlistview.setDividerHeight(6);
                                        subjectcontent.setText("");

                                    } catch (Exception ex) {
                                    }
                                }
                            };

                            //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                            Thread th = new Thread() {
                                public void run() {
                                    StringBuilder html = new StringBuilder();

                                    try {
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
                                                jsoncontent = html.toString().trim();
                                                Log.e("jsoncontent", jsoncontent);
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
                    });

                } catch (JSONException e) {
                    Log.e("예외", e.getMessage());
                }
            }
        };




        delete = (Button) findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sb = contentlistview.getCheckedItemPositions();

                if (sb.size() != 0) {
                    for (int i = contentlistview.getCount() - 1; i >= 0 ; i--) {
                        if (sb.get(i)) {
                            delcontent = delcontent + contentlist.get(i) + ",";
                            contentlist.remove(i);

                        }
                    }
                }

                final Handler deleteHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        contentlistview.clearChoices();
                        contentadapter.notifyDataSetChanged();
                        Toast.makeText(Main_subject.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                    }
                };

                Thread th = new Thread() {
                    public void run() {
                        try {
                            String addr = Common.server+ "/subjectdelete.jsp?";
                            addr = addr + "id=" + Session.id;
                            addr = addr + "&subject=" + URLEncoder.encode(subject,"utf-8");
                            delcontent = delcontent.substring(0, delcontent.length()-1);
                            // 맨 마지막 , 는 지우기
                            addr = addr + "&content=" + URLEncoder.encode(delcontent,"utf-8");

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

                                    }
                                    br.close();
                                }

                                conn.disconnect();
                                deleteHandler.sendEmptyMessage(0);
                            }
                        }catch (Exception ex1) {
                            Log.e("예외", ex1.getMessage());
                        }
                    }
                };
                th.start();
            }
        });



        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
        Thread th = new Thread() {
            public void run() {
                StringBuilder html = new StringBuilder();

                try {
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
