package com.cmu.ajou.spa;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class CardInformationActivity extends AppCompatActivity {

    private String identifier = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_information);

        Intent intent = getIntent();
        final String time = intent.getStringExtra("time");
        final String phoneNumber = intent.getStringExtra("phoneNumber");

        final TextView cardNumber = (TextView) findViewById(R.id.editTextCardNumber);
        final TextView cardMY = (TextView) findViewById(R.id.editTextMY);
        final TextView cardCSV = (TextView) findViewById(R.id.editTextCSV);

        final HTTPRequestTest req = new HTTPRequestTest(time, phoneNumber);

        Button btnNext = (Button)findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req.execute();
               // RegisterReservation rr = new RegisterReservation();
//                String status = "wait";
                Intent intent = new Intent(CardInformationActivity.this, OpenEnterGate.class);
              //  int result = rr.RegisterReservation(time, phoneNumber, intent);
//cardMY.getText().toString(), cardCSV.getText().toString()

                if(identifier == null) {

                } else {
                    intent.putExtra("time", time);

                    startActivity(intent);
                }
            }
        });
    }

    private class HTTPRequestTest extends AsyncTask<Void,Void,String> {

        private String url = ResourceClass.server_ip + "/surepark_server/rev/reservation.do";
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
            parameters.add("pReserTime", time);
            parameters.add("pReserTelno", phoneNumber);

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
                    identifier = jObject.getString("pIdentifier");
                    Log.d("TEST", identifier);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
