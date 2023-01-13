package com.firstaid.firstaidapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.firstaid.firstaidapp.model.Patient;
import com.firstaid.firstaidapp.registration.SignInActivity;
import com.firstaid.firstaidapp.utils.GPSTracker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView imgSignOut;
    private ImageView imgMyInformation;
    private ImageView imgMedicalConsultation;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private TextView tvName;
    GPSTracker gpsTracker;
    boolean isLocation = false;
    ActivityResultLauncher<String[]> mPLauncher;
    private FloatingActionButton btnAlarm;
    private TextView tvPhone;
    private FloatingActionButton btnCall;
    private TextView tvCallPhoneApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        getUser();
        setContentView(R.layout.activity_main);
        initView();


        getPhone();
        findLocations();


        imgMyInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyInformation.class));

            }
        });

        imgMedicalConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AdvanceActivity.class));

            }
        });


        imgSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (gpsTracker.checkUpGps(true)) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("name", tvName.getText().toString().trim());
                    intent.putExtra("callPh", tvCallPhoneApp.getText().toString().trim());
                    startActivity(intent);


                } else {
                    gpsTracker.showSettingsAlert();
                }


            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickWhatsapp(tvPhone.getText().toString());
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getUser() {
        String userID = mAuth.getCurrentUser().getUid();

        db.collection("Patient").document(userID)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            Patient patient = documentSnapshot.toObject(Patient.class);
                            tvName.setText(patient.getName());
                            tvCallPhoneApp.setText(patient.getPhone());


                            Log.d("PhonePatent", "DocumentSnapshot data2: " + patient.getPhone());
                        } else {
                            Log.d("TAG", "No such document");
                        }

                    }
                });


    }

    private void getPhone() {

        db.collection("DoctorsPhone").document("phone")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        tvPhone.setText("" + documentSnapshot.get("phone"));
                        Log.e("PhoneMainAc", "onSuccess: " + documentSnapshot.get("phone"));
                    }
                });

    }

    private void findLocations() {


        mPLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {

                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null) {

                    isLocation = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }

            }
        });

        requestPermissions();
    }


    private void requestPermissions() {
        isLocation = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        List<String> pRequest = new ArrayList<String>();

        if (!isLocation) {
            pRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!pRequest.isEmpty()) {

            mPLauncher.launch(pRequest.toArray(new String[0]));
        }

    }


    void clickWhatsapp(String phone) {

        Uri uri = Uri.parse("smsto:" + phone);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, "Contact With Doctors"));
    }


    private void initView() {

        imgSignOut = (ImageView) findViewById(R.id.imgSignOut);
        imgMyInformation = (ImageView) findViewById(R.id.imgMyInformation);
        imgMedicalConsultation = (ImageView) findViewById(R.id.imgMedicalConsultation);
        tvName = (TextView) findViewById(R.id.tvName);
        btnAlarm = (FloatingActionButton) findViewById(R.id.btnAlarm);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        btnCall = (FloatingActionButton) findViewById(R.id.btnCall);
        tvCallPhoneApp = (TextView) findViewById(R.id.tvCallPhoneApp);
    }
}