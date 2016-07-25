package com.cmu.ajou.spa;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

public class PaymentConfirmPopUpActivity extends AppCompatActivity {

    private String exitGateOpenUrl = ResourceClass.server_ip + "/surepark_server/rev/exitIdentify.do";
    private String identifier = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_payment_confirm_pop_up);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        String sDate = intent.getStringExtra("sDate");
        String eDate = intent.getStringExtra("eDate");
        String fee = intent.getStringExtra("fee");
        String card = intent.getStringExtra("card");
        identifier = intent.getStringExtra("pIdentifier");

        TextView txPhone = (TextView)findViewById(R.id.textNumber);
        TextView txFrom = (TextView)findViewById(R.id.textFrom);
        TextView txTo = (TextView)findViewById(R.id.textTo);
        TextView txPrice = (TextView)findViewById(R.id.textPrice);
        TextView txCard = (TextView)findViewById(R.id.textInfo);

        if (txPhone != null) {
            txPhone.setText(phone);
        }
        if (txFrom != null) {
            txFrom.setText(sDate);
        }
        if (txTo != null) {
            txTo.setText(eDate);
        }
        if (txPrice != null) {
            txPrice.setText(fee);
        }
        if (txCard != null) {
            txCard.setText(card);
        }

        Button btnCancel = (Button)findViewById(R.id.btnCancel);
        Button btnRegi = (Button)findViewById(R.id.btnPay);

        if (btnCancel != null) {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        }

        if (btnRegi != null) {
            btnRegi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    new HTTPRequestTest(exitGateOpenUrl,false).execute();

                }
            });
        }

    }

    private class HTTPRequestTest extends AsyncTask<Void,Void,String> {

        private String url = null;
        private boolean type2 = false;
        //= ResourceClass.server_ip + "/surepark_server/rev/exitProceed.do";

        public HTTPRequestTest(String url, boolean type2) {
            this.url = url;
            this.type2 = type2;
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
            Log.d("TEST", identifier);

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

            String status = null;
            String address_2 = null;
            String address_3 = null;
            String address_4 = null;

            try {
                JSONArray jarray = new JSONArray(s);
                JSONObject jObject = jarray.getJSONObject(0);  // JSONObject 추출
                status = jObject.getString("STATUS");



                Log.d("TEST_1", status);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(status.equals("SUCCESS")) {
                finish();

            } else if(status.equals("FAIL")) {
              Log.d("TEST", "FAIL!!!!!!!!!!!");
            }



            //Test
            Toast.makeText(getApplicationContext(), "stauts : " + status, Toast.LENGTH_SHORT).show();


            //tvRecvData_1.setText(address_2);
            //tvRecvData_2.setText(address_4);


            //tvRecvData.setText(address_2);

        }

    }

}
