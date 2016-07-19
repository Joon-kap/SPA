package com.cmu.ajou.spa;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button btnSend;

    EditText etMessage;
    TextView tvRecvData;

    /*
    private EditText etMessage;
    private TextView tvRecvData;
    */

    /* github test */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new HTTPRequestTest().execute();

        tvRecvData = (TextView) findViewById(R.id.textAvailable);

        btnSend = (Button)findViewById(R.id.btnNext);

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(),confirm_reservation.class);
                startActivity(intent);
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

        private String url = "http://172.16.31.244:8080/surepark_server/rev/test.do";

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
            parameters.add("first_name", "shim");
            parameters.add("last_name", "sha sha");

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

            String str = "[{'1':'AAAA'}]";
            Log.d("TEST", s);
            Log.d("TEST", "test");

            String address = null;
            try {
                JSONArray jarray = new JSONArray(s);
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    address = jObject.getString("Test_1");
                    Log.d("TEST", address);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Parsed Data : " + s, Toast.LENGTH_SHORT).show();

            tvRecvData.setText(address);

        }

    }



}
