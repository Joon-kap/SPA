package com.cmu.ajou.spa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 2016-07-18.
 */
public class Parking_process extends AppCompatActivity {

    Button btnSend;
    TextView textSpot;
    TextView tvRecvData_2;
    String attempt = null;
    String identifier = null;
    String spot = null;
    String phone = null;
    String time = null;
    String card = null;
    RequestThread rt = null;
    boolean runThread = true;
    //TextView ReservationTime;

    protected void onDestroy() {
        super.onDestroy();

        Log.d("TEST", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa========");
        runThread = false;

    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking_process);

        textSpot = (TextView) findViewById(R.id.textSpot);
        //tvRecvData_2 = (TextView) findViewById(R.id.textAvailable_2);

/*
        for(int i=0; i<100; i++){
            if(attempt==null){
                new HTTPRequestTest().execute();
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                break;
            }

        }
*/
        Intent intent = getIntent();
        identifier = intent.getStringExtra("pIdentifier");
        spot = intent.getStringExtra("pSpotNumber");
        phone = intent.getStringExtra("phone");
        time = intent.getStringExtra("time");
        card = intent.getStringExtra("card");

        textSpot.setText(spot);

        rt = new RequestThread();
       // rt.setDaemon(true);
        rt.start();


    }

    @Override
    public void onBackPressed() {

    }

    private class RequestThread extends Thread{

        public void run() {
            while(runThread){
                new HTTPRequestTest().execute();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d("TEST", "Thead is run");
            }
        }
    }

    private class HTTPRequestTest extends AsyncTask<Void,Void,String> {

        private String url = ResourceClass.server_ip+"/surepark_server/rev/currentstatus.do";

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
            parameters.add("pIdentifier", identifier);

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

            String spotNum = null;
            String enterTime = null;
            String exitGate = null;


            try {
                JSONArray jarray = new JSONArray(s);
                /*
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    spotNum = jObject.getString("P_SPOT_NUMBER");
                    enterTime = jObject.getString("P_ENTER_TIME");
                    Log.d("TEST_1", spotNum);
                    Log.d("TEST_2", enterTime);


                }*/


                JSONObject jObject = jarray.getJSONObject(0); // JSONObject 추출
                spotNum = jObject.getString("P_SPOT_NUMBER");
                enterTime = jObject.getString("P_ENTER_TIME");
                exitGate = jObject.getString("EXIT_GATE");
                Log.d("TEST_1", spotNum);
                Log.d("TEST_2", enterTime);
                Log.d("TEST_3", exitGate);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(spotNum != null && enterTime != null && exitGate != null){
                Intent intent = new Intent(Parking_process.this, Payment_process.class);
                intent.putExtra("spotNum", spotNum);
                intent.putExtra("ExitGate", exitGate);
                intent.putExtra("enterTime", enterTime);
                intent.putExtra("pIdentifier", identifier);
                intent.putExtra("phone",phone);
                intent.putExtra("card", card);
                startActivity(intent);
                runThread = false;
            }

            // attempt = address_5;
            //Test

         //   Toast.makeText(getApplicationContext(), "pPresentParkinglotStatus Data : " + spotNum, Toast.LENGTH_SHORT).show();
         //   Toast.makeText(getApplicationContext(), "pServationTime Data : " + enterTime, Toast.LENGTH_SHORT).show();
            /*
            Toast.makeText(getApplicationContext(), "pEnterTime Data : " + address_3, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "pSpotNumber Data : " + address_4, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "pIdentifier Data : " + address_5, Toast.LENGTH_SHORT).show();
*/
            //tvRecvData_1.setText(address_2);
            //tvRecvData_2.setText(address_4);

        }

    }
}
