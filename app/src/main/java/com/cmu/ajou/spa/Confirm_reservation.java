package com.cmu.ajou.spa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by bryan on 2016-07-18.
 */

public class Confirm_reservation extends AppCompatActivity {

    Button btnSend;
    Button btnCancel;
    TextView textTime;

    String identifier = null;
    String spot = null;
    String phone = null;
    String time = null;
    String card = null;

    //TextView ReservationTime;
    String LOG = "Confirm_reservation";
    String status = "wait";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_reservation);
//
        textTime = (TextView) findViewById(R.id.textTime);
        //tvRecvData_2 = (TextView) findViewById(R.id.textAvailable_2);

        Intent intent = getIntent();
        identifier = intent.getStringExtra("pIdentifier");
        spot = intent.getStringExtra("pSpotNumber");
        phone = intent.getStringExtra("phone");
        time = intent.getStringExtra("time");
        card = intent.getStringExtra("card");

        textTime.setText(time.substring(time.length() - 5, time.length()));

        Log.d(LOG, "identifier onCreate : " + identifier);


        btnSend = (Button)findViewById(R.id.gateOpenBtn);
        //ReservationTime = (EditText) findViewById(R.id.ReservationTime);

        btnCancel = (Button)findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG, "btn===========");

                new AlertDialog.Builder(Confirm_reservation.this)
                        .setMessage("Do you want to cancel the reservation?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = ResourceClass.server_ip + "/surepark_server/rev/cancel.do";
                                final HTTPRequestTest req = new HTTPRequestTest(url, identifier);
                                req.execute();

                                new CheckIdentifier().start();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(getBaseContext(),Payment_process.class);

                Log.d(LOG, "btn==================");
                Log.d(LOG, "setOnClickListener identifier : " + identifier);

                Date now = new Date();
                Calendar current = Calendar.getInstance();

                current.setTime(now);

                int cYear = current.get(Calendar.YEAR);
                int cMonth = current.get(Calendar.MONTH)+1;
                int cDate = current.get(Calendar.DATE);
                int cHour = current.get(Calendar.HOUR_OF_DAY);
                int cMin = current.get(Calendar.MINUTE);

                String cu = String.format("%02d%02d%02d%02d%02d", cYear,cMonth,cDate,cHour,cMin);
                time = time.replace(".", "");
                time = time.replace(":", "");
                time = time.replace(" ", "");
                long ire = Long.parseLong(time);
                long icu = Long.parseLong(cu);

                if(ire <= icu) {


                    // Intent intent = new Intent(getBaseContext(),Parking_process.class);
                    // startActivity(intent);
                    System.out.println("httpRe");
                    new HTTPRequestTest().execute();
                    System.out.println("checkidenti");

                    new CheckIdentifier().start();

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Confirm_reservation.this);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Press the Open button after reservation time.");
                    alert.show();
                }



               // startActivity(intent);
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

*/
    }

    @Override
    public void onBackPressed() {

    }

    private class CheckIdentifier extends Thread{

        public void run() {
            while(true){

                if(status.equals("wait")){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                Log.d(LOG, "run status :" + status);
                if(status.equals("FAIL")){

                    Intent intent = new Intent(Confirm_reservation.this, EnterFailPopUpActivity.class);
                    startActivity(intent);
                    Log.d(LOG, "====================run status :" + status);

                }else if(status.equals("SUCCESS")) {
                    Log.d(LOG, "====================run status :" + status);
                    Intent intent = new Intent(Confirm_reservation.this, Parking_process.class);
                    intent.putExtra("pIdentifier", identifier);
                    intent.putExtra("pSpotNumber", spot);
                    intent.putExtra("phone",phone);
                    intent.putExtra("time", time);
                    intent.putExtra("card", card);

                    startActivity(intent);
                }

                status = "wait";

                break;

            }
        }
    }

    private class HTTPRequestTest extends AsyncTask<Void,Void,String> {

        private String url = ResourceClass.server_ip + "/surepark_server/rev/identify.do";
        private String identi = "";

        public HTTPRequestTest(String url) {
            this.url = url;
        }

        public HTTPRequestTest(String url, String identi) {

            this.url = url;
            this.identi = identi;

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

            //status = null;

            try {
                JSONArray jarray = new JSONArray(s);
                /*
                for(int i=0; i < jarray.length(); i++){
                    JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                    address_1 = jObject.getString("pPresentParkinglotStatus");
                    address_2 = jObject.getString("pServationTime");
                    address_3 = jObject.getString("pEnterTime");
                    address_4 = jObject.getString("pSpotNumber");
                    Log.d("TEST_1", address_1);
                    Log.d("TEST_2", address_2);
                    Log.d("TEST_3", address_3);
                    Log.d("TEST_4", address_4);
                }*/

                JSONObject jObject = jarray.getJSONObject(0);  // JSONObject 추출
                status = jObject.getString("STATUS");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (status.equals("CANCEL")) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Confirm_reservation.this);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Confirm_reservation.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alert.setMessage("Your Reservation is canceled.");
                alert.show();

            } else if (status.equals("CANCEL_FAIL")) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Confirm_reservation.this);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setMessage("Your Reservation is not canceled.\nPlease try again.");
                alert.show();
            }


            //Test
            Toast.makeText(getApplicationContext(), "identify : " + status, Toast.LENGTH_SHORT).show();

           // tvRecvData_1.setText(address_2);
            //tvRecvData_2.setText(address_4);

        }

    }

}

