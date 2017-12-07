package com.example.ruslanio.keyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by Yura on 07.12.2017.
 */

public class RegistrationActivity extends AppCompatActivity {

    Button mSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mSignUp= (Button) findViewById(R.id.sign_up);
        mSignUp.setOnClickListener(v -> {
            finish();
        });
    }
}
