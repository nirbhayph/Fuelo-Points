package com.example.dhirenchandnani.fuelo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Dhiren Chandnani on 21-10-2016.
 */
public class FormActivity extends AppCompatActivity {


    public static final String PA="PA";
    public static final String BN="BN";
    public static final String Date="Date";
    public static final String Time="Time";
    public static final String Litres="Litres";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_window);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText form_BNo = (EditText)findViewById(R.id.billNo);
        final EditText form_petrol_amount = (EditText)findViewById(R.id.petrolAmnt);
        final EditText form_date = (EditText)findViewById(R.id.editText3);
        final EditText form_time = (EditText)findViewById(R.id.editText4);
        final EditText form_nol = (EditText)findViewById(R.id.nOL);

        SimpleDateFormat dateF = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        SimpleDateFormat timeF = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String date = dateF.format(Calendar.getInstance().getTime());
        String time = timeF.format(Calendar.getInstance().getTime());

        form_date.setText(date);
        form_time.setText(time);

        fab.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {

                                     String n_date = form_date.getText().toString();
                                     String n_time = form_time.getText().toString();
                                     String bn = form_BNo.getText().toString();
                                     String pa = form_petrol_amount.getText().toString();
                                     String noL = form_nol.getText().toString();

                                     Intent intent2 = new Intent(FormActivity.this, ViewFormActivity.class);
                                     intent2.putExtra(PA, pa);
                                     intent2.putExtra(BN,bn);
                                     intent2.putExtra(Date,n_date);
                                     intent2.putExtra(Time,n_time);
                                     intent2.putExtra(Litres,noL);
                                     startActivity(intent2);
                                 }
                             });







    }


}
