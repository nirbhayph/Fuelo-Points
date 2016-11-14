package com.example.dhirenchandnani.fuelo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListCategoriesActivity extends AppCompatActivity {

    public String[] category_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);
        Log.d("Entered LCA","");
        getJSON();




    }


    private void getJSON() {

        class GetJson extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d("DATA----------->",s);
                parseJSON(s);
                showData();
            }

            @Override
            protected String doInBackground(Void... params) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL("http://109.73.164.163/FueloPoints/server_files/get_unique_category_list.php");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    Log.d("JSON---->",sb.toString().trim());
                    return sb.toString().trim();


                }catch(Exception e){
                    return null;
                }

            }

        }
        GetJson gJ = new GetJson();
        gJ.execute();

}

    public void showData(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, category_list);
        ListView listView = (ListView) findViewById(R.id.list_category);
        listView.setAdapter(adapter);
    }

    private void parseJSON(String json){
        try {


            JSONArray jsonArray = new JSONArray(json);
            category_list = new String[jsonArray.length()];
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject k = jsonArray.getJSONObject(i);


                category_list[i] = getCategory(k);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private String getCategory(JSONObject j){
        String category = null;
        try {
            category = j.getString(Config.TAG_CATEGORY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return category;
    }



}
