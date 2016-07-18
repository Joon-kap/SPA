package com.cmu.ajou.spa;

import android.os.AsyncTask;
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

/**
 * Created by JK on 2016-07-17.
 */
public class HTTPRequest extends AsyncTask<Void,Void,String> {

    private String url = null;

    public HTTPRequest(String url) {
        this.url = url;
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

        String str = "[{'1':'AAAA'}]";
        Log.d("TEST", s);
        Log.d("TEST", "test");
        try {
            JSONArray jarray = new JSONArray(str);
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String address = jObject.getString("1");
                Log.d("TEST", address);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(getApplicationContext(), "Parsed Data : " + s, Toast.LENGTH_SHORT).show();



        //parseredData.show();
    }

}
