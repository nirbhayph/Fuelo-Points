package com.example.dhirenchandnani.fuelo;

/**
 * Created by Dhiren Chandnani on 29-10-2016.
 */

import android.app.AlertDialog;
import android.os.AsyncTask;

import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.DefaultHttpClient;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.net.URI;
        import java.net.URL;
        import java.net.URLConnection;
        import java.net.URLEncoder;


public class SendDataActivity  extends AsyncTask<String,Void,String> {
    ProgressDialog am;
    private Context context;
    //flag 0 means get and 1 means post.(By default it is get.)
    public SendDataActivity(Context context) {
        this.context = context;


    }

    protected void onPreExecute(){
        am = new ProgressDialog(this.context, AlertDialog.THEME_HOLO_DARK);
        am.setTitle("Uploading your Bill Details");
        am.setMessage("Uploading ... ");
        am.show();
        Log.e("onPreExecutive", "called" + am);
    }

    @Override
    protected String doInBackground(String... arg0) {


        try{
            String bNo= (String)arg0[0];
            String petrolAmnt = (String)arg0[1];
            String date = (String)arg0[2];
            String time = (String)arg0[3];
            String noL = (String)arg0[4];
            String user_id = (String)arg0[5];
            String car_name = (String)arg0[6];
            String photo = (String)arg0[7];

            String link="http://109.73.164.163/FueloPoints/server_files/insert_client_bill_detail.php";
            String data  = URLEncoder.encode("billnox", "UTF-8") + "=" + URLEncoder.encode(bNo, "UTF-8");
            data += "&" + URLEncoder.encode("billamtx", "UTF-8") + "=" + URLEncoder.encode(petrolAmnt, "UTF-8");
            data += "&" + URLEncoder.encode("datex", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
            data += "&" + URLEncoder.encode("timex", "UTF-8") + "=" + URLEncoder.encode(time, "UTF-8");
            data += "&" + URLEncoder.encode("ltrsx", "UTF-8") + "=" + URLEncoder.encode(noL, "UTF-8");
            data += "&" + URLEncoder.encode("linkx", "UTF-8") + "=" + URLEncoder.encode(photo, "UTF-8");
            data += "&" + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user_id, "UTF-8");
            data += "&" + URLEncoder.encode("car_name", "UTF-8") + "=" + URLEncoder.encode(car_name, "UTF-8");

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
        am.dismiss();

        if(result != null && !result.isEmpty()){
            if(result.equals("Success")) {
                Intent intent = new Intent(this.context, MapsActivity.class);
                this.context.startActivity(intent);
            }
            Toast toast= Toast.makeText(this.context, "Bill Uploaded!", Toast.LENGTH_SHORT);
            toast.setMargin(150,150);
            toast.show();

        }
        else{
            Toast toast= Toast.makeText(this.context, "Unable to connect to the server!", Toast.LENGTH_SHORT);
            toast.setMargin(150,150);
            toast.show();
        }



    }
}

