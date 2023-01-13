package com.firstaid.firstaidapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firstaid.firstaidapp.adapter.AdvancAdapter;
import com.firstaid.firstaidapp.model.Advance;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdvanceActivity extends AppCompatActivity {

    private ImageView imgBack;
    private ProgressBar progressPar;
    private RecyclerView rvAdv;
    FirebaseFirestore db;
    ArrayList<Advance> arrayList;
    AdvancAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);
        initView();

        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<Advance>();
        adapter = new AdvancAdapter(this, arrayList);
        rvAdv.setAdapter(adapter);

        getAdvanceFromeCollectios();


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getAdvanceFromeCollectios() {

        db.collection("Advance").get()
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
                            List<Advance> types = documentSnapshots.toObjects(Advance.class);

                            // Add all to your list
                            arrayList.addAll(types);
                            // Log.d(TAG, "onSuccess: " + mArrayList);


                            adapter.notifyDataSetChanged();
                            progressPar.setVisibility(View.GONE);
                            rvAdv.setVisibility(View.VISIBLE);
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
        rvAdv = (RecyclerView) findViewById(R.id.rvAdv);
    }
}