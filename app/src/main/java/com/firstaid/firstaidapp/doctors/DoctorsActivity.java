package com.firstaid.firstaidapp.doctors;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.firstaid.firstaidapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DoctorsActivity extends AppCompatActivity {

    private ImageView imgSignOut;
    private ImageView imgEmergencies;
    private ImageView imgPatient;
    private FloatingActionButton btnAlarm;
    private AppCompatButton btnEdit;
    private ProgressBar progressBar;
    private EditText etPhone;
    FirebaseFirestore db;
    private ImageView imgMedicalConsultation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        initView();


        //Phone Doctor
        db = FirebaseFirestore.getInstance();

        getPhone();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnEdit.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                String phone = etPhone.getText().toString().trim();

                Map<String, Object> user = new HashMap<>();
                user.put("phone", phone);

                // Add a new document with a generated ID
                db.collection("DoctorsPhone").document("phone")
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                btnEdit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(DoctorsActivity.this, " number updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        imgSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorsActivity.this, AddAdvancActivity.class));

            }
        });


        imgEmergencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DoctorsActivity.this, EmergenciesActivity.class));
            }
        });

        imgPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DoctorsActivity.this, PatentsActivity.class));
            }
        });


        imgMedicalConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DoctorsActivity.this, DAdvanceActivity.class));
            }
        });
    }

    private void getPhone() {
        db = FirebaseFirestore.getInstance();
        db.collection("DoctorsPhone").document("phone")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        etPhone.setText("" + documentSnapshot.get("phone"));
                        Log.e("PhoneMainAc", "onSuccess: " + documentSnapshot.get("phone"));
                    }
                });

    }

    private void initView() {
        imgSignOut = (ImageView) findViewById(R.id.imgSignOut);
        imgEmergencies = (ImageView) findViewById(R.id.imgEmergencies);
        imgPatient = (ImageView) findViewById(R.id.imgPatient);
        btnAlarm = (FloatingActionButton) findViewById(R.id.btnAlarm);
        btnEdit = (AppCompatButton) findViewById(R.id.btnEdit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        etPhone = (EditText) findViewById(R.id.et_phone);
        imgMedicalConsultation = (ImageView) findViewById(R.id.imgMedicalConsultation);
    }
}