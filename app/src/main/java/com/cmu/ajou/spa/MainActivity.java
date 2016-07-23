package com.cmu.ajou.spa;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.http.params.HttpConnectionParams;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button btnSend;
    Button btnSend1;
    Button btnSend2;

    EditText etMessage;
    TextView tvRecvData;
    TextView textPhoneNumber;

    Spinner sDate;
    Spinner sHour;
    Spinner sMin;

    int next = 0;

    /*
    private EditText etMessage;
    private TextView tvRecvData;
    */

    /* github test */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sDate = (Spinner)findViewById(R.id.select_date_spinner);
        sHour = (Spinner)findViewById(R.id.select_hour_spinner);
        sMin = (Spinner)findViewById(R.id.select_min_spinner);

        new HTTPRequestTest().execute();

        final Date date = new Date();

        Calendar current = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        current.setTime(date);
        end.setTime(date);
        end.add(Calendar.HOUR, 3);
        int cMonth = current.get(Calendar.MONTH);
        final int cDate = current.get(Calendar.DATE);
        final int cHour = current.get(Calendar.HOUR);
        int cMin = current.get(Calendar.MINUTE);
        int eMonth = end.get(Calendar.MONTH);
        final int eDate = end.get(Calendar.DATE);
        int eHour = end.get(Calendar.HOUR);
        int eMin = end.get(Calendar.MINUTE);


        if(cHour > 20) {
            String[] slDate = {String.valueOf(cDate), String.valueOf(eDate)};

            ArrayAdapter<String> dateList;
            dateList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, slDate);

            sDate.setAdapter(dateList);

        } else {
            String[] slDate = {String.valueOf(cDate)};

            ArrayAdapter<String> dateList;
            dateList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, slDate);

            sDate.setAdapter(dateList);

        }

        String[] slHour = {String.valueOf(cHour%24), String.valueOf((cHour+1)%24), String.valueOf((cHour+2)%24), String.valueOf((cHour+3)%24)};

        ArrayAdapter<String> hourList;
        hourList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, slHour);

        sHour.setAdapter(hourList);

        String[] slMin = new String[60];
        for(int i = 0; i<60; i++) {
            slMin[i] = String.valueOf(i);
        }

        List<String> fMin = new ArrayList<String>();
        for(int i = cMin; i < 60; i++) {
            fMin.add(String.valueOf(i));
        }

        List<String> lMin = new ArrayList<String>();
        for(int i = 0; i < cMin+1; i++) {
            lMin.add(String.valueOf(i));
        }

        ArrayAdapter<String> minList;
        minList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fMin);

        sMin.setAdapter(minList);


        if (sDate != null) {
            sDate.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    System.out.println(parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        btnSend = (Button)findViewById(R.id.btnNext);
        tvRecvData = (TextView) findViewById(R.id.textAvailable);
        textPhoneNumber = (TextView) findViewById(R.id.textPhoneNumber);

     //   btnSend1 = (Button)findViewById(R.id.btnNext1);
     //   btnSend2 = (Button)findViewById(R.id.btnNext2);

      //  tvRecvData.setText("aaaaaaaa");

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(next == 1) {
                    //           Intent intent = new Intent(getBaseContext(),HttpConnection2.class);
                    Intent intent = new Intent(MainActivity.this, SelectCardActivity.class);
                //    String text = spinner.getSelectedItem().toString();
                 //   Log.d("TEST", text);
                  //  text = text.replace(":", "");
                  //  Log.d("TEST", text);

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMdd");
                    String strNow = sdfNow.format(date);
                    Log.d("TEST", strNow);
                 //   strNow += text;


                    String phone = textPhoneNumber.getText().toString();
                    phone = phone.replace("-", "");
                    intent.putExtra("time", strNow);
                    intent.putExtra("phoneNumber", phone);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Now there is no parking spot is avaliable.", Toast.LENGTH_SHORT).show();
                }
            }
        });
/*
        btnSend1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(),HttpConnection.class);
                startActivity(intent);
            }
        });

        btnSend2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(),HttpConnection2.class);
                startActivity(intent);
            }
        });
        */
    }

    private class HTTPRequestTest extends AsyncTask<Void,Void,String> {

        private String url = ResourceClass.server_ip + "/surepark_server/rev/available.do";

        public HTTPRequestTest(String url) {
            this.url = url;
        }

        public HTTPRequestTest() {

        }


        @Override
        protected void onPreExecute() {

            //사이클 프로그래스바 시작
            /*
            setMessage("서버로부터 정보를 가져옵니다.");
            showLoadingProgressDialog();
            */
        }

        @Override
        protected String doInBackground(Void... params) {

            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

            RestTemplate restTemplate = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
            messageConverters.add(new FormHttpMessageConverter());
            messageConverters.add(new StringHttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);

            String result = restTemplate.postForObject(url, parameters, String.class);

            Log.d("TEST", result);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //사이클 프로그래스바 종료
            /*
            dismissProgressDialog();
            */
            //String sMessage = etMessage.getText().toString();   //보내는 메세지 수신

            //String[][] parsedData = jsonParserList(s);
            s = s.replace("null","");
            s = s.replace("(","[");
            s = s.replace(")","]");

            //String str = "[{'1':'AAAA'}]";
            Log.d("TEST", s);
            Log.d("TEST", "test");

            String avail = null;
            String total = null;

            try {

                JSONArray jarray = new JSONArray(s);
                JSONObject jObject = jarray.getJSONObject(0); // JSONObject 추출
                Log.d("TEST", jObject.getString("TOTAL_QTY"));
                Log.d("TEST", jObject.getString("AVABILE_QTY"));
                total = jObject.getString("TOTAL_QTY");
                avail = jObject.getString("AVABILE_QTY");
                /*
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i); // JSONObject 추출
                    if(jObject.getString("AVAILE_QTY") != null)
                        avail = jObject.getString("AVAILE_QTY");
                    if(jObject.getString("TOTAL_QTY") != null)
                        total = jObject.getString("TOTAL_QTY");

                }
                */
//                Log.d("TEST", avail);
 //               Log.d("TEST", total);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        //    Toast.makeText(getApplicationContext(), "Parsed Data : " + s, Toast.LENGTH_SHORT).show();

            tvRecvData.setText(avail+"/"+total);

            String strRed = "#FF0000";
            String strGreen = "#00B050";

            if(avail.equals("0")) {
                tvRecvData.setTextColor(Color.parseColor(strRed));
                next = 0;
            } else {
                tvRecvData.setTextColor(Color.parseColor(strGreen));
                next = 1;
            }
            //서버에서 받아온 정보를 text로 표시(남은 주차 칸 수)
            //tvRecvData.setText(identifier);

        }

    }



}
