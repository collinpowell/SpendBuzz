package com.collinpowell.spendbuzz.Actitvities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.collinpowell.spendbuzz.R;


public class SplashScreen extends AppCompatActivity {
    ImageView image;
    TextView appName, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View mView = getWindow().getDecorView();

        //mView.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;

        mView.setSystemUiVisibility(uiOptions);
        image = findViewById(R.id.image);
        appName = findViewById(R.id.app_name);
        slogan = findViewById(R.id.slogan);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        final Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        final Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        image.setVisibility(View.VISIBLE);
        image.setAnimation(animation);
        appName.setVisibility(View.VISIBLE);
        slogan.setVisibility(View.VISIBLE);
        appName.setAnimation(animation1);
        slogan.setAnimation(animation1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), IntroScreen.class);
                startActivity(intent);
                finish();
            }
        }, 2500);



    }

}
