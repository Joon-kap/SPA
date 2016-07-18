package com.cmu.ajou.spa;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpConnection2 extends AppCompatActivity  {

    TextView tx_txt;
    EditText et_str;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_connection2);

        tx_txt = (TextView) findViewById(R.id.tx_str);
        et_str = (EditText) findViewById(R.id.et_str);
        etMessage = (EditText)findViewById(R.id.et_message);
    }

    public void mOnClick(View v){
        new Post().execute();
    }

    private class Post extends AsyncTask<Void,Void,String> {

        //R.string.base_uri => http://192.168.1.102:8080
        final String url = "http://128.237.130.142:8080/surepark_server/rev/test.do";

        @Override
        protected void onPreExecute() {

            //사이클 프로그래스바 시작
            /*
            setMessage("서버로부터 정보를 가져옵니다.");
            showLoadingProgressDialog();
            */
        }

        public String[][] jsonParserList(String pRecvServerPage){
            //Log.i("");
            Log.i("From Server: ",pRecvServerPage);
            pRecvServerPage = "[{'1':'AAAA'}]";
//            String str_r = pRecvServerPage.substring(5,pRecvServerPage.length());

            try {
                //pRecvServerPage, Parsing -> parsedData


                JSONObject json = new JSONObject(pRecvServerPage);
                JSONArray jArr = json.getJSONArray("1");

                // 받아온 pRecvServerPage를 분석하는 부분
                String[] jsonName = {"AAAA"};
                String[][] parsedData = new String[jArr.length()][jsonName.length];

                for (int i = 0; i < jArr.length(); i++) {
                    json = jArr.getJSONObject(i);
                    if(json != null) {
                        for(int j = 0; j < jsonName.length; j++) {
                            parsedData[i][j] = json.getString(jsonName[j]);
                        }
                    }
                }
                Log.i("From Server: ",pRecvServerPage);

                // 분해 된 데이터를 확인하기 위한 부분
                for(int i=0; i<parsedData.length; i++){
                    Log.i("JSON을 분석한 데이터 "+i+" : ", parsedData[i][0]);
                }

                    return parsedData;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
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
            try {
                JSONArray jarray = new JSONArray(str);
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    String address = jObject.getString("1");
                    Log.d("TEST", address);
                    Log.d("TEST1", address);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Parsed Data : " + s, Toast.LENGTH_SHORT).show();
            Log.d("RESULT", s);
            Log.d("RESULT", "result");
            //parseredData.show();
        }
    }
}
