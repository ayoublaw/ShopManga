package com.android.shopmanga;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Spinner_List, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void ShowMaps(View view) {
        Geocoder geo = new Geocoder(this);
        double lat = 0,lng = 0;
        List<Address> addressList;
        EditText addresse = findViewById(R.id.autoCompleteTextView);
        String text = addresse.getText().toString();



        Intent intent = new Intent(this,MapsActivity.class);
        intent.putExtra("lat",lat);
        intent.putExtra("lng",lng);
        startActivity(intent);
    }
    
   /* public static void getLatLongFromGivenAddress(String youraddress, double lat, double lng) {
        try {
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + youraddress+ "&sensor=false&key=AIzaSyAejI8898winqzlekeYkhyJ2m1ZEPb3im0";

            URL url = new URL(googleMapUrl);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(
                    conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray resultJsonArray = jsonObj.getJSONArray("results");

            // Extract the Place descriptions from the results
            // resultList = new ArrayList<String>(resultJsonArray.length());

            JSONObject before_geometry_jsonObj = resultJsonArray
                    .getJSONObject(0);

            JSONObject geometry_jsonObj = before_geometry_jsonObj
                    .getJSONObject("geometry");

            JSONObject location_jsonObj = geometry_jsonObj
                    .getJSONObject("location");

            String lat_helper = location_jsonObj.getString("lat");
            lat = Double.valueOf(lat_helper);


            String lng_helper = location_jsonObj.getString("lng");
            lng = Double.valueOf(lng_helper);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
