package com.example.ruslanio.keyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    private Button mLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLogOut = (Button) findViewById(R.id.btn_log_out);

        mLogOut.setOnClickListener(btn -> {
            Intent intent = new Intent(SettingsActivity.this , LoginActivity.class);
            startActivity(intent);
        });
    }
}
