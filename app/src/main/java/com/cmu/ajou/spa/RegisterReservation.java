package com.cmu.ajou.spa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Minseong on 2016-07-18.
 */
public class RegisterReservation {

    private String identifier = "";

    private String status = null;
    private Intent intent = null;

    public int RegisterReservation( String time, String phoneNumber, Intent intent) {
        new HTTPRequestTest(time, phoneNumber).execute();

        this.intent = intent;


  /*      while(true){
            if(status.equals("null"))
                break;
            if(status.equals(identifier))
                break;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

*/
        if(status.equals("null"))
            return 0;

        if(status.equals(identifier))
            return 1;

        return 0;


    }

    private class HTTPRequestTest extends AsyncTask<Void,Void,String> {

        private String url = "http://172.16.31.160:8080/surepark_server/rev/test.do";
        private String identi = "";
        private String time = "";
        private String phoneNumber = "";

        public HTTPRequestTest(String url) {
            this.url = url;
        }

        public HTTPRequestTest(String time, String phoneNumber) {

            this.url = url;
            this.identi = identi;
            this.time = time;
            this.phoneNumber = phoneNumber;

        }


        @Override
        protected String doInBackground(Void... params) {

            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
            parameters.add("time", time);
            parameters.add("phoneNumber", phoneNumber);

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

            Log.d("TEST", s);
            Log.d("TEST", "test");

           // String identifier = null;
            try {
                JSONArray jarray = new JSONArray(s);
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    identifier = jObject.getString("pidentifier");
                    Log.d("TEST", identifier);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            status = identifier;
            intent.putExtra("time", time);



        }

    }
}
