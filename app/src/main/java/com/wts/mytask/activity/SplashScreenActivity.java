package com.wts.mytask.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wts.mytask.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static int  SPLASH_SCREEN_TIME_OUT=2000;
    SharedPreferences shp;
    String userId;

    ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        splashImage = findViewById(R.id.profile_image);
        splashImage.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter_from_bottom));

        shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                userId = shp.getString("userID", null);
                if (userId != null)
                {
                    Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                    startActivity(intent);

                }
            else
                {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

                }
            finish();

            }
        },SPLASH_SCREEN_TIME_OUT);


    }
}