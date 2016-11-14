package com.example.dhirenchandnani.fuelo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);
        String jsonCategories = getJSON();
        parseJSON(jsonCategories);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Config.category_list);

        ListView listView = (ListView) findViewById(R.id.list_category);
        listView.setAdapter(adapter);

    }


    protected String getJSON() {

        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(Config.GET_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String json;
            while((json = bufferedReader.readLine())!= null){
                sb.append(json+"\n");
            }

            return sb.toString().trim();

        }catch(Exception e){
            return null;
        }


    }

    private void parseJSON(String json){
        try {


            JSONArray jsonArray = new JSONArray(json);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject k = jsonArray.getJSONObject(i);

                Config.category_list[i] = getCategory(k);

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
