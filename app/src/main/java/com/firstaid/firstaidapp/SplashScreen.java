package com.firstaid.firstaidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.firstaid.firstaidapp.registration.SignInActivity;
import com.firstaid.firstaidapp.utils.GPSTracker;

public class SplashScreen extends AppCompatActivity {
    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        gpsTracker = new GPSTracker(this);



    }

    @Override
    protected void onStart() {
        super.onStart();

        if (gpsTracker.checkUpGps(true)) {
            HandlerStartMainActivity();

        } else {
            gpsTracker.showSettingsAlert();
        }

    }

    private final int SPLASH_DISPLAY_LENGTH = 1500;
    private void HandlerStartMainActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, SignInActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}