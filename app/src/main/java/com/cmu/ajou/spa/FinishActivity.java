package com.cmu.ajou.spa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

public class FinishActivity extends AppCompatActivity {
    Button  btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        btnHome = (Button) findViewById(R.id.btnHome);

        if (btnHome != null) {
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FinishActivity.this, MainActivity.class);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FinishActivity.this, MainActivity.class);
        startActivity(intent);
    }
}