package com.cmu.ajou.spa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CardNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_number);

        EditText etNum = (EditText)findViewById(R.id.editTextCardNumber);
        EditText etMY = (EditText)findViewById(R.id.editTextMY);
        EditText etCSV = (EditText)findViewById(R.id.editTextCSV);
        Button btnNext = (Button)findViewById(R.id.buttonNext);



        if (btnNext != null) {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CardNumberActivity.this, SelectCardActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
