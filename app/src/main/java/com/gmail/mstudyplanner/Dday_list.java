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
import java.util.ArrayList;

public class Dday_list extends AppCompatActivity {

    Button back, delete;
    TextView Dday;
    Button add;
    ListView listView;

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dday_list);


        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });

        delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        add = (Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dday_list.this, DdayInputActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listview);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        final Handler listHandler = new Handler(){
            public void handleMessage(Message msg){
                try {
                    JSONArray ar = new JSONArray(result.trim());
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject item = ar.getJSONObject(i);
                        String title = item.getString("title");
                        String ddaydate = item.getString("ddaydate");
                        list.add(title + ":" + ddaydate);
                    }

                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(Dday_list.this,
                            android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    listView.setDivider(new ColorDrawable(Color.RED));
                    listView.setDividerHeight(2);

                } catch (JSONException e) {
                    Toast.makeText(Dday_list.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
        Thread th = new Thread() {
            public void run() {
                StringBuilder html = new StringBuilder();

                try {
                    String addr = "http://172.30.1.16:9080/StudyPlanner/ddaylist.jsp?";
                    addr = addr + "id=" + Session.id;
                    Log.e("addr", addr);



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
                            Log.e("result", result);
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
