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
        final String url = "http://128.237.216.182:8080/surepark_server/rev/test.do";

        @Override
        protected void onPreExecute() {

            //사이클 프로그래스바 시작
            /*
            setMessage("서버로부터 정보를 가져옵니다.");
            showLoadingProgressDialog();
            */
        }

        public String jsonParserList(String pRecvServerPage){
            Log.d("From Server: ",pRecvServerPage);
            String rcv_data_1 = pRecvServerPage;
            String rcv_data_2 = rcv_data_1.replace("null","");
            String rcv_data_3 = rcv_data_2.replace("(","[");
            String rcv_data_4 = rcv_data_3.replace(")","]");
            String rcv_data = rcv_data_4;


            //pRecvServerPage Should Be "[{'1':'AAAA'}]"
            /*
            String rcv_data_1 = pRecvServerPage.substring(11,23);
            String rcv_data_2 = "[";
            String rcv_data_3 = "]";
            String rcv_data = rcv_data_2 + rcv_data_1 + rcv_data_3;
            */
            //pRecvServerPage = null({"1":"AAAA"})
            String address=null;

            try {
                JSONArray jarray = new JSONArray(rcv_data);
                for(int i = 0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);
                    address = jObject.getString("1");
                    Log.d("TEST",address);
                }
                return address;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
        @Override
        protected String doInBackground(Void... params) {
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
            parameters.add("Phone_Number", "010-7149-3357");
            parameters.add("Reservation_Time", "11:30");
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
            //String sMessage = etMessage.getText().toString();   //보내는 메세지 수신
            //String[][] parsedData = jsonParserList(s);
            String k=jsonParserList(s);
            Toast.makeText(getApplicationContext(), "Parsed Data : " + k, Toast.LENGTH_SHORT).show();
        }
    }
}
