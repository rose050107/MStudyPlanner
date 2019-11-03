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
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class Dday_list extends AppCompatActivity {

    ImageButton back;
    TextView DdayList;
    ImageButton delete;
    ImageButton add;

    ListView listview;
    ArrayList<String> list;
    ArrayList<Integer> idlist;
    ArrayAdapter<String> adapter;

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dday_list);

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){//Btton->View
            @Override
            public void onClick(View view){
                finish();
            }
        });

        DdayList = (TextView)findViewById(R.id.DdayList);

        delete = (ImageButton) findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray sb = listview.getCheckedItemPositions();
                if (sb.size() != 0) {
                    for (int i = listview.getCount() - 1; i >= 0 ; i--) {
                        if (sb.get(i)) {
                            list.remove(i);
                            final int _id = idlist.get(i);
                            Thread th = new Thread() {
                                public void run() {
                                    try {
                                        String addr = Common.server+ "/ddaydelete.jsp?";
                                        addr = addr + "_id=" + _id;

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
                                        }
                                    }catch (Exception ex1) {
                                        Log.e("예외", ex1.getMessage());
                                    }
                                }
                            };
                            th.start();
                        }
                    }
                    listview.clearChoices();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(Dday_list.this, "삭제 성공", Toast.LENGTH_SHORT).show();
                }
            }
        });

        add = (ImageButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dday_list.this, DdayInputActivity.class);
                startActivity(intent);
            }
        });

        listview = (ListView) findViewById(R.id.listview);

    }

    @Override
    protected  void onResume(){
        super.onResume();
        Toast.makeText(Dday_list.this,"실행", Toast.LENGTH_LONG).show();
        final Handler listHandler = new Handler(){
            public void handleMessage(Message msg){
                try {
                    JSONArray ar = new JSONArray(result.trim());
                    list = new ArrayList<>();
                    idlist = new ArrayList<>();
                    //ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject item = ar.getJSONObject(i);
                        String title = item.getString("title");
                        String ddaydate = item.getString("ddaydate");
                        list.add(title + ":" + ddaydate);
                        idlist.add(item.getInt("_id"));
                    }

                    //어댑터 생성
                    adapter =new ArrayAdapter<String>(Dday_list.this,android.R.layout.simple_list_item_multiple_choice, list);


                    //어댑터 설정
                    listview.setAdapter(adapter);
                    //listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listview.setDivider(new ColorDrawable(Color.RED));
                    listview.setDividerHeight(2);

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
                    String addr = Common.server + "/ddaylist.jsp?";
                    addr = addr + "id=" + Session.id;
                    //addr=addr + "id=" + "kjh17";
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
