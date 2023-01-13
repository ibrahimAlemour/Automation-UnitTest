package com.firstaid.firstaidapp.registration;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.firstaid.firstaidapp.MainActivity;
import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpSecound extends AppCompatActivity {

    String[] gender = {"Mail", "Female"};
    String[] boold = {"O+", "O-", "B+", "B-", "A+", "A-", "AB+", "AB-"};
    private TextView tvName;
    private EditText etBlood;
    private EditText etChronic;
    private EditText etAllergy;
    private Spinner spGender;
    private TextView etAge;
    private TextView tvLocation;
    private AppCompatButton btnRegister;

    ProgressDialog pd;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userID;
    Object item;
    Object itemBlood;
    private Spinner spBoold;
    private EditText etIdNumber;
    private EditText etPhone;
    Date now;
    Date currentTime;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_secound);
        initView();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);
        pd.setMessage("loading...");

        //Date || Age
         now = new Date();
         currentTime = Calendar.getInstance().getTime();


        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                SetAge();
            }
        };
        etAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUpSecound.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //Blood
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                boold);

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spBoold.setAdapter(adapter);

        spBoold.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        itemBlood = parent.getItemAtPosition(pos);
                        System.out.println(itemBlood.toString());     //prints the text in spinner item.

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        //Gender

        ArrayAdapter ad = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                gender);

        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spGender.setAdapter(ad);


        spGender.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        item = parent.getItemAtPosition(pos);
                        System.out.println(item.toString());     //prints the text in spinner item.

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {


            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    createAccount(bundle.getString("email"), bundle.getString("password"), bundle.getString("name"));
                }
            });


        }


    }

    private void createAccount(String email, String password, String name) {
        final String chronic = etChronic.getText().toString().trim();
        String allergy = etAllergy.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String id = etIdNumber.getText().toString().trim();


        if (TextUtils.isEmpty(chronic)) {
            etChronic.setError("chronic is Required.");
            return;
        }

        if (TextUtils.isEmpty(allergy)) {
            etAllergy.setError("allergy is Required.");
            return;
        }

        if (TextUtils.isEmpty(age)) {
            etAge.setError("age is Required.");
            return;
        }


        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    userID = mAuth.getCurrentUser().getUid();
                    insertUserDB(email, password, itemBlood.toString(), chronic, allergy, item.toString(), age, name, userID,id,phone);


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: Email not sent " + e.getMessage());
            }
        });
    }

    private void insertUserDB(String email, String password, String bloodType, String chronic, String allergy, String gender, String age, String name, String uid,String idNumber,String phone) {

        Patient patient1 = new Patient(name,idNumber,email,password,bloodType,chronic,allergy,gender,age,phone);
        db.collection("Patient").document(uid)
                .set(patient1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        Toast.makeText(SignUpSecound.this, "Patient Created Account.", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        startActivity(new Intent(SignUpSecound.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error adding Account", e);
                    }
                });
    }

    private void SetAge(){
        etAge.setText(age(myCalendar.getTime(),currentTime)+"");
    }

    public static int age(Date birthday, Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(formatter.format(birthday));
        int d2 = Integer.parseInt(formatter.format(date));
        int age = (d2-d1)/10000;
        return age;
    }

    private void initView() {
        tvName = (TextView) findViewById(R.id.tvName);
        etBlood = (EditText) findViewById(R.id.etBlood);
        etChronic = (EditText) findViewById(R.id.etChronic);
        etAllergy = (EditText) findViewById(R.id.etAllergy);
        spGender = (Spinner) findViewById(R.id.spGender);
        etAge = (TextView) findViewById(R.id.etAge);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        btnRegister = (AppCompatButton) findViewById(R.id.btnRegister);
        spBoold = (Spinner) findViewById(R.id.spBoold);
        etIdNumber = (EditText) findViewById(R.id.etIdNumber);
        etPhone = (EditText) findViewById(R.id.etPhone);
    }
}