package com.collinpowell.spendbuzz.Actitvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.collinpowell.spendbuzz.Adapters.IntroViewPagerAdapter;
import com.collinpowell.spendbuzz.Models.ScreenItem;
import com.collinpowell.spendbuzz.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroScreen extends AppCompatActivity {
    LinearLayout introLayout;
    ImageView introIconSlide,introImageNextButton;
    TabLayout tabIndicator;
    Button startBtn,skipBtn;
    Animation btnAnim,backgroundAnim;

    ViewPager screenPager;
    IntroViewPagerAdapter introViewPageAdapter;

    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(restorePrefData()){
            Intent main2Activity = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(main2Activity);
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        View mView = getWindow().getDecorView();

        //mView.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;

        mView.setSystemUiVisibility(uiOptions);
        // when this activity is about to be opened we need to check if it has been opened before or not

        //Hooks
        introLayout = findViewById(R.id.intro_layout);
        introIconSlide = findViewById(R.id.intro_imageView);
        tabIndicator = findViewById(R.id.tab_indicator);
        startBtn = findViewById(R.id.intro_StartButton);
        skipBtn = findViewById(R.id.skip_button);
        introImageNextButton = findViewById(R.id.Intro_imageView_button);
        screenPager = findViewById(R.id.new_view);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.intro_button_anim);
        backgroundAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.intro_text_anim);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Analyze Expenses","Track And Analyse Your Expenses, Income and Savings"));
        mList.add(new ScreenItem("Plan Budget","Plan Your Personal Budget"));
        mList.add(new ScreenItem("Organize Expenses","Organize expenses and income and record movement of money"));

        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Drawable layoutBackground1 = getApplicationContext().getResources().getDrawable(R.drawable.plan_budget);
                Drawable layoutBackground2 = getApplicationContext().getResources().getDrawable(R.drawable.plan_budget);
                Drawable layoutBackground3 = getApplicationContext().getResources().getDrawable(R.drawable.plan_budget);
               // Drawable layoutBackground4 = getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher_background);
               // Drawable layoutBackground5 = getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher_background);

                Drawable imageIcon1 = getApplicationContext().getResources().getDrawable(R.drawable.introslide1);
                Drawable imageIcon2 = getApplicationContext().getResources().getDrawable(R.drawable.plan_budget1);
                Drawable imageIcon3 = getApplicationContext().getResources().getDrawable(R.drawable.ic_baseline_storage_24);
                //Drawable imageIcon4 = getApplicationContext().getResources().getDrawable(R.drawable.introslide1);
                //Drawable imageIcon5 = getApplicationContext().getResources().getDrawable(R.drawable.introslide1);

                //change layout background/icon image on swipe
                position = screenPager.getCurrentItem();
                switch (position){
                    case 0:
                        introLayout.setBackground(layoutBackground1);
                        introIconSlide.setImageDrawable(imageIcon1);
                        break;
                    case 1:
                        introLayout.setBackground(layoutBackground2);
                        introIconSlide.setImageDrawable(imageIcon2);
                        break;
                    case 2:
                        introLayout.setBackground(layoutBackground3);
                        introIconSlide.setImageDrawable(imageIcon3);
                        break;
                    case 3:
                        //introLayout.setBackground(layoutBackground4);
                        //introIconSlide.setImageDrawable(imageIcon4);
                        break;
                    case 4:
                        //introLayout.setBackground(layoutBackground5);
                        //introIconSlide.setImageDrawable(imageIcon5);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        introLayout.setAnimation(backgroundAnim);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                savePrefsData();
                finish();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        // setup viewPager
        introViewPageAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPageAdapter);

        // setup tabLayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size()-1){
                    loadLastScreen();
                }else{
                    loadNormal();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //next button click listener
        introImageNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if(position < mList.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position == mList.size()-1){
                    //TODO:show the GETSTARTED button and HIDE the indicators and the next button
                    loadLastScreen();

                }
            }
        });
    }


    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActitvityOpenBefore = pref.getBoolean("isIntroOpened",false);
        return  isIntroActitvityOpenBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened",true);
        editor.commit();
    }


    private void loadNormal() {
        introImageNextButton.setVisibility(View.VISIBLE);
        startBtn.setVisibility(View.GONE);
        skipBtn.setVisibility(View.VISIBLE);
    }

    private void loadLastScreen() {
        introImageNextButton.setVisibility(View.GONE);
        skipBtn.setVisibility(View.GONE);
        startBtn.setVisibility(View.VISIBLE);

        startBtn.setAnimation(btnAnim);
    }
}