package com.cmu.ajou.spa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpenExitGate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_exit_gate);

        Intent intent = getIntent();

        Button btOpen = (Button)findViewById(R.id.gateOpenBtn);
        assert btOpen != null;
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenExitGate.this, FinishActivity.class);
                startActivity(intent);
            }
        });
    }
}
