package com.android.shopmanga;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class SearchMangaFragment extends Fragment {

    private FusedLocationProviderClient client;

    String mangaNametext;
    double lat, lng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_manga, container, false);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        Button button = view.findViewById(R.id.Searchbutton);
        Button myPositionButton = view.findViewById(R.id.MyPositionButton);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        EditText text = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);

        Places.initialize(getContext(), "AIzaSyAejI8898winqzlekeYkhyJ2m1ZEPb3im0");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                text.setText(place.getAddress());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
            }

            @Override
            public void onError(Status status) {
                Toasty.error(getContext(),"Error : can't choice this place",Toasty.LENGTH_LONG).show();
            }
        });

        myPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toasty.error(getContext(),"Error : Permisssion Denied",Toasty.LENGTH_LONG).show();
                    return;
                }
                client.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        text.setText("My position");
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                    }
                });
                client.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(getContext(),"Error : We can't find your position",Toasty.LENGTH_LONG).show();
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText mangaName = getView().findViewById(R.id.MangaName);
                mangaNametext = mangaName.getText().toString();

                //LatLngTask myTask = new LatLngTask();  //can pass other variables as needed
                //myTask.execute(addressetext,mangaNametext);

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("mangaName",mangaNametext);
                startActivity(intent);


            }
        });
        return view;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }
   /* public class LatLngTask extends AsyncTask<String, Void , String> {

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
    }*/

}
