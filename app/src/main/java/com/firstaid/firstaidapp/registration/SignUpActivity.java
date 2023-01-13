package com.firstaid.firstaidapp.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.firstaid.firstaidapp.R;

public class SignUpActivity extends AppCompatActivity {

    private ImageView imLogo;
    private TextView tvName;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private AppCompatButton btnNext;
    private TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();


                if(TextUtils.isEmpty(name)){
                    etName.setError("Name is Required.");
                    return;
                }


                if(TextUtils.isEmpty(email)){
                    etEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    etPassword.setError("Password Must be >= 6 Characters");
                    return;
                }

                Intent intent = new Intent(SignUpActivity.this,SignUpSecound.class);
                intent.putExtra("name",name);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        
    }

    private void initView() {
        imLogo = (ImageView) findViewById(R.id.imLogo);
        tvName = (TextView) findViewById(R.id.tvName);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnNext = (AppCompatButton) findViewById(R.id.btnNext);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
    }
}