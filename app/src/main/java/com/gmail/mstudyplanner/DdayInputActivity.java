package com.gmail.mstudyplanner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;

public class DdayInputActivity extends AppCompatActivity {

    ImageButton back;

    TextView DdayPlus;
    TextView name;

    EditText namein;
    TextView date;

    EditText datein;
    ImageButton cal;
    Button ddayadd;

    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dday_input);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DdayPlus = (TextView)findViewById(R.id.DdayPlus);

        name = (TextView)findViewById(R.id.name);

        namein = (EditText) findViewById(R.id.namein);

        date = (TextView)findViewById(R.id.date);

        datein = (EditText) findViewById(R.id.datein);

        cal = (ImageButton) findViewById(R.id.cal);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c= Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(DdayInputActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                       datein.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                       // 월이 하나 적게 나옴
                    }
                    },year, month, day);
                datePickerDialog.show();
            }
        });

        ddayadd = (Button) findViewById(R.id.ddayadd);
        ddayadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(namein.getText().toString().length() <= 0 || datein.getText().length() <= 0){
                    Toast.makeText(DdayInputActivity.this, "제목과 날짜는 필수 입력입니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                String temp = datein.getText().toString();
                String [] imsi = temp.split("-");
                final String date = String.format("%4d-%02d-%02d", Integer.parseInt(imsi[0]), Integer.parseInt(imsi[1]), Integer.parseInt(imsi[2]));


                final Handler registerHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (result != null && result.equals("success")) {
                            Toast.makeText(DdayInputActivity.this, "일정 등록 성공", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(DdayInputActivity.this, "일정 등록 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                //안드로이드에서는 네트워크에 접속할려면 스레드를 사용
                Thread th = new Thread() {
                    public void run() {
                        StringBuilder html = new StringBuilder();

                        try {
                            String addr = Common.server+ "/ddayregister.jsp?";
                            addr = addr + "id=" + Session.id;
                            addr = addr + "&title=" + URLEncoder.encode(namein.getText().toString(), "utf-8");
                            addr = addr + "&ddaydate=" + date;

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


                finish();
            }
        });
    }
}
