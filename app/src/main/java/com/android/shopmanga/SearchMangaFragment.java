package com.android.shopmanga;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SearchMangaFragment extends Fragment {

    String addressetext, mangaNametext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_manga, container, false);
        Button button =  view.findViewById(R.id.Searchbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geo = new Geocoder(getContext());
                double lat = 0,lng = 0;

                EditText addresse = getView().findViewById(R.id.autoCompleteTextView);
                EditText mangaName = getView().findViewById(R.id.MangaName);

                addressetext = addresse.getText().toString();
                mangaNametext = mangaName.getText().toString();

                LatLngTask myTask = new LatLngTask();  //can pass other variables as needed
                myTask.execute(addressetext,mangaNametext);
            }
        });
        return view;
    }

    public class LatLngTask extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... text) {
            StringBuilder jsonResults = new StringBuilder();
            String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + text[0]+ "&sensor=false&key=AIzaSyAejI8898winqzlekeYkhyJ2m1ZEPb3im0";

            try {
                URL url = new URL(googleMapUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
                return jsonResults.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            try {
                JSONObject jsonObj = new JSONObject(response);
                JSONArray resultJsonArray = jsonObj.getJSONArray("results");

                JSONObject before_geometry_jsonObj = resultJsonArray
                        .getJSONObject(0);

                JSONObject geometry_jsonObj = before_geometry_jsonObj
                        .getJSONObject("geometry");

                JSONObject location_jsonObj = geometry_jsonObj
                        .getJSONObject("location");

                String lat_helper = location_jsonObj.getString("lat");
                double lat = Double.valueOf(lat_helper);


                String lng_helper = location_jsonObj.getString("lng");
                double lng = Double.valueOf(lng_helper);

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("mangaName",mangaNametext);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
