package com.cmu.ajou.spa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class RegisterPopUpActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No Title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 팝업 외부 뿌연 효과
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
 //       layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        //효과 정도
 //       layoutParams.dimAmount = (float) 0.7;
        //적용
 //       getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_register_pop_up);

        final Intent intent = getIntent();
        final String phone = intent.getStringExtra("phoneNumber");
        final String iYear = intent.getStringExtra("year");
        final String iMonth = intent.getStringExtra("month");
        final String iDate = intent.getStringExtra("date");
        final String iHour = intent.getStringExtra("hour");
        final String iMin = intent.getStringExtra("min");
        final String card = intent.getStringExtra("card");

        TextView number = (TextView) findViewById(R.id.textNumber);
        TextView time = (TextView) findViewById(R.id.textTime);
        TextView info = (TextView) findViewById(R.id.textInfo);

        if (number != null) {
            number.setText(phone);
        }
        if (time != null) {
            time.setText(iYear + "." + iMonth + "." + iDate + " " + iHour + ":" + iMin);
        }
        if (info != null) {
            info.setText("**** **** **** " + card);
        }

        Button btnCancel = (Button)findViewById(R.id.btnCancel);
        Button btnRegi = (Button)findViewById(R.id.btnRegi);

        if (btnCancel != null) {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("cancel");
                    //intent.putExtra("regi", "not yet");
                   // RegisterPopUpActivity.this.finish();
                    intent.putExtra("result", "cancel");
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            });
        }

        if (btnRegi != null) {
            btnRegi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("ok");
                    intent.putExtra("result", "register");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }
}
