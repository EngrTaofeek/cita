package com.taofeek.cita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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