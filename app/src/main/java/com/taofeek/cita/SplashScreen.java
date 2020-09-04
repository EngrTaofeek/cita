package com.taofeek.cita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        imageView = findViewById(R.id.splash_image);
        // splash screen to open the Main Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, splashTimeOut);
        // create the animation
        Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splashanimation);
        imageView.startAnimation(myAnim);
    }
}