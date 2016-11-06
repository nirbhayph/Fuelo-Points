package com.example.dhirenchandnani.fuelo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by nirbhay on 11/1/16.
 */

public class SplashScreen extends AppCompatActivity{


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        //Added font Pacifico to Splash Screen
        TextView title = (TextView) findViewById(R.id.label_points1);
        Typeface font = Typeface.createFromAsset(this.getAssets(), "fonts/Pacifico.ttf");
        title.setTypeface(font);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(8500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);

                }
            }
        };
        timerThread.start();


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
