package com.firstaid.firstaidapp.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.firstaid.firstaidapp.MainActivity;
import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.doctors.DoctorsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private ImageView imLogo;
    private TextView tvName;
    protected EditText etEmail;
    protected EditText etPassword;
    private AppCompatButton btnSignIn;
    private TextView tvSignUp;
    ProgressDialog pd;
    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();

        mAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setMessage("loading...");

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String admin = etEmail.getText().toString().trim();
                String adminPas = etPassword.getText().toString().trim();

                if (admin.contains("admin") && adminPas.contains("admin")){
                    startActivity(new Intent(SignInActivity.this, DoctorsActivity.class));

                }else {
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    signInAccount(email,password);
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    public void signInAccount(String email,String password) {

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is Required.");
            return ;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is Required.");
            return ;
        }

        if (password.length() < 6) {
            etPassword.setError("Password Must be >= 6 Characters");
            return ;
        }

        pd.show();

        // authenticate the user

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    pd.dismiss();

                } else {
                    Toast.makeText(SignInActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }

            }
        });



    }


    private void initView() {
        imLogo = (ImageView) findViewById(R.id.imLogo);
        tvName = (TextView) findViewById(R.id.tvName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSignIn = (AppCompatButton) findViewById(R.id.btnSignIn);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
    }
}