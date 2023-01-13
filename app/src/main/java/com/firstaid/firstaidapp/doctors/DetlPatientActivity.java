package com.firstaid.firstaidapp.doctors;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.model.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetlPatientActivity extends AppCompatActivity {

    private ImageView imgBack;
    private TextView tvName;
    private TextView etBlood;
    private TextView etChronic;
    private TextView etAllergy;
    private TextView etAge;
    private TextView etGender;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private TextView etIdNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detl_patient);
        initView();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //receive
        Patient patient = (Patient) getIntent().getSerializableExtra("pa");
        if (patient != null) {

            etBlood.setText(patient.getBloodType());
            etAllergy.setText(patient.getAllergy());
            etChronic.setText(patient.getChronic());
            etAge.setText(patient.getAge());
            tvName.setText(patient.getName());
            etGender.setText(patient.getGender());
            etIdNumber.setText(patient.getIdNumber());
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvName = (TextView) findViewById(R.id.tvName);
        etBlood = (TextView) findViewById(R.id.etBlood);
        etChronic = (TextView) findViewById(R.id.etChronic);
        etAllergy = (TextView) findViewById(R.id.etAllergy);
        etAge = (TextView) findViewById(R.id.etAge);
        etGender = (TextView) findViewById(R.id.etGender);
        etIdNumber = (TextView) findViewById(R.id.etIdNumber);
    }


}