package com.cmu.ajou.spa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by bryan on 2016-07-18.
 */
public class payment_process extends AppCompatActivity {

    Button btnSend;
    //TextView ReservationTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_process);

        btnSend = (Button) findViewById(R.id.paymentBtn);
        //ReservationTime = (EditText) findViewById(R.id.ReservationTime);


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), payment_process.class);
                startActivity(intent);
            }
        });
    }

}
