package com.example.dhirenchandnani.fuelo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.FormatException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import static com.example.dhirenchandnani.fuelo.FormActivity.resultsMain;

/**
 * Created by Dhiren Chandnani on 21-10-2016.
 */
public class FormActivity extends AppCompatActivity implements SpinnerDetails.AsyncResponse {



    public static final String PA="PA";
    public static final String BN="BN";
    public static final String Date="Date";
    public static final String Time="Time";
    public static final String Litres="Litres";
    public static final String CAR="Cars";
    public static String resultsMain;
    public static String[] results,results1,results2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        new SpinnerDetails(this).execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_window);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final EditText form_BNo = (EditText)findViewById(R.id.billNo);
        final EditText form_petrol_amount = (EditText)findViewById(R.id.petrolAmnt);
        final EditText form_date = (EditText)findViewById(R.id.editText3);
        final EditText form_time = (EditText)findViewById(R.id.editText4);
        final EditText form_nol = (EditText)findViewById(R.id.nOL);

//        new SpinnerDetails(this).execute();

        final Spinner spinner = (Spinner)findViewById(R.id.spinner);
//         Log.d("SPINNER-----",resultsMain);
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,resultsMain); //selected item will look like a spinner set from XML
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(spinnerArrayAdapter);


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
                                     String carV = spinner.getSelectedItem().toString();

                                     Intent intent2 = new Intent(FormActivity.this, ViewFormActivity.class);
                                     intent2.putExtra(PA, pa);
                                     intent2.putExtra(CAR, carV);
                                     intent2.putExtra(BN,bn);
                                     intent2.putExtra(Date,n_date);
                                     intent2.putExtra(Time,n_time);
                                     intent2.putExtra(Litres,noL);
                                     startActivity(intent2);
                                 }
                             });



    }

    @Override
    public void processFinish(String result){

        if(result != null && !result.isEmpty()){
//            results = result.split("/// ///");
//            results1 = results[0].split("///");
//            results2 = results[1].split("///");

//
//            if(results1[0].trim().equals("Success")) {
//                Log.e("check2",results1[0]);
                //results1 = Arrays.copyOfRange(results1,1,results1.length);
               // LoginActivity.userid = results[1];

               // Log.d("RESULTSMAIN",resultsMain);
         //   }

        }
        else{
            Toast toast= Toast.makeText(this, "Unable to connect to the server!", Toast.LENGTH_SHORT);
            toast.setMargin(150,150);
            toast.show();
        }

        Log.d("OPOPOPOP",result);
        FormActivity.resultsMain = result;
        Log.d("RESULTSMAIN",resultsMain);
        Log.d("RESULTSMAIN2", FormActivity.resultsMain);
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
    }


}

