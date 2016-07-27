package com.cmu.ajou.spa;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static android.support.v4.app.ActivityCompat.startActivity;

public class CardInformationActivity extends AppCompatActivity {

    private PopupWindow mPopupWindow;

    private String identifier = null;
    private String spot = null;
    private String time = "";
    private String phoneNumber = "";
    private String phone = "";
    private String sYear = "";
    private String sMonth = "";
    private String sDate = "";
    private String sHour = "";
    private String sMin = "";
    private String sCard = "";
    private String timeForm = "";
    private String cardInfo = "";
    private static final char space = ' ';
    private static final char dash = '/';

    String LOG = "CardInformationActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_information);

        final Intent intent = getIntent();
        phone = intent.getStringExtra("phoneNumber");
        final String iYear = intent.getStringExtra("year");
        final String iMonth = intent.getStringExtra("month");
        final String iDate = intent.getStringExtra("date");
        final String iHour = intent.getStringExtra("hour");
        final String iMin = intent.getStringExtra("min");
        final String[] card = {""};
        sYear = iYear;
        sMonth = iMonth;
        sDate = iDate;
        sHour = iHour;
        sMin = iMin;



        final EditText cardNumber = (EditText) findViewById(R.id.editTextCardNumber);
 //       if (cardNumber != null) {
 //           cardNumber.setNextFocusDownId(R.id.editTextMY);
 //       }
        final EditText cardMY = (EditText) findViewById(R.id.editTextMY);
 //       if (cardMY != null) {
 //           cardMY.setNextFocusDownId(R.id.editTextCSV);
 //       }
        final EditText cardCSV = (EditText) findViewById(R.id.editTextCSV);
  //      if (cardCSV != null){
  //          cardCSV.setNextFocusDownId(R.id.btnNext);
  //      }

        if (cardNumber != null) {
            cardNumber.requestFocus();
        }
        // 키보드 포커스
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        if (cardNumber != null) {
            cardNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardNumber.setText("");
                }
            });

            cardNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                  /*  if(cardNumber.getText().length() == 4) {
                        String now = String.valueOf(cardNumber.getText());
                        now = now + "s";
                        System.out.println(now);
                        cardNumber.setText(now);
                    }
                    if(cardNumber.getText().length() == 9) {
                        String now = String.valueOf(cardNumber.getText());
                        now = now + " ";
                        cardNumber.setText(now);
                    }
                    if(cardNumber.getText().length() == 14) {
                        String now = String.valueOf(cardNumber.getText());
                        now = now + " ";
                        cardNumber.setText(now);
                    }
                  */

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Remove spacing char
                    if (s.length() > 0 && (s.length() % 5) == 0) {
                        final char c = s.charAt(s.length() - 1);
                        if (space == c) {
                            s.delete(s.length() - 1, s.length());
                        }
                    }
                    // Insert char where needed.
                    if (s.length() > 0 && (s.length() % 5) == 0) {
                        char c = s.charAt(s.length() - 1);
                        // Only if its a digit where there should be a space we insert a space
                        if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                            s.insert(s.length() - 1, String.valueOf(space));
                        }
                    }
                    if(cardNumber.getText().length() == 19) {
                        card[0] = String.valueOf(cardNumber.getText());
                        sCard = card[0].replace(" ", "");
                        if (cardMY != null) {
                            cardMY.requestFocus();
                        }
                    }
                }
            });
        }

        if (cardMY != null) {
            cardMY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardMY.setText("");

                }
            });
            cardMY.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 /*   if(cardMY.getText().length() == 2) {
                        String now = String.valueOf(cardNumber.getText());
                        now = now + "/";
                        cardMY.setText(now);
                    }
                  */
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Remove spacing char
                    if (s.length() > 0 && (s.length() % 3) == 0) {
                        final char c = s.charAt(s.length() - 1);
                        if (dash == c) {
                            s.delete(s.length() - 1, s.length());
                        }
                    }
                    // Insert char where needed.
                    if (s.length() > 0 && (s.length() % 3) == 0) {
                        char c = s.charAt(s.length() - 1);
                        // Only if its a digit where there should be a space we insert a space
                        if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(dash)).length <= 3) {
                            s.insert(s.length() - 1, String.valueOf(dash));
                        }
                    }
                    if(cardMY.getText().length() == 5) {
                        if(Integer.valueOf(String.valueOf(cardMY.getText()).substring(0,2))>12) {
                            cardMY.setText("");
                        } else if (cardCSV != null) {
                            cardCSV.requestFocus();
                        }
                    }
                }
            });
        }

        if (cardCSV != null) {
            cardCSV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardCSV.setText("");
                }
            });
            cardCSV.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(cardCSV.getText().length() == 3) {
                        InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    }
                }
            });
        }
//지우기
//        cardNumber.setText("1111 1111 1111 1111");
//        cardMY.setText("11/11");
//        cardCSV.setText("111");

        Button btnNext = (Button)findViewById(R.id.buttonNext);
        if (btnNext != null) {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String re = iYear + iMonth + iDate + iHour + iMin;

                    Date now = new Date();
                    Calendar current = Calendar.getInstance();

                    current.setTime(now);

                    int cYear = current.get(Calendar.YEAR);
                    int cMonth = current.get(Calendar.MONTH)+1;
                    int cDate = current.get(Calendar.DATE);
                    int cHour = current.get(Calendar.HOUR_OF_DAY);
                    int cMin = current.get(Calendar.MINUTE);

                    String cu = String.format("%02d%02d%02d%02d%02d", cYear,cMonth,cDate,cHour,cMin);

                    int compare = re.compareTo(cu);

                    if(compare < 0) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(CardInformationActivity.this);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(CardInformationActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        alert.setMessage("The reservation time is not available.");
                        alert.show();
                    } else if(cardNumber.getText().length() == 19 && cardMY.getText().length() == 5 && cardCSV.getText().length() ==3) {
                        Intent intent = new Intent(CardInformationActivity.this, RegisterPopUpActivity.class);

                        intent.putExtra("phoneNumber", phone);

                        timeForm = iYear + "." + iMonth + "." + iDate + " " + iHour + ":" + iMin;
                        intent.putExtra("time", timeForm);

                        cardInfo = card[0];
                        cardInfo = cardInfo.substring(cardInfo.length() - 4, cardInfo.length());
                        intent.putExtra("card", "**** **** **** " + cardInfo);
                        //   String sRegi = "not";
                        //  intent.putExtra("regi", sRegi);

                        time = iYear + iMonth + iDate + iHour + iMin;
                        phoneNumber = phone.replace("-", "");

                        startActivityForResult(intent, RESULT_CANCELED);
                    }
                     /*
                    req.execute();
                    // RegisterReservation rr = new RegisterReservation();
    //                String status = "wait";
                    //Intent intent = new Intent(CardInformationActivity.this, Confirm_reservation.class);
                    //  int result = rr.RegisterReservation(time, phoneNumber, intent);
    //cardMY.getText().toString(), cardCSV.getText().toString()
                    new CheckIdentifier().start();


                    if(identifier == null) {

                    } else {
                    }
       */         }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
            if (resultCode == RESULT_OK) {
                final HTTPRequestTest req = new HTTPRequestTest(time, phoneNumber);
                req.execute();

                    //RegisterReservation rr = new RegisterReservation();
                    //String status = "wait";
                    //Intent intent = new Intent(CardInformationActivity.this, Confirm_reservation.class);
                    //  int result = rr.RegisterReservation(time, phoneNumber, intent);
                    //cardMY.getText().toString(), cardCSV.getText().toString()
                 new CheckIdentifier().start();
            }
    }

    private void nextActivity(){
        if(identifier == null){

        }

    }

    private class CheckIdentifier extends Thread{

        public void run() {
            while(true){
                if(identifier == null){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                Log.d(LOG, "run identifier :" + identifier);
                Intent intent = new Intent(CardInformationActivity.this, Confirm_reservation.class);
                intent.putExtra("phone",phone);
                intent.putExtra("time", timeForm);
                intent.putExtra("card", "**** **** **** " + cardInfo);
                intent.putExtra("pIdentifier", identifier);
                intent.putExtra("pSpotNumber", spot);

                startActivity(intent);
                break;

            }
        }
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
                JSONObject jObject = jarray.getJSONObject(0); // JSONObject 추출
                identifier = jObject.getString("pIdentifier");
                spot = jObject.getString("pSpotNumber");
                Log.d(LOG, "onPostExecute identifier :" + identifier);
                Log.d(LOG, "onPostExecute soptNumber :" + spot);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
