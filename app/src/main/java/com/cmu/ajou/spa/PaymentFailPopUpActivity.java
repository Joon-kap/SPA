package com.cmu.ajou.spa;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class PaymentFailPopUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // No Title bar
    //   requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setContentView(R.layout.activity_payment_fail_pop_up);

        Button btnOk = (Button)findViewById(R.id.btnOk);

        if (btnOk != null) {
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
