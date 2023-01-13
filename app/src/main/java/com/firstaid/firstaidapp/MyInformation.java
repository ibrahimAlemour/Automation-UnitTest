package com.firstaid.firstaidapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.firstaid.firstaidapp.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyInformation extends AppCompatActivity {

    private ImageView imgBack;
    private TextView tvName;
    private EditText etBlood;
    private EditText etChronic;
    private EditText etAllergy;
    private EditText etAge;
    private TextView tvLocation;
    private AppCompatButton btnEdit;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private EditText etPhone;
    private EditText etIdNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getUser();
        setContentView(R.layout.activity_my_information);
        initView();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updatePatient(etBlood.getText().toString(),
                        etAllergy.getText().toString(),
                        etChronic.getText().toString(),
                        etAge.getText().toString(),
                        etPhone.getText().toString(),
                        etIdNumber.getText().toString());
            }
        });
    }

    void updatePatient(String blood, String allergy, String chronic,String age,String phone,String idNumber) {

        ProgressDialog pd = new ProgressDialog(MyInformation.this);
        pd.setMessage("loading...");
        pd.show();

        db.collection("Patient").document(mAuth.getCurrentUser().getUid())
                .update("bloodType", blood
                        , "allergy", allergy
                        ,"chronic",chronic
                        ,"age",age
                        ,"phone",phone
                        ,"idNumber",idNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        pd.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }
                });



    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        tvName = (TextView) findViewById(R.id.tvName);
        etBlood = (EditText) findViewById(R.id.etBlood);
        etChronic = (EditText) findViewById(R.id.etChronic);
        etAllergy = (EditText) findViewById(R.id.etAllergy);
        etAge = (EditText) findViewById(R.id.etAge);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        btnEdit = (AppCompatButton) findViewById(R.id.btnEdit);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etIdNumber = (EditText) findViewById(R.id.etIdNumber);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getUser() {
        String userID = mAuth.getCurrentUser().getUid();

        db.collection("Patient").document(userID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                Patient patient = document.toObject(Patient.class);
                                etBlood.setText(patient.getBloodType());
                                etAllergy.setText(patient.getAllergy());
                                etChronic.setText(patient.getChronic());
                                etAge.setText(patient.getAge());
                                etPhone.setText(patient.getPhone());
                                etIdNumber.setText(patient.getIdNumber());

                                Log.d("TAG", "DocumentSnapshot data2: " + patient.getName());
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });

    }

}