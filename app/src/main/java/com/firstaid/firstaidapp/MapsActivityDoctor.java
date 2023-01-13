package com.firstaid.firstaidapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firstaid.firstaidapp.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.firstaid.firstaidapp.databinding.ActivityMapsDoctorBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class MapsActivityDoctor extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsDoctorBinding binding;
    GPSTracker gpsTracker;
    int zoom = 8;
    String lat;
    String call;
    String des;
    TextView tvKm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(this);
        binding = ActivityMapsDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvKm = findViewById(R.id.tvKm);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            lat = bundle.getString("lat");
            call = bundle.getString("call");
            des = bundle.getString("des");
            Log.e("desMaps", "onCreate: "+des );

            binding.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickCallApp(call);
                }
            });

            binding.btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updatePatient(des);

                }

            });

        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String[] parts = lat.split(",");
        String lat1 = parts[0]; // 004
        String lng = parts[1];



        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(lat1), Double.parseDouble(lng));
        mMap.addMarker(new MarkerOptions().position(sydney).title("Patent").icon(BitmapDescriptorFactory.fromResource(R.drawable.health_medical_icon)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



        gpsTracker = new GPSTracker(this);
        LatLng latLng2 = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng2).title("موقعي"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, zoom));

        float distance = getKmFromLatLong((float)Double.parseDouble(lat1),(float)Double.parseDouble(lng),(float)gpsTracker.getLatitude(),(float)gpsTracker.getLongitude());
        tvKm.setText(new DecimalFormat("##.###").format(distance)+" "+"Km");
    }


    void clickCallApp(String phone) {

        Uri uri = Uri.parse("tel:"+phone);
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(Intent.createChooser(i, "Contact the patient"));
    }

    void updatePatient(String des) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("loading...");
        pd.show();

        db.collection("Alarm").document(des)
                .update("status",false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        pd.dismiss();
                        Toast.makeText(MapsActivityDoctor.this, "True", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }
                });



    }

    public static float getKmFromLatLong(float lat1, float lng1, float lat2, float lng2) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters/1000;
    }

}