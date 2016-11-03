package com.example.dhirenchandnani.fuelo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Dhiren Chandnani on 02-11-2016.
 */

public class OptionsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_form);
    }

    Button btn = (Button) findViewById(R.id.btn_fuel);

    public void BtnClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
