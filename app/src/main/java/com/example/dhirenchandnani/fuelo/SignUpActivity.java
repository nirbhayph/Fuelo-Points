package com.example.dhirenchandnani.fuelo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


/**
 * Created by Dhiren Chandnani on 29-10-2016.
 */

public class SignUpActivity extends AppCompatActivity {
    LinearLayout ll1,ll2,ll3,ll4,ll5;
    LinearLayout mainll;
    LinearLayout.LayoutParams layoutParams;
    static int c = 1;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_form);
        ll1=(LinearLayout) findViewById(R.id.Car_Details_1);
        ll2=(LinearLayout) findViewById(R.id.Car_Details_2);
        ll3=(LinearLayout) findViewById(R.id.Car_Details_3);
        ll4=(LinearLayout) findViewById(R.id.Car_Details_4);
        ll5=(LinearLayout) findViewById(R.id.Car_Details_5);
        layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);




    }
    public void addCar(View view){

        if(c==5)
            return;
        if(c==1)
            ll2.setVisibility(LinearLayout.VISIBLE);
        if(c==2)
            ll3.setVisibility(LinearLayout.VISIBLE);
        if(c==3)
            ll4.setVisibility(LinearLayout.VISIBLE);
        if(c==4)
            ll5.setVisibility(LinearLayout.VISIBLE);
        c++;
    }

    public void removeCar(View view){

        if(c==1)
            return;

        if(c==2) {
            ll2.setVisibility(LinearLayout.INVISIBLE);
            EditText k1 = (EditText) findViewById(R.id.ed2a);
            k1.setText("");
            EditText k2 = (EditText) findViewById(R.id.ed2b);
            k2.setText("");


        }
        if(c==3) {
            ll3.setVisibility(LinearLayout.INVISIBLE);
            EditText k1 = (EditText) findViewById(R.id.ed3a);
            k1.setText("");
            EditText k2 = (EditText) findViewById(R.id.ed3b);
            k2.setText("");
        }
        if(c==4) {
            ll4.setVisibility(LinearLayout.INVISIBLE);
            EditText k1 = (EditText) findViewById(R.id.ed4a);
            k1.setText("");
            EditText k2 = (EditText) findViewById(R.id.ed4b);
            k2.setText("");
        }
            if(c==5) {

                ll5.setVisibility(LinearLayout.INVISIBLE);
                EditText k1 = (EditText) findViewById(R.id.ed5a);
                k1.setText("");
                EditText k2 = (EditText) findViewById(R.id.ed5b);
                k2.setText("");
            }
                c--;
    }
}
