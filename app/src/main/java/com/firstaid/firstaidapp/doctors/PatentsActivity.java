package com.firstaid.firstaidapp.doctors;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.adapter.AlarmAdapter;
import com.firstaid.firstaidapp.adapter.PatentAdapter;
import com.firstaid.firstaidapp.model.AlarmDataSend;
import com.firstaid.firstaidapp.model.Patient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatentsActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ProgressBar progressPar;
    private RecyclerView rvPatent;
    FirebaseFirestore db;
    ArrayList<Patient> arrayList;
    PatentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patents);
        initView();

        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<Patient>();
        adapter = new PatentAdapter(this, arrayList);
        rvPatent.setAdapter(adapter);

        getDataFromeCollectios();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }

    private void getDataFromeCollectios() {

        db.collection("Patient").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.isEmpty()) {
                            Log.d("TAG", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<Patient> types = documentSnapshots.toObjects(Patient.class);

                            // Add all to your list
                            arrayList.addAll(types);
                            // Log.d(TAG, "onSuccess: " + mArrayList);


                            adapter.notifyDataSetChanged();
                            progressPar.setVisibility(View.GONE);
                            rvPatent.setVisibility(View.VISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void initView() {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        progressPar = (ProgressBar) findViewById(R.id.progressPar);
        rvPatent = (RecyclerView) findViewById(R.id.rvPatent);
    }
}