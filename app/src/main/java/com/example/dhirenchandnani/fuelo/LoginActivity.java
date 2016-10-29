package com.example.dhirenchandnani.fuelo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Dhiren Chandnani on 29-10-2016.
 */

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);




    }
    public void signupPost(View view){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

    }
}