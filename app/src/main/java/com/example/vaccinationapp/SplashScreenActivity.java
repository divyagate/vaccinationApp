package com.example.vaccinationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.example.vaccinationapp.helpers.Constants;
import com.example.vaccinationapp.helpers.PrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    PrefManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Flag's for full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        manager = new PrefManager(this);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (manager.getData(Constants.EMAIL_ID) == null || manager.getData(Constants.EMAIL_ID).equals("")) {
                    //Do something after 3 Seconds
                    Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    //Do something after 3 Seconds
                    Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }, 3 * 1000);
    }
}