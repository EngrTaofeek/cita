package com.taofeek.cita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.taofeek.cita.customer.HomeActivity;
import com.taofeek.cita.organization.FacilityHomeActivity;
/*
    xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/nav_image_view"
        android:layout_width="100dp"
        android:layout_height="90dp"
        android:layout_marginStart="8dp"
        android:layout_marginLeft="8dp"
        android:layout_marginTop="8dp"
        android:contentDescription="@string/nav_header_desc"
        android:paddingTop="@dimen/nav_header_vertical_spacing"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/stadium"
        app:civ_border_width="2dp"
        app:civ_border_color="@color/color_gold"*/

public class SplashScreen extends AppCompatActivity {
    private ImageView imageView;
    private static int splashTimeOut = 5000;
    Animation animBlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        TextView splash_title = findViewById(R.id.splash_cita);
        splash_title.startAnimation(animBlink);


        // imageView = findViewById(R.id.splash_image);
        // splash screen to open the Main Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                final int data = prefs.getInt("login_state", 2);
                if (data == 1) {
                    final String state = prefs.getString("login_user_state", "customer");
                    if ( state == "facilitator"){
                        Intent existing_user_facility = new Intent(SplashScreen.this, FacilityHomeActivity.class);
                        startActivity(existing_user_facility);
                  } else {
                    Intent existing_user = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(existing_user);}

                } else {
                    Intent intent = new Intent(SplashScreen.this, OnboardingScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, splashTimeOut);
        // create the animation
//        Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splashanimation);
//        imageView.startAnimation(myAnim);
    }
}