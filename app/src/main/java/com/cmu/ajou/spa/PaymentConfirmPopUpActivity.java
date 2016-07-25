package com.cmu.ajou.spa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PaymentConfirmPopUpActivity extends AppCompatActivity {

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
                    finish();
                }
            });
        }

    }
}
