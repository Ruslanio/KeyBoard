package com.example.ruslanio.keyboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Yura on 07.12.2017.
 */

public class LoginActivity extends AppCompatActivity {

    Button mSignIn;
    Button mSignUp;
    TextInputEditText mLogin;
    TextInputEditText mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSignIn= (Button) findViewById(R.id.sign_in);
        mSignUp= (Button) findViewById(R.id.sign_up);
        mLogin= (TextInputEditText) findViewById(R.id.login);
        mPassword= (TextInputEditText) findViewById(R.id.password);

        mSignIn.setOnClickListener(v -> {
            if("admin".equals(mLogin.getText().toString().trim())
                    && "admin".equals(mPassword.getText().toString().trim())){
                startActivity(new Intent(this,MainActivity.class));
                finish();
            }else {
                Toast.makeText(this,"Incorrect password or login!",Toast.LENGTH_SHORT).show();
            }
        });
        mSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this,RegistrationActivity.class));
        });
    }
}
