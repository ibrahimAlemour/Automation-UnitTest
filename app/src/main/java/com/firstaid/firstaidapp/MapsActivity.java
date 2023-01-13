package com.firstaid.firstaidapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firstaid.firstaidapp.model.AlarmDataSend;
import com.firstaid.firstaidapp.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.firstaid.firstaidapp.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    String[] status = {"medium", "slight","critical"};
    private Spinner spStatus;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    GPSTracker gpsTracker;
    int zoom = 8;
    ProgressDialog pd;
    Object item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(this);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            binding.tvName.setText(bundle.getString("name"));
            binding.tvCall.setText(bundle.getString("callPh"));
            Log.d("callMaps", "onCreate: " + bundle.getString("callPh"));


            binding.btnUploadAlarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String location = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
                    String des = binding.etDetail.getText().toString().trim();
                    sendAlarm(binding.tvName.getText().toString(), location, des, bundle.getString("callPh"));

                }
            });
        }
        if (gpsTracker.checkUpGps(true)) {


        } else {
            gpsTracker.showSettingsAlert();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        spStatus = (Spinner) findViewById(R.id.spStatus);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                status);

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spStatus.setAdapter(adapter);

        spStatus.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        item = parent.getItemAtPosition(pos);
                        System.out.println(item.toString());     //prints the text in spinner item.

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gpsTracker = new GPSTracker(this);
        // Add a marker in Sydney and move the camera

        float latMe =(float) gpsTracker.getLatitude();
        float lngMe =(float)gpsTracker.getLongitude();
        int cDistance = 2;

        int distance1 = (int) getKmFromLatLong((float)26.299736,(float)50.191795,latMe,lngMe);
        Log.e("Distance1", "onMapReady: "+distance1 );

        if (distance1>=cDistance){
            LatLng sydney1 = new LatLng(26.299736, 50.191795);
            mMap.addMarker(new MarkerOptions().position(sydney1).title("مستشفى الملك فهد الجامعي التعليمي").icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney1));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }

        int distance2 = (int) getKmFromLatLong((float)26.4101442,(float)50.048365,latMe,lngMe);
        if (distance2>=cDistance){
            LatLng sydney2 = new LatLng(26.4101442, 50.048365);
            mMap.addMarker(new MarkerOptions().position(sydney2).title("مستشفى المواساة").icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney2));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }



        int distance3 = (int) getKmFromLatLong((float)26.550971,(float)50.0126457,latMe,lngMe);
        if (distance3>=cDistance){
            LatLng sydney3 = new LatLng(26.550971, 50.0126457);
            mMap.addMarker(new MarkerOptions().position(sydney3).title("مركز صحي القطيف").icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney3));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }


        int distance4 = (int) getKmFromLatLong((float)26.3179607,(float)50.1631536,latMe,lngMe);
        if (distance4>=cDistance){
            LatLng sydney4 = new LatLng(26.3179607,50.1631536);
            mMap.addMarker(new MarkerOptions().position(sydney4).title("مركز صحي الدوحة").icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney4));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }


        int distance5 = (int) getKmFromLatLong((float)26.2647531,(float)50.203512,latMe,lngMe);
        if (distance5>=cDistance){
            LatLng sydney5 = new LatLng(26.2647531,50.203512);
            mMap.addMarker(new MarkerOptions().position(sydney5).title("مركز صحي ابن حيان").icon(BitmapDescriptorFactory.fromResource(R.drawable.health_icon)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney5));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }



        LatLng latLng2 = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(latLng2).title("موقعي"));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, zoom));

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null){
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            binding.tvLocation.setText(address + "-" + state);
        }

    }


    private void sendAlarm(String name, String location, String des, String phone) {

        pd = new ProgressDialog(this);
        pd.setMessage("loading...");
        pd.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AlarmDataSend dataSend = new AlarmDataSend(name, des, phone, location,item.toString(),true);
        db.collection("Alarm").document(des)
                .set(dataSend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(MapsActivity.this, "Send Success", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error adding Account", e);
                    }
                });



//
//
//                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//
//                    }
//                })
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