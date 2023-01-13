package com.firstaid.firstaidapp.doctors;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firstaid.firstaidapp.R;
import com.firstaid.firstaidapp.adapter.AlarmAdapter;
import com.firstaid.firstaidapp.model.AlarmDataSend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmergenciesActivity extends AppCompatActivity {

    private ImageView imgBack;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ArrayList<AlarmDataSend> arrayList;
    AlarmAdapter adapter;
    private ProgressBar progressPar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencies);
        initView();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        arrayList = new ArrayList<AlarmDataSend>();
        adapter = new AlarmAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

       // getDataFromeCollectios();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromeCollectios();
    }

    private void getDataFromeCollectios() {

        db.collection("Alarm").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        arrayList.clear();

                        if (documentSnapshots.isEmpty()) {
                            Log.d("TAG", "onSuccess: LIST EMPTY");
                            return;
                        } else {
                            // Convert the whole Query Snapshot to a list
                            // of objects directly! No need to fetch each
                            // document.
                            List<AlarmDataSend> types = documentSnapshots.toObjects(AlarmDataSend.class);

                            // Add all to your list
                            arrayList.addAll(types);
                           // Log.d(TAG, "onSuccess: " + mArrayList);


                            adapter.notifyDataSetChanged();
                            progressPar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
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
        recyclerView = (RecyclerView) findViewById(R.id.rvPlants);
    }


}