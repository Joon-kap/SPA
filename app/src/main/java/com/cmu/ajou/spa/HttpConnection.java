package com.cmu.ajou.spa;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;


public class HttpConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_connection);
        //new HttpConnTest().execute();

        AsyncHttpClient client = new AsyncHttpClient();
        async_post();
        /*
        client.get("https://www.google.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("TEST", "onSuccess");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("TEST", "onFailure");


            }
        });
        */

        RequestParams params = new RequestParams("single", "value");
        client.post("https://172.16.31.51:8080/surepark_server/rev/test.do",params, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("TEST", "onSuccess");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("TEST", "onFailure");


            }
        });

    }

    public void async_post(){
        AQuery aq = new AQuery(this);
        String url = "https://172.16.31.51:8080/surepark_server/rev/test.do";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("testValue", "8");
        params.put("callback", "jQuery1520458785286385228_1468471152085");
        params.put("_","1468471155297");



        AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {
                /*showResult(json);*/
                System.out.println("json : " + json);
                System.out.println("url : " + url);
                System.out.println("status : " + status);

            }
        };

        cb.url(url).type(JSONObject.class).params(params).weakHandler(this, "loginCallback");
        cb.header("Content-Type", "application/x-www-form-urlencoded");


/*
        aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject json, AjaxStatus status) {

                System.out.println("json : " + json);
                System.out.println("url : " + url);
                System.out.println("status : " + status);

            }
        });
*/
    }

    public void loginCallback(String url, JSONObject json, AjaxStatus status) throws JSONException {
        if (json != null) {

        }else{
            Log.d("TEST", "AAAAAAAAAAAAa");
        }
    }

    private class HttpConnTest extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... params1) {
            //기본적인 설정
            String url = "http://172.16.31.51:8080/surepark_server/rev/test.do";

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 3000);
            HttpConnectionParams.setSoTimeout(params, 3000);
            post.setHeader("Content-type", "application/json; charset=utf-8");

            // JSON OBject를 생성하고 데이터를 입력합니다.
            //여기서 처음에 봤던 데이터가 만들어집니다.
            JSONObject jObj = new JSONObject();
            try {
                jObj.put("name", "hong");
                jObj.put("phone", "000-0000");
                jObj.put("testValue", "7");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            try {
                // JSON을 String 형변환하여 httpEntity에 넣어줍니다.
                StringEntity se;
                se = new StringEntity(jObj.toString());
                HttpEntity he = se;
                post.setEntity(he);

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            try {
                //httpPost 를 서버로 보내고 응답을 받습니다.
                HttpResponse response = client.execute(post);
                // 받아온 응답으로부터 내용을 받아옵니다.
                // 단순한 string으로 읽어와 그내용을 리턴해줍니다.
                BufferedReader bufReader =
                        new BufferedReader(new InputStreamReader(
                                response.getEntity().getContent(),
                                "utf-8"
                        )
                        );

                String line = null;
                String result = "";

                while ((line = bufReader.readLine()) != null) {
                    result += line;
                }
                return result;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                return "Error" + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Error" + e.toString();
            }
//            return null;
        }
    }
}
