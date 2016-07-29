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
public class Payment_process extends AppCompatActivity {

    TextView tvSpot;
    TextView tvI;
    TextView tvT;
    boolean runThread = true;
    RequestThread rt = null;
    String identifier = null;
    String spot = null;
    String startDate = null;
    String phone = null;
    String card = null;
    String endDate = null;
    String fee = null;
    String exitGate = null;
    String exitProceedUrl = ResourceClass.server_ip + "/surepark_server/rev/exitProceed.do";
    String exitGateOpenUrl = ResourceClass.server_ip + "/surepark_server/rev/exitIdentify.do";
    int statusG = 1;


    Button btnSend;
    //TextView ReservationTime;

    protected void onDestroy() {
        super.onDestroy();

        Log.d("TEST", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa========");
        runThread = false;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_process);

        //new HTTPRequestTest().execute();

        tvSpot = (TextView) findViewById(R.id.textSpot);
        tvI = (TextView) findViewById(R.id.infront);
        tvT = (TextView) findViewById(R.id.text);
        btnSend = (Button) findViewById(R.id.paymentBtn);

    //    tvRecvData_1 = (TextView) findViewById(R.id.textAvailable_1);
        //ReservationTime = (EditText) findViewById(R.id.ReservationTime);

        Intent intent = getIntent();
        identifier = intent.getStringExtra("pIdentifier");
        spot = intent.getStringExtra("spotNum");
        startDate = intent.getStringExtra("enterTime");
        phone = intent.getStringExtra("phone");
        card = intent.getStringExtra("card");
        exitGate = intent.getStringExtra("ExitGate");

        if(exitGate.equals("Detected") && spot.equals("0") && startDate.equals("0")) {
            tvSpot.setText("");
            tvI.setText("You are infront of the gate");
            tvT.setText("If you are going to go out,\nplease press the payment button.");

        } else {
            tvSpot.setText(spot);
        }

   //     tvTime.setText(enterTime);

        rt = new RequestThread();
        // rt.setDaemon(true);
        rt.start();


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new HTTPRequestTest(exitProceedUrl, true).execute();
        //        Intent intent = new Intent(getBaseContext(), Payment_process.class);

                Log.d("TEST", "Button!!!!!!!!!!!!!!!1");


            }
        });

    }

    @Override
    public void onBackPressed() {

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
                    fee = jObject.getString("pPayment");
                    endDate = jObject.getString("pExitTime");


                    Log.d("TEST_1", status);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(status.equals("SUCCESS") && statusG == 1) {
                runThread = false;
                if(type2){
                    Intent intent = new Intent(Payment_process.this, PaymentConfirmPopUpActivity.class);

                    intent.putExtra("phone", phone);
                    intent.putExtra("sDate", startDate);
                    intent.putExtra("eDate", endDate);
                    intent.putExtra("fee", fee);
                    intent.putExtra("card", card);


                    //startActivity(intent);
                    startActivityForResult(intent, RESULT_CANCELED);
                }
            } else if(status.equals("FAIL")) {
                if(type2){
                    Intent intent = new Intent(Payment_process.this, PaymentFailPopUpActivity.class);
                    startActivity(intent);
                }
            }



            //Test
        //    Toast.makeText(getApplicationContext(), "stauts : " + status, Toast.LENGTH_SHORT).show();


            //tvRecvData_1.setText(address_2);
            //tvRecvData_2.setText(address_4);


            //tvRecvData.setText(address_2);

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            new HTTPRequestTest(exitGateOpenUrl, true).execute();
            statusG = 0;
            //final HTTPRequestTest req = new HTTPRequestTest(time, phoneNumber);
            //req.execute();

            //new CheckIdentifier().start();
            Intent finish = new Intent(Payment_process.this, FinishActivity.class);
            startActivity(finish);
        }
    }

    private class RequestThread extends Thread{

        public void run() {
            while(runThread){
                new HTTPRequestTest(exitProceedUrl,false).execute();

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.d("TEST", "Thead is run");
            }
        }
    }


}


