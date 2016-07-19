package com.cmu.ajou.spa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_credit_card);

        Intent intent = getIntent();
        final String time = intent.getStringExtra("time");
        final String phoneNumber = intent.getStringExtra("phoneNumber");

        final RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroupCard);
   //     Button b = (Button)findViewById(R.id.radioButtonNC);
   /*     b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                System.out.println(rb.getText().toString());
            }
        });
*/
        Button btnNext = (Button)findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectCardActivity.this, CardInformationActivity.class);

                intent.putExtra("time", time);
                intent.putExtra("phoneNumber", phoneNumber);

                startActivity(intent);
            }
        });
    }
}
