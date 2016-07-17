package com.cmu.ajou.spa;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class HttpConnection2 extends AppCompatActivity  {

    TextView tx_txt;
    EditText et_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_connection2);

        tx_txt = (TextView) findViewById(R.id.tx_str);
        et_str = (EditText) findViewById(R.id.et_str);
    }

    public void mOnClick(View v){
        new Post().execute();
    }

    private class Post extends AsyncTask<Void,Void,String> {

        //R.string.base_uri => http://192.168.1.102:8080
        final String url = "http://172.16.31.51:8080/surepark_server/rev/test5.do";

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

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            //사이클 프로그래스바 종료
            /*
            dismissProgressDialog();
            */
            Toast.makeText(getApplicationContext(), "결과값은 : " + s, Toast.LENGTH_SHORT).show();
        }
    }

}
