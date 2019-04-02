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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class SearchMangaFragment extends Fragment {

    private FusedLocationProviderClient client;

    AutoCompleteTextView mangaName;
    View view;

    Map<String, List<Double>> listMangaExist = new HashMap<String, List<Double>>();
    ArrayAdapter<Double> adapterSpinner;
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
        view = inflater.inflate(R.layout.fragment_search_manga, container, false);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        Button button = view.findViewById(R.id.Searchbutton);
        Button myPositionButton = view.findViewById(R.id.MyPositionButton);
        mangaName = view.findViewById(R.id.MangaName);

        AutoCompletionTask myTask = new AutoCompletionTask();
        myTask.execute();

        Spinner listVolume = view.findViewById(R.id.VolumeList);

        mangaName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                List<Double> list = listMangaExist.get(string);
                if(list == null)
                    adapterSpinner = new ArrayAdapter<Double>(getContext(),R.layout.support_simple_spinner_dropdown_item,new Double[]{Double.NaN});
                else{
                    list.add(Double.NaN);
                    adapterSpinner = new ArrayAdapter<Double>(getContext(),R.layout.support_simple_spinner_dropdown_item,list);
                }
                listVolume.setAdapter(adapterSpinner);
            }
        });



        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        EditText text = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);

        Places.initialize(getContext(), "AIzaSyAejI8898winqzlekeYkhyJ2m1ZEPb3im0");
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.ADDRESS, Place.Field.LAT_LNG));

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

                mangaNametext = mangaName.getText().toString();

                //myTask.execute(addressetext,mangaNametext);

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("mangaName",mangaNametext);
                if(!listVolume.getSelectedItem().toString().equals("NaN"))
                    intent.putExtra("volume",Double.valueOf(String.valueOf(listVolume.getSelectedItem())));
                startActivity(intent);


            }
        });
        return view;
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }
    public class AutoCompletionTask extends AsyncTask<String, Void , String> {

        @Override
        protected String doInBackground(String... text) {
            StringBuilder jsonResults = new StringBuilder();
            String apiUrl = "https://shopmangamobileapi.herokuapp.com/AllMangaAndVolume";

            try {
                URL url = new URL(apiUrl);
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
            Gson gson = new GsonBuilder().create();
            Type typeOfHashMap = new TypeToken<Map<String, List<Double>>>() { }.getType();
            listMangaExist = null;
            listMangaExist = gson.fromJson(response, typeOfHashMap);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item, listMangaExist.keySet().toArray(new String[listMangaExist.keySet().size()]));
            mangaName.setAdapter(adapter);

        }
    }
}
