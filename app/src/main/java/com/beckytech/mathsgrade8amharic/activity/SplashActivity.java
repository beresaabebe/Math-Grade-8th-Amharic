package com.beckytech.mathsgrade8amharic.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.beckytech.mathsgrade8amharic.MainActivity;
import com.beckytech.mathsgrade8amharic.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView splash = findViewById(R.id.splashscreen);
        splash.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splash.getViewTreeObserver().addOnDrawListener(() -> {
                splash.setVisibility(View.GONE);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            });
        }
        else {
            new Handler().postDelayed(() -> {
                splash.setVisibility(View.VISIBLE);
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }, 1500);
        }
    }
}