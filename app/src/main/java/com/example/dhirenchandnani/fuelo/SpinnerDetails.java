package com.example.dhirenchandnani.fuelo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;

public class SpinnerDetails  extends AsyncTask<String,Void,String> {

//    private Context context;
//    //flag 0 means get and 1 means post.(By default it is get.)
//    public SpinnerDetails(Context context) {
//        this.context = context;
//    }


    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public SpinnerDetails(AsyncResponse delegate){
        this.delegate = delegate;
    }






    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {


        try{
            String userid= MapsActivity.userid;


            String link="http://109.73.164.163/FueloPoints/server_files/car_get.php";
            String data  = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write( data );
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            return sb.toString();
        }
        catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result){
//        am.dismiss();

        delegate.processFinish(result);






    }
}
