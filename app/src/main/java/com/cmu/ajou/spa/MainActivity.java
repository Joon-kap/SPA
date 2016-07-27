package com.cmu.ajou.spa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
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

    TextView tvRecvData;
    TextView textPhoneNumber;

    Spinner sDate;
    Spinner sHour;
    Spinner sMin;

    int next = 0;
    private BackPressCloseHandler backPressCloseHandler;

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

        final Date cal = new Date();

        final Calendar current = Calendar.getInstance();
        final Calendar end = Calendar.getInstance();
        current.setTime(cal);
        end.setTime(cal);
        end.add(Calendar.HOUR, 3);


        int cMonth = current.get(Calendar.MONTH);
        final int cDate = current.get(Calendar.DATE);
        final int cHour = current.get(Calendar.HOUR_OF_DAY);
        int cMin = current.get(Calendar.MINUTE);
        int eMonth = end.get(Calendar.MONTH);
        final int eDate = end.get(Calendar.DATE);
        int eHour = end.get(Calendar.HOUR_OF_DAY);
        int eMin = end.get(Calendar.MINUTE);
        final int[] positionDate = {0};
        final int[] positionHour = {0};
        final int[] positionMin = {0};

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

        final String[] slMin = new String[60];
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

        final ArrayAdapter<String> slMinList;
        final ArrayAdapter<String> fMinList;
        final ArrayAdapter<String> lMinList;
        slMinList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, slMin);
        fMinList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fMin);
        lMinList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lMin);

        sMin.setAdapter(fMinList);


        if (sDate != null) {
            sDate.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 //   System.out.println(parent.getItemAtPosition(position));
                    positionDate[0] = position;

                    if(position==0 && Integer.valueOf(String.valueOf(sHour.getItemAtPosition(positionHour[0])))<3) {
                        sHour.setSelection(0);
                    } else if(position==1 && Integer.valueOf(String.valueOf(sHour.getItemAtPosition(positionHour[0])))>20) {
                        if(Integer.valueOf(String.valueOf(sHour.getItemAtPosition(1)))<3) {
                            sHour.setSelection(1);
                            sMin.setAdapter(slMinList);
                        } else if(Integer.valueOf(String.valueOf(sHour.getItemAtPosition(2)))<3) {
                            sHour.setSelection(2);
                            sMin.setAdapter(fMinList);
                        } else if(Integer.valueOf(String.valueOf(sHour.getItemAtPosition(3)))<3) {
                            sHour.setSelection(3);
                            sMin.setAdapter(lMinList);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (sHour != null) {
            sHour.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //    System.out.println(parent.getItemAtPosition(position));
                    positionHour[0] = position;

                    if(Integer.valueOf(String.valueOf(parent.getItemAtPosition(position)))<9 && Integer.valueOf(String.valueOf(parent.getItemAtPosition(0)))>9) {
                        sDate.setSelection(1);
                    } else {
                        sDate.setSelection(0);
                    }

                    if(position == 0) {
                        sMin.setAdapter(fMinList);
                    } else if (position == 1) {
                        sMin.setAdapter(slMinList);
                    } else if (position == 2) {
                        sMin.setAdapter(slMinList);
                    } else if (position == 3) {
                        sMin.setAdapter(lMinList);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (sMin != null) {
            sMin.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positionMin[0] = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        btnSend = (Button)findViewById(R.id.btnNext);
        tvRecvData = (TextView) findViewById(R.id.textAvailable);
        textPhoneNumber = (TextView) findViewById(R.id.textPhoneNumber);

    /*    TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String number = mTelephonyMgr.getLine1Number();

        Log.d("####", "Phone Number : " + number);
        if(number != null) {
            textPhoneNumber.setText(number);
        }
*/



                btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                int year = current.get(Calendar.YEAR);
                int month = current.get(Calendar.MONTH)+1;

                if(positionDate[0] == 1) {
                    year = end.get(Calendar.YEAR);
                    month = end.get(Calendar.MONTH)+1;
                }

                int date = Integer.valueOf(String.valueOf(sDate.getItemAtPosition(positionDate[0])));
                int hour = Integer.valueOf(String.valueOf(sHour.getItemAtPosition(positionHour[0])));
                int min = Integer.valueOf(String.valueOf(sMin.getItemAtPosition(positionMin[0])));

                Date now = new Date();
                Calendar current = Calendar.getInstance();

                current.setTime(now);

                int cYear = current.get(Calendar.YEAR);
                int cMonth = current.get(Calendar.MONTH)+1;
                int cDate = current.get(Calendar.DATE);
                int cHour = current.get(Calendar.HOUR_OF_DAY);
                int cMin = current.get(Calendar.MINUTE);

                String re = String.format("%02d%02d%02d%02d%02d", year,month,date,hour,min);
                String cu = String.format("%02d%02d%02d%02d%02d", cYear,cMonth,cDate,cHour,cMin);
                long ire = Long.parseLong(re);
                long icu = Long.parseLong(cu);
        //        System.out.println(re);
        //        System.out.println(cu);

        //        int compare = re.compareTo(cu);

        //        System.out.println(compare);

                if(ire < icu) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alert.setMessage("The reservation time is not available.");
                    alert.show();

                } else if(next == 1) {
                    Intent intent = new Intent(MainActivity.this, CardInformationActivity.class);

                    //핸드폰 번호
                    String phone = textPhoneNumber.getText().toString();
                //    phone = phone.replace("-", "");
                    intent.putExtra("phoneNumber", phone);

                    String iYear = String.format("%02d", year);
                    String iMonth = String.format("%02d", month);
                    String iDate = String.format("%02d", date);
                    String iHour = String.format("%02d", hour);
                    String iMin = String.format("%02d", min);

                    intent.putExtra("year", iYear);
                    intent.putExtra("month", iMonth);
                    intent.putExtra("date", iDate);
                    intent.putExtra("hour", iHour);
                    intent.putExtra("min", iMin);


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

    @Override
    protected void onRestart() {
        super.onRestart();

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:

             //   Toast.makeText(this, "Back Button is Pressed.", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(this)
                      //  .setTitle("프로그램 종료")
                        .setMessage("Do you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 프로세스 종료.
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                break;

            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
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


            String strRed = "#FF0000";
            String strGreen = "#00B050";

            if (avail != null) {
                tvRecvData.setText(avail+"/"+total);
                if(avail.equals("0")) {
                    tvRecvData.setTextColor(Color.parseColor(strRed));
                    next = 0;
                } else {
                    tvRecvData.setTextColor(Color.parseColor(strGreen));
                    next = 1;
                }
            }
            //서버에서 받아온 정보를 text로 표시(남은 주차 칸 수)
            //tvRecvData.setText(identifier);

        }

    }



}
